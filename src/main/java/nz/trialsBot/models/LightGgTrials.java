package nz.trialsBot.models;

import nz.trialsBot.views.helpers.RewardsDrawer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class LightGgTrials extends BaseScreenshoter {
    private static final String TARGET_URL = "https://www.light.gg/";

    @Override
    public File getRewardsScreenshot() {
        initialize();
        return makeRewardsScreenshot();
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
        } catch (IOException | URISyntaxException e) {
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


    private void initialize(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("window-size=550,888");
        _driver = new ChromeDriver(options);
        _driver.get(TARGET_URL);
    }
}
