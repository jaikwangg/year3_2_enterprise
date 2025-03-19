package kmitl.cs.entp.sarun;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main {
    public static void main(String[] args) {
        InitialContext initialContext;
        Topic topic;

        try {
            initialContext = new InitialContext();
            topic = (Topic) initialContext.lookup("jms/SimpleJMSTopic");

        }  catch (NamingException e) {
            throw new RuntimeException(e);
        }

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {

            jmsContext.setClientID("durableSub");
            //JMSConsumer consumer = jmsContext.createConsumer(topic);
            JMSConsumer consumer = jmsContext.createDurableConsumer(topic, "subscription1");
            consumer.close();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            jmsContext.unsubscribe("subscription1");
            consumer = jmsContext.createDurableConsumer(topic, "subscription1");
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