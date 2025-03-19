package kmitl.cs.entp.sarun;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main {
    public static void main(String[] args) {
        InitialContext initialContext;
        Queue queue;

        try {
            initialContext = new InitialContext();
            queue = (Queue) initialContext.lookup("ExpiryQueue");


        }  catch (NamingException e) {
            throw new RuntimeException(e);
        }

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {

            JMSConsumer consumer = jmsContext.createConsumer(queue);
            TextMessage message;
            while (true) {
                Message m = consumer.receive();
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
        }
    }
}