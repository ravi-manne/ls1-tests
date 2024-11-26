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
    public void setup(Scenario scenario){
        if (scenario.getName().toLowerCase().contains("screenshare")) {
            System.out.println("Skipping scenario: " + scenario.getName());
            throw new SkipException("Skipping scenario: " + scenario.getName());
        }
        if(System.getProperty("BROWSER").equals("android")){
            service = BrowserFactory.startAppiumService();
            service.start();
        }
        ExtentManager.createTest(scenario.getName(),"");
    }

    @After
    public void teardown(){
        if(service!=null)
            service.stop();
        extent.flush();

    }

}
