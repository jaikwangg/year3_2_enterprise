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
        Queue replyQueue;
        Session session;
        MessageProducer producer;
        TextMessage message;
        TextListener listener;
        try {
            initialContext = new InitialContext();
            connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            requestQueue = (Queue) initialContext.lookup("RequestQueue");
            replyQueue = (Queue) initialContext.lookup("ReplyQueue");


        }  catch (NamingException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(requestQueue);
            listener = new TextListener();
            MessageConsumer responseConsumer = session.createConsumer(replyQueue);
            responseConsumer.setMessageListener(listener);
            message = session.createTextMessage();
            message.setText("Hello friend" );
            message.setJMSReplyTo(replyQueue);
            //Set a correlation ID so when you get a response you know which sent message the response is for
            //String correlationId = "12345";
           //message.setJMSCorrelationID(correlationId);
            //or we can use message ID
            connection.start();
            System.out.println("Sending message: " + message.getText());
            producer.send(message);
            System.out.println("message id: " + message.getJMSMessageID());
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