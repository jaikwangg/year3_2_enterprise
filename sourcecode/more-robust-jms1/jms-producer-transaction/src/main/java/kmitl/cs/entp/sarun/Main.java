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
        MessageProducer producer;
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
            session = connection.createSession(true, 0);
            producer = session.createProducer(queue);
            message = session.createTextMessage();
            message.setText("Withdraw A 500");
            System.out.println("Sending message: " + message.getText());
            producer.send(message);
            //int i = 1 / 0;
            message.setText("Deposit B 500");
            System.out.println("Sending message: " + message.getText());
            producer.send(message);
            producer.send(session.createMessage()); //send empty message
            session.commit();

            producer.send(session.createMessage());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        catch(Exception e) {
            try {
                assert session != null;
                session.rollback();
            } catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
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