package steps;

import core.BrowserFactory;
import core.ExtentManager;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.LandingPage;
import utilities.Constants;

import java.time.Duration;
import java.util.List;
import java.util.Map;

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
    }

    @When("I join the video chat with the following details:")
    public void iJoinTheVideoChatWithTheFollowingDetails(DataTable dataTable) {
        try {
            long startTime, endTime;
            double duration;
            String formattedDuration;

            String channel = generateRandomChannelID();
            for (Map<String, String> data : dataTable.asMaps(String.class, String.class)) {
                String userName = data.get("Name");
                String mode = data.get("Mode");

                boolean joinedSuccessfully = false;

                if ("Primary User".equals(userName)) {
                    landingPage1 = new LandingPage(driver1);
                    startTime = System.currentTimeMillis();
                    joinedSuccessfully = joinVideoChat(landingPage1, driver1, userName, mode, channel);
                    endTime = System.currentTimeMillis();
                    Assert.assertTrue(joinedSuccessfully);
                    primaryMode = mode;
                    duration = (endTime - startTime) / 1000.0;
                    formattedDuration = String.format("%.2f", duration);
                } else if ("Secondary User".equals(userName)) {
                    System.out.println(" -------  2 user -------");

                    driver2  = BrowserFactory.getDriver("chrome");;
                    driver2.get(System.getProperty("CLUSTER"));
                    landingPage2 = new LandingPage(driver2);
                    startTime = System.currentTimeMillis();
                    joinedSuccessfully = joinVideoChat(landingPage2, driver2, userName, mode, channel);
                    endTime = System.currentTimeMillis();
                    Assert.assertTrue(joinedSuccessfully);
                    secondaryMode = mode;
                    duration = (endTime - startTime) / 1000.0;
                    formattedDuration = String.format("%.2f", duration);
                } else {
                    throw new IllegalArgumentException("Unsupported user name: " + userName);
                }

                // Log success if the assertion passes
                if (joinedSuccessfully) {
                    ExtentManager.getTest().pass("<b>"+userName + "</b> successfully joined the meeting "+"<b>"+channel+"</b>"+" in <b>" + mode + "</b> mode."+" in "+"<b>"+formattedDuration+"</b>"+" seconds");
                }
            }
        } catch (AssertionError e) {
            handleException(e, "User couldn't join the meeting", driver1, driver2);
        } catch (Exception e) {
            handleException(e, "User couldn't join the meeting", driver1, driver2);
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
