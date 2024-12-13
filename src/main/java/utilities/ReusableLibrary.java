package utilities;

import com.aventstack.extentreports.MediaEntityBuilder;
import core.ExtentManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.LandingPage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReusableLibrary {
    private static final ThreadLocal<Logger> threadLocalLogger = ThreadLocal.withInitial(() -> LogManager.getLogger(ReusableLibrary.class));

    public static Logger getLogger() {
        return threadLocalLogger.get();
    }
    public static final Properties properties = new Properties();
    private static WebDriverWait wait;

    public static String getBaseURL(){
        return properties.getProperty("BaseURL");
    }

    public static void switchToChildWindow(WebDriver driver) {
        String mainWindowHandle = driver.getWindowHandle();
        String newWindowHandle = null;
        // Get the set of all window handles
        Set<String> windowHandles = driver.getWindowHandles();
        System.out.println(driver.getWindowHandles().size());
        for (String handle : windowHandles) {
            if (!handle.equals(mainWindowHandle)) {
                newWindowHandle = handle;
                break;
            }
        }
        driver.switchTo().window(newWindowHandle);

    }
    public static void WaitForElement(WebDriver driver, WebElement selector) throws Exception {
        try{
            wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOf(selector));
            //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
        catch (TimeoutException e){
            throw new Exception("Element Not Found");
        }

    }
    public static void stepFailed(String msg){
        //quitDriver();
        System.out.println("Application Closed");
        Assert.fail();
    }
    public static void closeDriver(WebDriver driver){
        if(driver!=null)
            driver.quit();
    }
    public static Double extractValue(String inputString,String serachString) {
        Pattern pattern = Pattern.compile(serachString + "(\\d+\\.\\d+)");
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        } else {
            return null;
        }
    }
    public static void validateStat(String statName, String operator, double expectedValue, double actualValue) {
        String errorMessage = String.format("Expected %s to be %s %s but was %s", statName, operator, expectedValue, actualValue);
        switch (operator) {
            case ">":
                Assert.assertTrue(actualValue > expectedValue, errorMessage);
                break;
            case "=":
                Assert.assertEquals(actualValue, expectedValue, errorMessage);
                break;
            case "<":
                Assert.assertTrue(actualValue < expectedValue, errorMessage);
                break;
            default:
                throw new IllegalArgumentException("Unsupported condition: " + operator);
        }
    }
    public static boolean validateMessageSending(LandingPage landingPage, WebDriver driver, String user, String message) {
        try {
            landingPage.sendMessage(message);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            String expectedMessage = user + ": " + message;
            wait.until(ExpectedConditions.textToBePresentInElementLocated(landingPage.chatBox, expectedMessage));

            String actualMessage = landingPage.getChatBoxText();
            return actualMessage.contains(expectedMessage);
        } catch (Exception e) {
            return false;
        }
    }

    public static void quitDriver(WebDriver driver){
        if(driver!=null)
            driver.quit();
    }
    public static double joinVideoChat(LandingPage landingPage, WebDriver driver, String userName, String mode, String channel, String options) {
        double timeTakenSeconds = 0;
        try {
            landingPage.setName(userName);
            landingPage.setChannelId(channel);
            landingPage.selectMeetingMode(mode);

            if (!"N/A".equals(options)) {
                switch (options) {
                    case "TCP":
                        landingPage.setForceTurnsTcp(true);
                        break;
                    case "PublishOnly":
                        landingPage.setOptPublishOnly(true);
                        break;
                    case "ReceiveOnly":
                        landingPage.setOptRecieveOnly(true);
                        break;
                }
                Thread.sleep(2000);
            }

            landingPage.clickSubmit();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            long startTime = System.currentTimeMillis();

            if ("Secondary User".equals(userName)) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(landingPage.connectionSuccessfull)).isDisplayed();
            } else {
                wait.until(ExpectedConditions.textToBePresentInElementLocated(landingPage.chatBox, "User"));
            }

            long endTime = System.currentTimeMillis();
            timeTakenSeconds = (endTime - startTime) / 1000.0;
            System.out.println("Time taken for " + userName + " to connect successfully: " + timeTakenSeconds + " seconds");
            return timeTakenSeconds;
        } catch (TimeoutException e) {
            System.err.println("Operation timed out: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        return timeTakenSeconds;

    }







    public static boolean joinVideoChat(LandingPage landingPage, WebDriver driver, String userName, String mode, String channel, boolean enableTcp, boolean useWebSockets) {
        try {
            landingPage.setName(userName);
            landingPage.setChannelId(channel);
            landingPage.selectMeetingMode(mode);

            // Interact with the new locators
            landingPage.setForceTurnsTcp(enableTcp);
            landingPage.setUseWebSocketsForMedia(useWebSockets);
            landingPage.clickSubmit();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            return wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(landingPage.chatBox), "User"));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean joinVideoChat(LandingPage landingPage, WebDriver driver, String userName, String mode, String channel, List<String> options) {
        try {
            // Set user name, channel, and mode
            landingPage.setName(userName);
            landingPage.setChannelId(channel);
            landingPage.selectMeetingMode(mode);

            // Check the list of options and set them accordingly
            if (options.contains("AudioOnly")) {
                landingPage.setOptAudioOnly(true);
            }
            if (options.contains("ReceiveOnly")) {
                landingPage.setOptRecieveOnly(true);
            }
            if (options.contains("CaptureScreen")) {
                landingPage.setOptCaptureScreen(true);
            }

            // Wait for a moment before clicking submit

            landingPage.clickSubmit();
            Thread.sleep(2000);
            //System.out.println(driver.switchTo().alert().getText());

            // Wait until the chat box shows the user's presence
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            return wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(landingPage.chatBox), "User"));
        } catch (Exception e) {
            e.printStackTrace();  // Add proper logging here if necessary
            return false;
        }
    }

    public static String takeScreenshot(WebDriver driver, String screenshotName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String dest = "Automation Reports/Screenshots/" + screenshotName + ".png";
        try {
            Files.createDirectories(Paths.get(dest).getParent()); // Ensure the directory exists
            Files.copy(source.toPath(), Paths.get(dest));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;
    }
    public static String captureScreenShot(WebDriver driver) throws IOException {
        TakesScreenshot screen = (TakesScreenshot) driver;
        File src = screen.getScreenshotAs(OutputType.FILE);
        String dest =  "Automation Reports/Screenshots/" + "test01"+ ".jpg";
        File target = new File(dest);
        FileUtils.copyFile(src, target);
        return dest;
    }
    public static void handleException(AssertionError e, String context,WebDriver... drivers) {
        ExtentManager.getTest().fail("Exception occurred while " + context + ": " + e.getMessage());
        ExtentManager.getTest().fail(e);
        for (WebDriver driver : drivers) {
            if (driver != null) {
                // Capture screenshot
                String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                ExtentManager.getTest().addScreenCaptureFromBase64String(screenshotBase64, "Screenshot on Failure");
                driver.quit();
            }
        }

        ExtentManager.flush();
        throw new RuntimeException(e);
    }
    public static void handleException(Exception e, String context, WebDriver... drivers) {
        ExtentManager.getTest().fail("Exception occurred while " + context + ": " + e.getMessage());
        ExtentManager.getTest().fail(e);
        for (WebDriver driver : drivers) {
            if (driver != null) {
                // Capture screenshot
                String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                ExtentManager.getTest().fail("Screenshot:", MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
                driver.quit();
            }
        }

        ExtentManager.flush();
        throw new RuntimeException(e);
    }


    public static String generateRandomChannelID() {
        StringBuilder channelId = new StringBuilder(6);
        Random RANDOM = new Random();
        String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String DIGITS = "0123456789";
        for (int i = 0; i < 3; i++) {
            // Add a random uppercase letter
            channelId.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
            // Add a random digit
            channelId.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        }
        return channelId.toString();
    }

    public static void logAndValidateStats(String userName, String statName, String operator, double expectedValue, double actualValue,
                                           double expectedBitrate, double actualBitrate, double expectedFPS, double actualFPS, double expectedHeight, double actualHeight,String mode) {

        String successMessage = String.format("Validation passed: %s is %s %s as expected.", statName, operator, expectedValue);
        String errorMessage = String.format("Expected %s to be %s %s but was %s", statName, operator, expectedValue, actualValue);

        switch (operator) {
            case ">":
            case "<":
                //if (!(mode.equals("MCU") && statName.equals("bitrate")) && !statName.equals("FPS")) {
                if (operator.equals(">")) {
                    Assert.assertTrue(actualValue >= expectedValue, errorMessage);
                } else {
                    Assert.assertTrue(actualValue <= expectedValue, errorMessage);
                }
                //}
                ExtentManager.getTest().pass(successMessage);
                break;

            case "=":
                //if (!(mode.equals("MCU") && statName.equals("bitrate")) && !statName.equals("FPS")) {
                Assert.assertEquals(actualValue, expectedValue, errorMessage);
                //}
                ExtentManager.getTest().pass(successMessage);
                break;

            default:
                throw new IllegalArgumentException("Unsupported condition: " + operator);
        }

    }

    public static void addScreenshot(WebDriver driver,String stepName){
        String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        ExtentManager.getTest().info(stepName, MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());

    }
    public static void validateCaptureScreen(){

    }
}
