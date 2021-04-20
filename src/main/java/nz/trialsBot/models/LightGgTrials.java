package nz.trialsBot.models;

import nz.trialsBot.views.helpers.RewardsDrawer;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LightGgTrials extends BaseScreenshoter {
    private static final String TARGET_URL = "https://www.light.gg/";

    @Override
    public File getRewardsScreenshot() {
        initialize();
        return makeRewardsScreenshot();
    }

    private File takeRewardsScreenshot(){
        try{
            ((JavascriptExecutor) _driver).executeScript("arguments[0].scrollIntoView(true);", _driver.findElement(By.cssSelector("#trials-billboard > div:nth-child(2) > div.subheader > h2 > span > a > img")));
            Thread.sleep(500);
        }catch(Exception e){
            e.printStackTrace();
        }
        return resize(crop(((TakesScreenshot)_driver).getScreenshotAs(OutputType.FILE)));
    }
    private File makeRewardsScreenshot(){
        List<String> threeWins = getRewardImages(".rewards-0 img");
        List<String> fiveWins = getRewardImages(".rewards-1 img");
        List<String> sevenWins = getRewardImages(".rewards-2 img");
        List<String> flawless = getRewardImages(".rewards-3 img");
        String mapName = _driver.findElement(By.cssSelector("#trials-billboard .map-name")).getText().trim();
        String defaultBaseDir = System.getProperty("java.io.tmpdir");
        File outfile = new File(defaultBaseDir+"out.jpg");
        try {
            BufferedImage img = RewardsDrawer.drawRewards(threeWins,fiveWins,sevenWins,flawless,mapName);
            ImageIO.write(img,"jpg",outfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outfile;
    }

    private List<String> getRewardImages(String cssSelector) {
        List<String> imgUrls = new ArrayList<>();
        List<WebElement> imgs = _driver.findElements(By.cssSelector(cssSelector));
        imgs.forEach(el->imgUrls.add(el.getAttribute("src")));
        return imgUrls;
    }

    public void getRewards() {
        initialize();
        String threeWins = ".rewards-0 img";
        String fiveWins = ".rewards-1 img";
        String sevenWins = ".rewards-2 img";
        String flawless = ".rewards-3 img";
        //Extract Images for section

        List<WebElement> threeWinImages = _driver.findElements(By.cssSelector(threeWins));
        threeWinImages.forEach(el->System.out.println(el.getAttribute("src")));
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
