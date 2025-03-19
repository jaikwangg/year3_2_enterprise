package kmitl.cs.entp.sarun;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main {
    public static void main(String[] args) {
        InitialContext initialContext;
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Topic topic;
        Queue queue;
        String destType;
        Destination dest = null;
        Session session;
        MessageProducer producer;
        TextMessage message;

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
            connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
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
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(dest);
            message = session.createTextMessage();
            for (int i = 0; i < NUM_MSGS; i++) {
                message.setText("This is message " + (i + 1));
                System.out.println("Sending message: " + message.getText());
                producer.send(message);
            }

            producer.send(session.createMessage());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException ignored) {

                }
            }
        }

    }
}