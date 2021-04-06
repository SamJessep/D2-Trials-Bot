package interfaces;

import java.io.File;

public interface IMessageView {
    /**
     * Send message with only a text message
     * @param text the text message contents to send
     */
    public void sendMessage(String text);

    /**
     * Send a message to users with text message and image
     * @param text the text message contents to send
     * @param image the image file to send
     */
    public void sendMessage(String text, File image);
}
