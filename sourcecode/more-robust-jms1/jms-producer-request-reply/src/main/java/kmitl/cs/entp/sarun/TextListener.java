package kmitl.cs.entp.sarun;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class TextListener implements MessageListener {
    TextMessage msg = null;
    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
                System.out.println("Reading message: " + msg.getText() + " " +
                        msg.getJMSCorrelationID());
            } else {
                System.err.println("Message is not a TextMessage");
            }
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage(): " + e.toString());
        } catch (Throwable t) {
            System.err.println("Exception in onMessage():" + t.getMessage());
        }
    }
}
