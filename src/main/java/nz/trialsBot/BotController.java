package nz.trialsBot;

import nz.trialsBot.interfaces.IInfoCollector;
import nz.trialsBot.interfaces.IMessageView;

import java.io.File;
import java.io.IOException;
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
        String messageText = String.format("Trials rewards for %s", date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        messageBot.sendMessageAll(messageText,image);
        System.out.println("Waiting for 10 seconds, then closing chromedrivers");
        try{Thread.sleep(10000);}catch (Exception e){e.printStackTrace();}
        messageBot.close();
        infoCollector.close();

        cleanupChromeProcesses();
    }

    public static void cleanupChromeProcesses() {
        String systemType = System.getProperty("os.name").toLowerCase();
        if (systemType.contains("win")) {
            try {
                System.out.println("Close one or more driver exe files");
                Runtime.getRuntime().exec("taskkill /f /im chromedriver.exe");
            } catch (IOException e) {
                System.out.println("Failed to close one or more driver exe files");
            }
        }
    }
}
