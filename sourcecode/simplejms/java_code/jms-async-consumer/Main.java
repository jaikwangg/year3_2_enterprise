package kmitl.cs.entp.sarun;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.InputStreamReader;

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
        MessageConsumer consumer;
        TextMessage message;
        TextListener listener = null;
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
            consumer = session.createConsumer(dest);
            listener = new TextListener();
            consumer.setMessageListener(listener);
            connection.start();
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
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
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