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
            queue = (Queue) initialContext.lookup("jms/SimpleJMSQueue");

        }  catch (NamingException e) {
            throw new RuntimeException(e);
        }

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             //JMSContext jmsContext = connectionFactory.createContext()) {
             JMSContext jmsContext = connectionFactory.createContext(JMSContext.SESSION_TRANSACTED)) {

            JMSConsumer consumer = jmsContext.createConsumer(queue);
            TextMessage message;
            while (true) {
                Message m = consumer.receive(5000);
                if (m != null) {
                    if (m instanceof TextMessage) {
                        message = (TextMessage) m;
                        System.out.println("Reading message: " + message.getText());

                    } else {
                        break;
                    }
                } else {
                    System.out.println("No message");
                    break;
                }
            }
            jmsContext.commit();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}