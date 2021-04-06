package nz.trialsBot.views;

import nz.trialsBot.interfaces.IMessageView;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

public class PsnView implements IMessageView {
    private static final String LOGIN_URL = "https://my.playstation.com/profile/FireteamBot/friends";
    protected WebDriver _driver;
    protected WebDriverWait _wait;

    //PSN WEB ELEMENTS, CSS SELECTIONS
    private static final String START_LOGIN_BTN = "#sb-social-toolbar-root > div > div > button";
    private static final String EMAIL_FIELD = "input[title=\"Sign-In ID (Email Address)\"]";
    private static final String NEXT_BTN = "button.primary-button";
    private static final String PASSWORD_FIELD = "input[title=\"Password\"]";
    private static final String LOGIN_BTN = "button.primary-button";
    private static final String MESSAGES_BTN = "#sb-messages-dropdown-toggle";
    private static final String CHATS = ".sb-list>li";
    private static final String MESSAGE_INPUT = "textarea";
    private static final String FILE_INPUT_DROP = ".sb-drop-attachment__drop-zone";
    private static final String SEND_BUTTON  = "button[title=\"Send Message\"]";


    public PsnView(String email, String password) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--disable-blink-features=AutomationControlled",
                "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "+
                "Chrome/87.0.4280.141 Safari/537.36",
                "user-data-dir=D:\\Users\\Sam\\Desktop\\tmp\\D2Bot");
        _driver = new ChromeDriver(options);
        _wait = new WebDriverWait(_driver, 2);
        login(email, password);
    }

    @Override
    public void sendMessage(int chatIndex, String text) {
        selectChat(chatIndex);
        findAndClick(MESSAGE_INPUT).sendKeys(text);
        findAndClick(SEND_BUTTON);
        System.out.println("PSN-MESSAGE: "+text);
    }

    @Override
    public void sendMessageAll(String text) {
        int chats = countChats();
        for(int chatIndex = 0; chatIndex<chats; chatIndex++){
            sendMessage(chatIndex,text);
        }
    }

    @Override
    public void sendMessage(int chatIndex, String text, File image) {
        selectChat(chatIndex);
        findAndClick(MESSAGE_INPUT).sendKeys(text);
        WebElement dropArea = _driver.findElement(By.cssSelector(FILE_INPUT_DROP));
        DropFile(image, dropArea);
        findAndClick(SEND_BUTTON);
        System.out.println("PSN-MESSAGE: "+text+image.getName());
    }

    @Override
    public void sendMessageAll(String text, File image) {
        int chats = countChats();
        for(int chatIndex = 0; chatIndex<chats; chatIndex++){
            sendMessage(chatIndex,text,image);
        }
    }

    @Override
    public void close() {
        _driver.quit();
    }

    public int countChats(){
        return _driver.findElements(By.cssSelector(CHATS)).size();
    }

    private void selectChat(int chatIndex) {
        _driver.findElements(By.cssSelector(CHATS)).get(chatIndex).click();
    }

    private void login(String email, String password) {
        _driver.get(LOGIN_URL);
        try{
            _driver.findElements(By.cssSelector(START_LOGIN_BTN));
            findAndClick(START_LOGIN_BTN);
            findAndClick(EMAIL_FIELD).sendKeys(email);
            findAndClick(NEXT_BTN);
            findAndClick(PASSWORD_FIELD).sendKeys(password);
            findAndClick(LOGIN_BTN);
        }catch(Exception e){
            //e.printStackTrace();
        }
        findAndClick(MESSAGES_BTN);
    }

    private WebElement findAndClick(String cssSelector){
        _wait.until(d -> d.findElement(By.cssSelector(cssSelector)));
        WebElement el = _driver.findElement(By.cssSelector(cssSelector));
        el.click();
        return el;
    }

    private static void DropFile(File filePath, WebElement target) {
        if(!filePath.exists())
            throw new WebDriverException("File not found: " + filePath.toString());

        WebDriver driver = ((RemoteWebElement)target).getWrappedDriver();
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        WebDriverWait wait = new WebDriverWait(driver, 30);

        String JS_DROP_FILE =
                "var target = arguments[0]," +
                        "    offsetX = arguments[1]," +
                        "    offsetY = arguments[2]," +
                        "    document = target.ownerDocument || document," +
                        "    window = document.defaultView || window;" +
                        "" +
                        "var input = document.createElement('INPUT');" +
                        "input.type = 'file';" +
                        "input.style.display = 'none';" +
                        "input.onchange = function () {" +
                        "  var rect = target.getBoundingClientRect()," +
                        "      x = rect.left + (offsetX || (rect.width >> 1))," +
                        "      y = rect.top + (offsetY || (rect.height >> 1))," +
                        "      dataTransfer = { files: this.files };" +
                        "" +
                        "  ['dragenter', 'dragover', 'drop'].forEach(function (name) {" +
                        "    var evt = document.createEvent('MouseEvent');" +
                        "    evt.initMouseEvent(name, !0, !0, window, 0, 0, 0, x, y, !1, !1, !1, !1, 0, null);" +
                        "    evt.dataTransfer = dataTransfer;" +
                        "    target.dispatchEvent(evt);" +
                        "  });" +
                        "" +
                        "  setTimeout(function () { document.body.removeChild(input); }, 25);" +
                        "};" +
                        "document.body.appendChild(input);" +
                        "return input;";

        WebElement input =  (WebElement)jse.executeScript(JS_DROP_FILE, target, 0, 0);
        input.sendKeys(filePath.getAbsoluteFile().toString());
        wait.until(ExpectedConditions.stalenessOf(input));
    }
}
