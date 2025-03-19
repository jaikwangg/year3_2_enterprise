package kmitl.cs.entp.sarun;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
//demonstrate working with Message Property
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
                dest =  queue;
            } else {
                dest =  topic;
            }
        } catch (Exception e) {
            System.err.println("Error setting destination: " + e.toString());
            System.exit(1);
        }

        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(dest);
            connection.start();
            do {
                message = (TextMessage) consumer.receive();
                System.out.println("Reading message: " + message.getText());
            } while (!message.getBooleanProperty("last"));

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