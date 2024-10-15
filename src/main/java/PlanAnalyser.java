import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PlanAnalyser {
    public static void main(String[] args) {
        // Setting up chrome driver through WebDriverManager.
        WebDriverManager.chromedriver().setup();

        // Creating an instance of the Chrome driver
        WebDriver kr_driver = new ChromeDriver();

        // Creating an instance of the WebDriverWait
        WebDriverWait kr_wait = new WebDriverWait(kr_driver, Duration.ofSeconds(10));
    }
}
