package com.example.app;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import lombok.Setter;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverManager {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            driver = createDriver();
        }
        return driver;
    }

    public static void setDriver(WebDriver webdriver) {
        driver = webdriver;
    }

    private static WebDriver createDriver() {
        return createDriver("chrome"); // Default ke Chrome
    }

    private static WebDriver createDriver(String browser) {
        String downloadDir = Paths.get("target").toAbsolutePath().toString();

        /** CHROME / EDGE Options */
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadDir);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("plugins.always_open_pdf_externally", true);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", prefs);

        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setExperimentalOption("prefs", prefs);

        /** FIREFOX Options */
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.download.dir", downloadDir);
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf");
        profile.setPreference("pdfjs.disabled", true);

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setProfile(profile);

        /** Setup WebDriverManager */
        switch (browser.toLowerCase()) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(firefoxOptions);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver(edgeOptions);
            }
            default -> { // Default ke Chrome
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(chromeOptions);
            }
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;

    }
}
