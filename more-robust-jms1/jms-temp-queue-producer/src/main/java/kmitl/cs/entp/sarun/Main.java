package kmitl.cs.entp.sarun;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InitialContext initialContext;
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Queue requestQueue;
        Session session;
        MessageProducer producer;
        TextMessage message;
        TextListener listener;
        try {
            initialContext = new InitialContext();
            connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            requestQueue = (Queue) initialContext.lookup("RequestQueue");

        }  catch (NamingException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(requestQueue);
            listener = new TextListener();
            //Create a temporary queue that this client will listen for responses on then create a consumer
            //that consumes message from this temporary queue...for a real application a client should reuse
            //the same temp queue for each message to the server...one temp queue per client
            Queue tempDest = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(tempDest);
            responseConsumer.setMessageListener(listener);
            message = session.createTextMessage();
            message.setText("Hello friend" );
            message.setJMSReplyTo(tempDest);
            String correlationId = "12345";
            message.setJMSCorrelationID(correlationId);
            connection.start();
            System.out.println("Sending message: " + message.getText());
            producer.send(message);
            String ch;
            Scanner inp = new Scanner(System.in);
            while(true) {
                System.out.print("Press q to quit ");
                ch = inp.nextLine();
                if (ch.equals("q")) {
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