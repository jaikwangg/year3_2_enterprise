package kmitl.cs.entp.sarun;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main {
    public static void main(String[] args) {
        InitialContext initialContext;
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Queue queue;
        Session session;
        MessageConsumer consumer;
        TextMessage message;

        try {
            initialContext = new InitialContext();
            connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            queue = (Queue) initialContext.lookup("ExpiryQueue");

        }  catch (NamingException e) {
            throw new RuntimeException(e);
        }


        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(queue);
            connection.start();
            Message m = null;
            while (true) {
                m = consumer.receive();
                if (m != null) {
                    if (m instanceof TextMessage) {
                        message = (TextMessage) m;
                        System.out.println("Reading message: " + message.getText());
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }

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