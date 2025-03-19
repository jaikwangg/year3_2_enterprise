package kmitl.cs.entp.sarun;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;


public class Main {
    public static void main(String[] args) {
        InitialContext initialContext;
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Queue queue;
        TextMessage message = null;

        try {
            initialContext = new InitialContext();
            connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            queue = (Queue) initialContext.lookup("jms/SimpleJMSQueue");

        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueBrowser browser = session.createBrowser(queue);
            Enumeration msgs = browser.getEnumeration();
            if (!msgs.hasMoreElements()) {
                System.out.println("No messages in queue");
            } else {
                while (msgs.hasMoreElements()) {
                    Message tempMsg = (Message) msgs.nextElement();
                    if (tempMsg instanceof TextMessage) {
                        message = (TextMessage) tempMsg;
                        System.out.println(
                                "Reading message: " + message.getText());
                    } else {
                        System.out.println("Message is not a text message");
                    }
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