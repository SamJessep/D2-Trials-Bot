import interfaces.IInfoCollector;
import interfaces.IMessageView;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BotController {

    IInfoCollector infoCollector;
    IMessageView messageBot;

    public BotController(IInfoCollector collector, IMessageView messageView){
        infoCollector = collector;
        messageBot = messageView;
    }

    public void run(){
        LocalDate date = LocalDate.now();
        File image = infoCollector.getRewardsScreenshot();
        String messageText = String.format("Trials rewards %s", date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        messageBot.sendMessage(messageText,image);
    }
}
