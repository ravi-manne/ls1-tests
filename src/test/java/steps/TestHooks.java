package steps;

import core.BrowserFactory;
import core.ExtentManager;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.testng.SkipException;

import static core.ExtentManager.extent;

public class TestHooks {
    public static AppiumDriverLocalService service;

    @Before
    public void setup(Scenario scenario) {
        // Skip scenarios explicitly mentioning "screenshare" in their names
//        if (scenario.getName().toLowerCase().contains("screen share")) {
//            System.out.println("Skipping scenario: " + scenario.getName());
//            throw new SkipException("Skipping scenario: " + scenario.getName());
//        }

        // Skip all scenarios if the browser is "android"
//        if ("android".equalsIgnoreCase(System.getProperty("BROWSER"))) {
//            System.out.println("Skipping scenario for Android browser: " + scenario.getName());
//            throw new SkipException("Skipping scenario for Android browser: " + scenario.getName());
//        }

        // Start Appium service for Android browser
        if ("android".equalsIgnoreCase(System.getProperty("BROWSER"))) {
            if (scenario.getName().toLowerCase().contains("screen share")) {
                System.out.println("Skipping scenario: " + scenario.getName());
                throw new SkipException("Skipping scenario: " + scenario.getName());
            }
            service = BrowserFactory.startAppiumService();
            service.start();
        }

        // Initialize Extent reporting
        ExtentManager.createTest(scenario.getName(), "");
    }

    @After
    public void teardown() {
        // Stop Appium service if it was started
        if (service != null) {
            service.stop();
        }

        // Flush Extent reports
        extent.flush();
    }
}
