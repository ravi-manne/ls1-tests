import core.BrowserFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Map;


public class PlayArea {
    static WebDriverWait wait1;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello");
        System.out.println("-----"+ BrowserFactory.getPropertyValue("MAX-CONNECTION-TIME"));

//        WebDriver driver1 = BrowserFactory.getDriver("chrome");
//        WebDriver driver2 = BrowserFactory.getDriver("firefox");
//        driver2.get("https://uberchat-rc-apsouth1.liveswitch.io/");
//        driver1.get("https://uberchat-rc-apsouth1.liveswitch.io/");
//        Thread.sleep(2000);
//


//        //driver.findElement(By.xpath("//input[@id='audioOnlyInput']")).click();
//        driver.findElement(By.xpath("//button[@id='joinButton']")).click();
//
//        Thread.sleep(2000);
//
//        // Locate the video element
//        WebElement videoElement = driver.findElement(By.xpath("//div[@id='localView']//video[@autoplay='autoplay']"));
//
//        // Create an instance of Actions class
//        Actions actions = new Actions(driver);
//
//        // Perform right-click on the video element
//        actions.contextClick(videoElement).perform();
//
//        Thread.sleep(2000);
//
//
//        driver.findElement(By.xpath("//span[normalize-space()='Mute Video']")).click();
//        Thread.sleep(2000);
//        // Execute JavaScript to retrieve WebRTC stats
//        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//        String script =
//                "return navigator.mediaDevices.getUserMedia({audio: true, video: true})"
//                        + ".then(stream => {"
//                        + "  const videoTracks = stream.getVideoTracks();"
//                        + "  const audioTracks = stream.getAudioTracks();"
//                        + "  return {"
//                        + "    videoActive: videoTracks.length > 0 && videoTracks[0].readyState === 'live',"
//                        + "    audioActive: audioTracks.length > 0 && audioTracks[0].readyState === 'live'"
//                        + "  };"
//                        + "});";
//
//        Map<String, Object> mediaStats = (Map<String, Object>) jsExecutor.executeScript(script);
//
//        // Check the status of audio and video
//        boolean videoActive = (Boolean) mediaStats.get("videoActive");
//        boolean audioActive = (Boolean) mediaStats.get("audioActive");
//
//        System.out.println("Video active: " + videoActive);
//        System.out.println("Audio active: " + audioActive);


//        System.out.println(driver1.toString());
//        System.out.println(driver2.toString());
//        driver1.quit();
//        driver2.quit();
    }
}
