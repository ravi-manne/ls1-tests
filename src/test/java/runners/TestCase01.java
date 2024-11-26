package runners;

import core.BrowserFactory;
import core.ExtentManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ThreadGuard;
import org.testng.annotations.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;


public class TestCase01 {
    public static WebDriver driver;
    public static AppiumDriverLocalService service;
    ChromeOptions chromeOptions;
    static UiAutomator2Options androidOptions;
    private static XCUITestOptions iosOptions;
    @Test
    public static void test01() throws InterruptedException, URISyntaxException, MalformedURLException {

//        System.out.println("-----"+System.getProperty("BROWSER"));
//        System.out.println(BrowserFactory.getAndroidDevice());
//        service = BrowserFactory.startAppiumService();
//        service.start();
//        driver = BrowserFactory.getDriver(System.getProperty("BROWSER"));
//        driver.get(System.getProperty("CLUSTER"));
//        Thread.sleep(2000);
//        BrowserFactory.cleanupDriver();
//        service.stop();
    }
}
