package runners;

import core.ExtentManager;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.FileInputStream;
import java.io.IOException;

@CucumberOptions(
        features = "src/test/resources/features", // Path to the feature files
        glue = "steps",      // Package containing step definitions
        tags = "@SmokeTest",                      // Tags to filter scenarios
        plugin = {
                "pretty",                         // Output the Cucumber test output in the console
                "html:target/cucumber-reports/cucumber.html", // HTML report
                "json:target/cucumber-reports/cucumber.json", // JSON report
                "junit:target/cucumber-reports/cucumber.xml"  // JUnit XML report
        }
)
public class TestRunner extends AbstractTestNGCucumberTests {
    // AbstractTestNGCucumberTests handles running the Cucumber tests with TestNG.

    @BeforeClass
    public void beforeClass(@Optional String pt) {
        ExtentManager.extent = ExtentManager.getInstance(System.getProperty("BROWSER"), System.getProperty("CLUSTER"),"");
    }
}
