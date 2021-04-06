package nz.trialsBot;

import nz.trialsBot.models.LightGgTrials;
import org.openqa.selenium.chrome.ChromeDriverService;
import nz.trialsBot.views.PsnView;

public class Main {

    public static void main(String[] args) {
        String PSN_EMAIL = args[0];
        String PSN_PASSWORD = args[1];
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
        new BotController(new LightGgTrials(), new PsnView(PSN_EMAIL, PSN_PASSWORD)).run();
    }
}
