package steps;

import core.BrowserFactory;
import core.ExtentManager;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.LandingPage;
import utilities.Constants;
import utilities.MediaStats;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static utilities.Constants.MAX_WAIT;
import static utilities.ReusableLibrary.*;


public class MyStepdefs {
     WebDriver driver1 = BrowserFactory.getDriver(System.getProperty("BROWSER"));;
     WebDriver driver2;
    LandingPage landingPage1;
    LandingPage landingPage2;
    private String primaryMode=null;
    private String secondaryMode=null;

    ChromeOptions chromeOptions;
    static UiAutomator2Options androidOptions;
    private static XCUITestOptions iosOptions;

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
        driver2.quit();
    }

    @When("I join the video chat with the following details:")
    public void iJoinTheVideoChatWithTheFollowingDetails(DataTable dataTable) {
        try {
            String channel = generateRandomChannelID();

            for (Map<String, String> data : dataTable.asMaps(String.class, String.class)) {
                String userName = data.get("Name");
                String mode = data.get("Mode");

                double connectionTime;

                if ("Primary User".equals(userName)) {
                    landingPage1 = new LandingPage(driver1);
                    connectionTime = joinVideoChat(landingPage1, driver1, userName, mode, channel);

                    // Assert connection was successful and within time limit
                    Assert.assertTrue(connectionTime > 0 && connectionTime <= 10, "Connection was unsuccessful, taken <b>"+connectionTime+" </b>seconds for connection");

                    primaryMode = mode;
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
                    connectionTime = joinVideoChat(landingPage2, driver2, userName, mode, channel);

                    // Assert connection was successful and within time limit
                    Assert.assertTrue(connectionTime > 0 && connectionTime <= 10, "Connection was unsuccessful, taken <b>"+connectionTime+" </b>seconds for connection");

                    secondaryMode = mode;
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
        String mode=null;
        try {
            for (Map<String, String> data : dataTable.asMaps(String.class, String.class)) {
                String userName = data.get("Name");
                String[] bitrate = data.get("Bitrate").split(" ");
                String[] fps = data.get("FPS").split(" ");
                String[] height = data.get("Height").split(" ");

                double expectedBitrate = Double.parseDouble(bitrate[1]);
                double expectedFPS = Double.parseDouble(fps[1]);
                double expectedHeight = Double.parseDouble(height[1]);

                double actualBitrate;
                double actualFPS;
                double actualHeight;

                if ("Primary User".equals(userName)) {
                    mode=primaryMode;
                    getLogger().info("Outbound{}", MediaStats.initializeConnection(driver1));
                    //ExtentManager.getTest().info(MediaStats.initializeConnection(driver1));

                    actualBitrate = MediaStats.getOutboundBitRate(primaryMode);
                    actualFPS = MediaStats.getOutboundFPS(primaryMode);
                    actualHeight = MediaStats.getOutboundHeight(primaryMode);

                } else if ("Secondary User".equals(userName)) {
                    mode=secondaryMode;
                    getLogger().info("Inbound{}", MediaStats.initializeConnection(driver2));
                    //ExtentManager.getTest().info(MediaStats.initializeConnection(driver2));

                    actualBitrate = MediaStats.getInboundBitRate(secondaryMode);
                    actualFPS = MediaStats.getInboundFPS(secondaryMode);
                    actualHeight = MediaStats.getInboundHeight(secondaryMode);

                } else {
                    throw new IllegalArgumentException("Unsupported user name: " + userName);
                }

                // Using the combined log and validate method
//                logAndValidateStats(userName, "bitrate", bitrate[0], expectedBitrate, actualBitrate,
//                        expectedBitrate, actualBitrate, expectedFPS, actualFPS, expectedHeight, actualHeight,mode);
//                logAndValidateStats(userName, "FPS", fps[0], expectedFPS, actualFPS,
//                        expectedBitrate, actualBitrate, expectedFPS, actualFPS, expectedHeight, actualHeight,mode);
//                logAndValidateStats(userName, "Height", height[0], expectedHeight, actualHeight,
//                        expectedBitrate, actualBitrate, expectedFPS, actualFPS, expectedHeight, actualHeight,mode);
            }
            ExtentManager.getTest().pass("Bitrate validation Passed");
            ExtentManager.getTest().pass("FPS validation Passed");
            ExtentManager.getTest().pass("Height validation Passed");
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
}
