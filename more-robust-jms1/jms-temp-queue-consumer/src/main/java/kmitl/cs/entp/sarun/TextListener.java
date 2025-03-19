package kmitl.cs.entp.sarun;

import javax.jms.*;

public class TextListener implements MessageListener {
    private MessageProducer replyProducer;
    private Session session;

    public TextListener(Session session) {
        this.session = session;
        try {
            replyProducer = session.createProducer(null);

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onMessage(Message message) {
        TextMessage msg = null;

        try {
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
                System.out.println("Reading message: " + msg.getText());
            } else {
                System.err.println("Message is not a TextMessage");
            }
            // set up the reply message
            TextMessage response = session.createTextMessage("Hello back");
            response.setJMSCorrelationID(message.getJMSCorrelationID());
            System.out.println("sending message " + response.getText());
            replyProducer.send(message.getJMSReplyTo(), response);
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage(): " + e.toString());
        } catch (Throwable t) {
            System.err.println("Exception in onMessage():" + t.getMessage());
        }
    }
}
