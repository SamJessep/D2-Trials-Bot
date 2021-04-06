package nz.trialsBot.models;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;

public class KybersCornerTrials extends BaseScreenshoter {

    private static final String TARGET_URL = "http://kyber3000.com/Trials";

    @Override
    public File getRewardsScreenshot() {
        initialize();
        File screenshot = ((TakesScreenshot)_driver).getScreenshotAs(OutputType.FILE);
        return resize(screenshot);
    }



    private void initialize(){
        _driver = new ChromeDriver();
        _driver.get(TARGET_URL);
    }
}
