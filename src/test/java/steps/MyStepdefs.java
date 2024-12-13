package steps;

import core.BrowserFactory;
import core.ExtentManager;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.LandingPage;
import utilities.Constants;
import utilities.MediaStats;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static io.appium.java_client.internal.CapabilityHelpers.toDouble;
import static utilities.MediaStats.*;
import static utilities.ReusableLibrary.*;


public class MyStepdefs {
     WebDriver driver1 = BrowserFactory.getDriver(System.getProperty("BROWSER"));;
     WebDriver driver2;
    LandingPage landingPage1;
    LandingPage landingPage2;
    private String primaryMode=null;
    private String secondaryMode=null;
    private double maxConnectionTime = Double.parseDouble(BrowserFactory.getPropertyValue("MAX-CONNECTION-TIME"));
    ChromeOptions chromeOptions;
    static UiAutomator2Options androidOptions;
    private static XCUITestOptions iosOptions;
    String channelID = generateRandomChannelID();
    double sumOfConnectionTime=0.0;
    private static double maxConnectionTime1 = 0.0;

    @Given("I launch application")
    public void iLaunchApplication() throws InterruptedException {
        try {
            driver1.get(System.getProperty("CLUSTER"));
            //driver2.get(System.getProperty("CLUSTER"));
            Assert.assertEquals(Constants.LANDING_PAGE_TITLE, driver1.getTitle());

            // Log the success message
            ExtentManager.getTest().info("Application Launched in " + System.getProperty("BROWSER"));
            ExtentManager.getTest().pass("Landing page title verified successfully.");
        } catch (AssertionError e) {
            handleException(e, "validating video stats", driver1, driver2);
        } catch (Exception e) {
            handleException(e, "validating video stats", driver1, driver2);
        }
    }

    @And("I close the application")
    public void iCloseTheApplication() {
        BrowserFactory.cleanupDriver("chrome");
        BrowserFactory.cleanupDriver("firefox");
        BrowserFactory.cleanupDriver("android");
        BrowserFactory.cleanupDriver("safari");
        if(driver2!=null)
            driver2.quit();
    }

    @When("I join the video chat with the following details:")
    public void iJoinTheVideoChatWithTheFollowingDetails(DataTable dataTable) {
        try {
            String channel = generateRandomChannelID();

            for (Map<String, String> data : dataTable.asMaps(String.class, String.class)) {
                String userName = data.get("Name");
                String mode = data.get("Mode");
                String options = data.get("WebOptions");

                double connectionTime;

                if ("Primary User".equals(userName)) {
                    landingPage1 = new LandingPage(driver1);
                    connectionTime = joinVideoChat(landingPage1, driver1, userName, mode, channel,options);

                    // Assert connection was successful and within time limit
                    Assert.assertTrue(connectionTime > 0 && connectionTime <= maxConnectionTime, "Connection was unsuccessful, taken <b>"+connectionTime+" </b>seconds for connection");

                    //primaryMode = mode;
                    String formattedDuration = String.format("%.2f", connectionTime);

                    // Log success
                    ExtentManager.getTest().pass("<b>" + userName + "</b> successfully joined the meeting <b>" + channel + "</b> in <b>" + mode + "</b> mode in <b>" + formattedDuration + "</b> seconds.");
                } else if ("Secondary User".equals(userName)) {
                    System.out.println(" -------  2 user -------");

                    System.out.println(driver1.toString());
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments(new String[]{"--use-fake-ui-for-media-stream", "--use-fake-device-for-media-stream"});
                    chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    driver2 = new ChromeDriver(chromeOptions);
                    System.out.println(driver1.toString());
                    System.out.println(driver2.toString());
                    driver2.get(System.getProperty("CLUSTER"));
                    landingPage2 = new LandingPage(driver2);
                    connectionTime = joinVideoChat(landingPage2, driver2, userName, mode, channel,options);

                    // Assert connection was successful and within time limit
                    Assert.assertTrue(connectionTime > 0 && connectionTime <= maxConnectionTime, "Connection was unsuccessful, taken <b>"+connectionTime+" </b>seconds for connection");

                    //secondaryMode = mode;
                    String formattedDuration = String.format("%.2f", connectionTime);

                    // Log success
                    ExtentManager.getTest().pass("<b>" + userName + "</b> successfully joined the meeting <b>" + channel + "</b> in <b>" + mode + "</b> mode in <b>" + formattedDuration + "</b> seconds.");
                } else {
                    throw new IllegalArgumentException("Unsupported user name: " + userName);
                }
            }
        } catch (AssertionError e) {
            handleException(e, "connection failed", driver1, driver2);
        } catch (Exception e) {
            handleException(e, "connection failed", driver1, driver2);
        }
    }


    @And("I validate Chat Messages for {string}")
    public void iValidateChatMessagesFor(String user, DataTable dataTable) {
        try {
            String sendMessage = "Outgoing Message";
            String receiveMessage = "Incoming Message";
            List<List<String>> data = dataTable.asLists(String.class);

            for (List<String> step : data) {
                switch (step.get(0)) {
                    case "Send Message":
                        boolean sentSuccessfully = validateMessageSending(landingPage1, driver1, "Primary User", sendMessage);
                        Assert.assertTrue(sentSuccessfully);
                        if (sentSuccessfully) {
                            ExtentManager.getTest().pass("Message sent successfully by Primary User.");
                        }
                        break;
                    case "Receive Message":
                        boolean receivedSuccessfully = validateMessageSending(landingPage2, driver2, "Secondary User", receiveMessage);
                        Assert.assertTrue(receivedSuccessfully);
                        if (receivedSuccessfully) {
                            ExtentManager.getTest().pass("Message received successfully by Secondary User.");
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown step: " + step.get(0));
                }
            }
        } catch (AssertionError e) {
            handleException(e, "validating chat messages", driver1, driver2);
        } catch (Exception e) {
            handleException(e, "validating chat messages", driver1, driver2);
        }
    }
    @Then("I {string} {string}")
    public void i(String action, String media) throws InterruptedException {
        try {
            if (media.equals("Video")) {
                landingPage1.rightClickOnVideo();
                landingPage1.setMuteUnMuteVideo();
                // Log the action as pass
                ExtentManager.getTest().pass("Successfully " + action.toLowerCase() + " the " + media.toLowerCase());

                //Thread.sleep(MAX_WAIT); // waiting to resolve promise for App Stats Object
            } else if (media.equals("Audio")) {
                landingPage1.rightClickOnVideo();
                landingPage1.setMuteUnMuteAudio();
                // Log the action as pass
                ExtentManager.getTest().pass("Successfully " + action.toLowerCase() + " the " + media.toLowerCase());

                //Thread.sleep(MAX_WAIT); // waiting to resolve promise for App Stats Object
            }
        } catch (AssertionError e) {
            handleException(e, "validating video stats", driver1, driver2);
        } catch (Exception e) {
            handleException(e, "validating video stats", driver1, driver2);
        }
    }
    @And("I validate Video stats as below")
    public void iValidateVideoStatsAsBelow(DataTable dataTable) {
        try {
            for (Map<String, String> data : dataTable.asMaps(String.class, String.class)) {
                // Parse expected values and operators
                double expectedBitrate = parseExpectedValue(data.get("Bitrate"));
                String bitrateOperator = parseOperator(data.get("Bitrate"));

                double expectedFPS = parseExpectedValue(data.get("FPS"));
                String fpsOperator = parseOperator(data.get("FPS"));

                double expectedHeight = parseExpectedValue(data.get("Height"));
                String heightOperator = parseOperator(data.get("Height"));

                double expectedWidth = parseExpectedValue(data.get("Width"));
                String widthOperator = parseOperator(data.get("Width"));

                // Extract actual values
                Map<String, Object> extractedValues = MediaStats.extractMediaStreamInfo(getCallConnectionStatsJson(driver1));
                ExtentManager.getTest().info(extractedValues.toString());

                // Get actual values with type-safe conversion
                double actualBitrate = toDouble(extractedValues.getOrDefault("outbound_bitrate", 0.0));
                double actualFPS = toDouble(extractedValues.getOrDefault("outbound_fps", 0.0));
                double actualHeight = toDouble(extractedValues.getOrDefault("outbound_height", 0.0));
                double actualWidth = toDouble(extractedValues.getOrDefault("outbound_width", 0.0));

                // Validate and log results
                validateAndLog("Bitrate", actualBitrate, expectedBitrate, bitrateOperator);
                validateAndLog("FPS", actualFPS, expectedFPS, fpsOperator);
                validateAndLog("Height", actualHeight, expectedHeight, heightOperator);
                validateAndLog("Width", actualWidth, expectedWidth, widthOperator);
            }
        } catch (AssertionError e) {
            handleException(e, "validating video stats", driver1, driver2);
        } catch (Exception e) {
            handleException(e, "validating video stats", driver1, driver2);
        }
    }

    @And("I validate screen sharing for {string}")
    public void iValidateScreenSharingFor(String user, DataTable dataTable) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver1, Duration.ofSeconds(15));
        List<List<String>> actions = dataTable.asLists(String.class);

        try {
            for (List<String> action : actions) {
                String expectedAction = action.get(0);
                landingPage1.screenShare();
                Thread.sleep(2000);

                int expectedVideoCount = expectedAction.equals("Start Screen Share") ? 3 : 2;
                //        wait.until(ExpectedConditions.numberOfElementsToBe(landingPage1.videoController, expectedVideoCount));
                //Assert.assertEquals(landingPage1.totalVideoCount(), expectedVideoCount,
                //        "Expected " + expectedVideoCount + " video elements after " + expectedAction.toLowerCase());

                // Log success message
                ExtentManager.getTest().pass("Screen Share " + (expectedVideoCount == 3 ? "started" : "stopped") + " successfully.");
            }
        } catch (AssertionError e) {
            handleException(e, "validating screen sharing", driver1, driver2);
        } catch (Exception e) {
            handleException(e, "validating screen sharing", driver1, driver2);
        }
    }

    @Then("Primary User initiated the meeting")
    public void primaryUserInitiatedTheMeeting() throws InterruptedException {

        landingPage1 = new LandingPage(driver1);
        landingPage1.setChannelId(channelID);
        landingPage1.clickSubmit();


    }

    @And("Secondary User Joined and dropped the call")
    public void secondaryUserJoinedTheCall() throws InterruptedException {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(new String[]{"--use-fake-ui-for-media-stream", "--use-fake-device-for-media-stream"});
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        driver2 = new ChromeDriver(chromeOptions);
        landingPage2 = new LandingPage(driver2);
        driver2.get(System.getProperty("CLUSTER"));
        landingPage2.setChannelId(channelID);
        landingPage2.clickSubmit();
        WebDriverWait wait = new WebDriverWait(driver2, Duration.ofSeconds(20));
        long startTime = System.currentTimeMillis();

        boolean isConnected = wait.until(ExpectedConditions.visibilityOfElementLocated(landingPage2.connectionSuccessfull)).isDisplayed();

        long endTime = System.currentTimeMillis();
        double timeTakenSeconds = (endTime - startTime) / 1000.0; // Convert to seconds
        sumOfConnectionTime += timeTakenSeconds;

        // Check if the current time is the highest and update maxConnectionTime
        if (timeTakenSeconds > maxConnectionTime1) {
            maxConnectionTime1 = timeTakenSeconds;
        }

        System.out.println("Time taken for Primary User to connect successfully: " + timeTakenSeconds + " seconds");
        System.out.println("Current maximum connection time: " + maxConnectionTime1 + " seconds");

        driver2.findElement(landingPage2.btnLeave).click();
        Thread.sleep(1000);
        driver2.quit();
    }

    @And("Print average connection time taken")
    public void printAverageConnectionTimeTaken() {
        System.out.println("************ Average Connection Time Taken ********** "+sumOfConnectionTime/5);
    }



}
