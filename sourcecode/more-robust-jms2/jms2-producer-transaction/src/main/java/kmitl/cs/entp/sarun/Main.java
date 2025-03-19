package kmitl.cs.entp.sarun;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;


public class Main {
    public static void main(String[] args) {
        InitialContext initialContext;
        Queue queue;
        JMSContext jmsContext = null;
        try {
            initialContext = new InitialContext();
            queue = (Queue) initialContext.lookup("jms/SimpleJMSQueue");

        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory()) {

             jmsContext = connectionFactory.createContext(Session.SESSION_TRANSACTED);
             JMSProducer producer = jmsContext.createProducer();
             TextMessage message = jmsContext.createTextMessage();
             message.setText("Withdraw A 500");
             System.out.println("Sending message: " + message.getText());
             producer.send(queue, message);
             //int i = 1 / 0;
             message.setText("Deposit B 500");
             System.out.println("Sending message: " + message.getText());
             producer.send(queue,message);
             producer.send(queue, jmsContext.createMessage()); //send empty message
             jmsContext.commit();

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        catch (Exception e) {
            System.out.println("error divide by zero");
            assert jmsContext != null;
            jmsContext.rollback();
        }
        finally {
            assert jmsContext != null;
            jmsContext.close();
        }
    }
}