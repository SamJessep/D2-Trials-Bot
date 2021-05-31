package nz.trialsBot.models;

import nz.trialsBot.interfaces.IInfoCollector;
import org.imgscalr.Scalr;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class BaseScreenshoter implements IInfoCollector {

    protected WebDriver _driver;
    protected WebDriverWait _wait;

    public BaseScreenshoter() {
        ChromeOptions options = new ChromeOptions();
        String userData = getUserDir();
        options.addArguments(
                "--disable-blink-features=AutomationControlled",
                "--no-sandbox",
                "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "+
                        "Chrome/87.0.4280.141 Safari/537.36",
                "user-data-dir="+userData);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("window-size=550,888");
        _driver = new ChromeDriver(options);
        _wait = new WebDriverWait(_driver, 10);
    }

    private String getUserDir(){
        try {
            return new File("./.user-dataBS").getCanonicalPath();
        } catch (IOException e) {
            return "./user-dataBS";
        }
    }

    @Override
    public abstract File getRewardsScreenshot();

    @Override
    public void close() {
        _driver.quit();
    }

    public File resize(File screenshot){
        try{
            BufferedImage img = Scalr.resize(ImageIO.read(screenshot), 1000);
            ImageIO.write(img, "png", screenshot);
        }catch (Exception e){
            e.printStackTrace();
        }
        return screenshot;
    }
}
