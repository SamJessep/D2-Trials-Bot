package nz.trialsBot.models;

import nz.trialsBot.interfaces.IInfoCollector;
import org.imgscalr.Scalr;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class BaseScreenshoter implements IInfoCollector {

    protected WebDriver _driver;

    @Override
    public File getRewardsScreenshot() {
        return null;
    }

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
