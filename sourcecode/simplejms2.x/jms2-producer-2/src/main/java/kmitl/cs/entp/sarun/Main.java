package kmitl.cs.entp.sarun;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main {
    public static void main(String[] args) {
        InitialContext initialContext;
        Topic topic;
        Queue queue;
        String destType;
        Destination dest = null;

        final int NUM_MSGS;
        if ((args.length < 1) || (args.length > 2)) {
            System.err.println(
                    "Program takes one or two arguments: "
                            + "<dest_type> [<number-of-messages>]");
            System.exit(1);
        }

        destType = args[0];
        System.out.println("Destination type is " + destType);

        if (!(destType.equals("queue") || destType.equals("topic"))) {
            System.err.println("Argument must be \"queue\" or " + "\"topic\"");
            System.exit(1);
        }

        if (args.length == 2) {
            NUM_MSGS = Integer.parseInt(args[1]);
        } else {
            NUM_MSGS = 1;
        }

        try {
            initialContext = new InitialContext();
            queue = (Queue) initialContext.lookup("jms/SimpleJMSQueue");
            topic = (Topic) initialContext.lookup("jms/SimpleJMSTopic");
        }  catch (NamingException e) {
            throw new RuntimeException(e);
        }
        try {
            if (destType.equals("queue")) {
                dest = queue;
            } else {
                dest = topic;
            }
        } catch (Exception e) {
            System.err.println("Error setting destination: " + e);
            System.exit(1);
        }
        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {

            JMSProducer producer = jmsContext.createProducer();
            String message;
            for (int i = 0; i < NUM_MSGS; i++) {
                message = "This is message " + (i + 1);
                System.out.println("Sending message: " + message);
                producer.send(dest,message);
            }
            //send ending message
            producer.send(dest,"last");
        }
    }
}