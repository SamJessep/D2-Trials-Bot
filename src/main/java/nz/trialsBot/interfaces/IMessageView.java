package nz.trialsBot.interfaces;

import java.io.File;

public interface IMessageView {
    /**
     * Send message with only a text message
     * @param chatIndex chat index number, used to determine where to post the message
     * @param text the text message contents to send
     */
    void sendMessage(int chatIndex, String text);
    /**
     * Send message with only a text message
     * @param text the text message contents to send
     */
    void sendMessageAll(String text);

    /**
     * Send a message to users with text message and image
     * @param chatIndex chat index number, used to determine where to post the message
     * @param text the text message contents to send
     * @param image the image file to send
     */
    void sendMessage(int chatIndex, String text, File image);
    /**
     * Send a message to users with text message and image
     * @param text the text message contents to send
     * @param image the image file to send
     */
    void sendMessageAll(String text, File image);
}
