package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LandingPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor
    public LandingPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Locators
    public By hdrTitle = By.xpath("//div[@class='header-content']");
    public By nameInput = By.id("userNameInput");
    private By channelIdInput = By.id("channelInput");
    private By submitButton = By.id("joinButton");
    public By meetingVideo = By.xpath("//div[@id='localView']//video[@autoplay='autoplay']");
    public By videoController = By.xpath("//video[@autoplay='autoplay']");
    private By meetingMode = By.id("connectionModeInput");
    public By chatBox = By.id("eventLog");
    private By muteUnMuteVideo = By.xpath("//span[normalize-space()='Mute Video']");
    private By muteUnMuteAudio = By.xpath("//span[normalize-space()='Mute Audio']");
    private By chatMessageInput = By.id("sendInput");
    private By sendButton = By.id("sendButton");
    public By screenShareButton = By.xpath("//button[@id='shareScreenBtn']");
    private By forceTurnsTcpInput = By.xpath("//input[@id='forceTurnsTcpInput']");
    private By useWebSocketsForMedia = By.xpath("//input[@id='useWebSocketsForMedia']");
    private By optAudioOnly = By.xpath("//input[@id='audioOnlyInput']");
    private By optRecieveOnly = By.xpath("//input[@id='receiveOnlyInput']");
    private By optPublishOnly = By.xpath("//input[@id='publishOnlyInput']");

    private By optCaptureScreen = By.xpath("//input[@id='screenShareInput']");
    public By connectionSuccessfull = By.xpath("//p[contains(text(),'Data channel connection established')]");
    public By btnLeave = By.xpath("//button[@id='leaveButton']");
    // Methods to interact with the elements
    public void clickTitleHeader(){
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(hdrTitle));
        title.click();
    }
    public void setName(String name) {
        WebElement nameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        nameElement.clear();
        nameElement.sendKeys(name);
    }

    public void setChannelId(String channelId) {
        WebElement channelIdElement = wait.until(ExpectedConditions.visibilityOfElementLocated(channelIdInput));
        channelIdElement.clear();
        channelIdElement.sendKeys(channelId);
    }

    public void clickSubmit() {
        WebElement submitElement = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        scrollToElement(submitElement);
        submitElement.click();
    }

    public void selectMeetingMode(String mode) {
        WebElement modeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(meetingMode));
        modeElement.sendKeys(mode);
    }

    public boolean isVideoAvailable() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(meetingVideo)).isDisplayed();
    }

    public int totalVideoCount() {
        return driver.findElements(videoController).size();
    }

    public void rightClickOnVideo() {
        // Wait for the video element to be visible
        WebElement videoElement = wait.until(ExpectedConditions.visibilityOfElementLocated(meetingVideo));

        // Use JavascriptExecutor to right-click on the video element
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "var evt = new MouseEvent('contextmenu', {" +
                "bubbles: true," +
                "cancelable: true," +
                "view: window" +
                "});" +
                "arguments[0].dispatchEvent(evt);";
        js.executeScript(script, videoElement);
    }

    public String getChatBoxText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(chatBox)).getText();
    }

    public void setMuteUnMuteVideo() {
        WebElement muteVideoElement = wait.until(ExpectedConditions.elementToBeClickable(muteUnMuteVideo));
        muteVideoElement.click();
    }

    public void setMuteUnMuteAudio() {
        WebElement muteAudioElement = wait.until(ExpectedConditions.elementToBeClickable(muteUnMuteAudio));
        muteAudioElement.click();
    }

    public void sendMessage(String msg) throws InterruptedException {
        System.out.println("********************************");
        WebElement chatInput = wait.until(ExpectedConditions.visibilityOfElementLocated(chatMessageInput));
        chatInput.sendKeys(msg);
        WebElement sendElement = wait.until(ExpectedConditions.elementToBeClickable(sendButton));
        scrollToElement(sendElement);
        sendElement.sendKeys(Keys.ENTER);

    }

    public void screenShare() throws InterruptedException {
        //WebElement screenShareElement = wait.until(ExpectedConditions.elementToBeClickable(screenShareButton));
        //Thread.sleep(2000);
        //screenShareElement.sendKeys(Keys.ENTER);
        driver.findElement(screenShareButton).click();
        //WebElement screenShareElement = screenShareButton
        //screenShareElement.click();
    }

    public void setForceTurnsTcp(boolean enable) {
        WebElement forceTurnsElement = wait.until(ExpectedConditions.elementToBeClickable(forceTurnsTcpInput));
        if (forceTurnsElement.isSelected() != enable) {
            forceTurnsElement.click();
        }
    }

    public void setUseWebSocketsForMedia(boolean enable) {
        WebElement useWebSocketsElement = wait.until(ExpectedConditions.elementToBeClickable(useWebSocketsForMedia));
        if (useWebSocketsElement.isSelected() != enable) {
            useWebSocketsElement.click();
        }
    }

    public void setOptAudioOnly(boolean enable) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(optAudioOnly));
        if (element.isSelected() != enable) {
            element.click();
        }
    }
    public void setOptRecieveOnly(boolean enable) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(optRecieveOnly));
        if (element.isSelected() != enable) {
            element.click();
        }
    }
    public void setOptPublishOnly(boolean enable) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(optPublishOnly));
        if (element.isSelected() != enable) {
            element.click();
        }
    }
    public void setOptCaptureScreen(boolean enable) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(optCaptureScreen));
        if (element.isSelected() != enable) {
            element.click();
        }
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
