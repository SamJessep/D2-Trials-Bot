package views;

import interfaces.IMessageView;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileOutputStream;

public class ConsoleView implements IMessageView {
    @Override
    public void sendMessage(String text) {
        System.out.println("message: "+text);
    }

    @Override
    public void sendMessage(String text, File imageFile) {
        System.out.println("message: "+text);
        System.out.println("image: "+imageFile.getName());
    }
}
