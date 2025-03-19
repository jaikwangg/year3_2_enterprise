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
        Session session = null;
        MessageConsumer consumer;
        TextMessage message;
        try {
            initialContext = new InitialContext();
            connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            queue = (Queue) initialContext.lookup("jms/SimpleJMSQueue");

        }  catch (NamingException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //session = connection.createSession(true, 0);
            consumer = session.createConsumer(queue);
            connection.start();
            message = session.createTextMessage();
            while (true) {
                Message m = consumer.receive(5000);
                if (m != null) {
                    if (m instanceof TextMessage) {
                        message = (TextMessage) m;
                        System.out.println(
                                "Reading message: " + message.getText());

                    } else {
                        break;
                    }
                }
                else {
                    System.out.println("No message");
                    break;
                }
            }
            //session.commit();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException ignored) {

                }
            }
        }

    }
}