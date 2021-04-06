package models;

import interfaces.IInfoCollector;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Base64;

public class SeleniumInfoCollector implements IInfoCollector {
    protected WebDriver _driver;
    private static final String TARGET_URL = "http://kyber3000.com/Trials";
    @Override
    public File getRewardsScreenshot() {
        initialize();
        return ((TakesScreenshot)_driver).getScreenshotAs(OutputType.FILE);
    }

    private void initialize(){
        _driver = new ChromeDriver();
        _driver.get(TARGET_URL);
    }
}
