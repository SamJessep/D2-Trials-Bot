import models.SeleniumInfoCollector;
import views.ConsoleView;
import views.PsnView;

public class Main {

    public static void main(String[] args) {
        String PSN_EMAIL = args[0];
        String PSN_PASSWORD = args[1];
        new BotController(new SeleniumInfoCollector(), new PsnView(PSN_EMAIL, PSN_PASSWORD)).run();
    }
}
