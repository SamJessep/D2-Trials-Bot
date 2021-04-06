package nz.trialsBot.models;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class LightGgTrials extends BaseScreenshoter {
    private static final String TARGET_URL = "https://www.light.gg/";

    @Override
    public File getRewardsScreenshot() {
        initialize();
        try{
            ((JavascriptExecutor) _driver).executeScript("arguments[0].scrollIntoView(true);", _driver.findElement(By.cssSelector("#trials-billboard > div:nth-child(2) > div.subheader > h2 > span > a > img")));
            Thread.sleep(500);
        }catch(Exception e){
            e.printStackTrace();
        }
        return resize(crop(((TakesScreenshot)_driver).getScreenshotAs(OutputType.FILE)));
    }

    private File crop(File screenshot) {
        WebElement ele = _driver.findElement(By.id("trials-billboard"));
        // Get entire page screenshot
        BufferedImage fullImg = null;
        try{
            fullImg = ImageIO.read(screenshot);
        }catch (Exception e){
            e.printStackTrace();
        }
        // Get the location of element on the page
        Point point = ele.getLocation();
        // Get width and height of the element
        int eleWidth = ele.getSize().getWidth();
        int eleHeight = ele.getSize().getHeight();

        JavascriptExecutor executor = (JavascriptExecutor) _driver;
        int value = Math.toIntExact((Long)executor.executeScript("return window.pageYOffset;"));
        // Crop the entire page screenshot to get only element screenshot
        BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY()-value,
                eleWidth, eleHeight);
        try{
            ImageIO.write(eleScreenshot, "png", screenshot);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  screenshot;
    }


    private void initialize(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("window-size=550,888");
        _driver = new ChromeDriver(options);
        _driver.get(TARGET_URL);
    }
}
