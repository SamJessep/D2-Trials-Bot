package nz.trialsBot.views;

import nz.trialsBot.interfaces.IMessageView;

import java.io.File;

public class ConsoleView implements IMessageView {
    @Override
    public void sendMessage(int chatIndex, String text) {
        System.out.println("chat:"+chatIndex+" message: "+text);
    }

    @Override
    public void sendMessageAll(String text) {

    }

    @Override
    public void sendMessage(int chatIndex, String text, File imageFile) {
        System.out.println("chat:"+chatIndex+"message: "+text);
        System.out.println("image: "+imageFile.getName());
    }

    @Override
    public void sendMessageAll(String text, File image) {

    }
}
