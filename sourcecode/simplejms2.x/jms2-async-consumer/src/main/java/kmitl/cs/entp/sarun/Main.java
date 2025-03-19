package kmitl.cs.entp.sarun;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        InitialContext initialContext;
        Topic topic;
        Queue queue;
        String destType;
        Destination dest = null;
        InputStreamReader inputStreamReader = null;
        char answer = '\0';

        if (args.length != 1) {
            System.err.println("Program takes one argument: <dest_type>");
            System.exit(1);
        }

        destType = args[0];
        System.out.println("Destination type is " + destType);

        if (!(destType.equals("queue") || destType.equals("topic"))) {
            System.err.println("Argument must be \"queue\" or " + "\"topic\"");
            System.exit(1);
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
                dest = (Destination) queue;
            } else {
                dest = (Destination) topic;
            }
        } catch (Exception e) {
            System.err.println("Error setting destination: " + e.toString());
            System.exit(1);
        }
        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {

            JMSConsumer consumer = jmsContext.createConsumer(dest);
            TextMessage message = jmsContext.createTextMessage();
            TextListener listener = new TextListener();
            consumer.setMessageListener(listener);
            System.out.println(
                    "To end program, type Q or q, " + "then <return>");
            inputStreamReader = new InputStreamReader(System.in);

            while (!((answer == 'q') || (answer == 'Q'))) {
                try {
                    answer = (char) inputStreamReader.read();
                } catch (IOException e) {
                    System.err.println("I/O exception: " + e.toString());
                }
            }
        }
    }
}