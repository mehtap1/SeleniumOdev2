import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import logs.LogsTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VehicleClassifiedNumberAssertion
{
    ChromeDriver driver;
    private static final Logger logger = LogManager.getLogger(VehicleClassifiedNumberAssertion.class);

    @BeforeEach()
    public void beforeEach() throws InterruptedException
    {
        System.setProperty("webdriver.chrome.driver", "/opt/chromedriver");
        driver = new ChromeDriver();

        logger.info("ChromeDriver oluşturuldu.");

        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(1, TimeUnit.MINUTES);
        driver.manage().window().maximize();
        driver.get("https://www.sahibinden.com/");
    }

    @Test
    public void testVehicleClassifiedNumberAssertion() throws InterruptedException
    {
        WebDriverWait wait = new WebDriverWait(driver, 30);

        Thread.sleep(3000);
        setCookieToTestbox();

        try
        {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("onetrust-accept-btn-handler"))).click();
            Thread.sleep(3500);
            //wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("close-button"))).click();
            driver.findElement(By.xpath("//a[@title='Otomobil']")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='all-classifieds-link']"))).click();
            List<WebElement> classifiedList = driver.findElements(By.id("searchResultsTable"));

            if (classifiedList.size() > 0)
            {
                logger.info("ilan listesi doludur");
            }
            else
                logger.info("ilan listesi boştur");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='classifiedTitle']")));
            WebElement classifiedTitle = driver.findElement(By.xpath("//a[@class='classifiedTitle']"));
            logger.info("İlan başlığı:" + classifiedTitle.getText());
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='searchResultsPriceValue']")));
            WebElement price = driver.findElement(By.xpath("//a[@class='searchResultsPriceValue']"));
            logger.info("İlan fiyatı:" + price);
            classifiedList.get(0).click();

            //ilan No karşılaştırma
            String URL = driver.getCurrentUrl();
            WebElement classifieldId = driver.findElement((By.id("classifiedId")));
            //Assertion
            Assertions.assertTrue(URL.contains((CharSequence) classifieldId));
            logger.info("ilan id'ler karşılaştırıldı...");

        }
        catch (Exception exception)
        {
            logger.info("Error message: Test fail");
            File imageFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            logger.info(imageFile.getPath());
        }
    }

    public void setCookieToTestbox()
    {
        List<Cookie> cookieList = new ArrayList<>();
        cookieList.add(new Cookie("testBox", "10", ".sahibinden.com", "/", null));
        cookieList.add(new Cookie("tbSite", "x", ".sahibinden.com", "/", null));
        cookieList.forEach(cookie -> driver.manage().addCookie(cookie));

        driver.navigate().refresh();
    }

    @AfterEach
    public void afterEach()
    {
        driver.quit();
        logger.info("ChromeDriver kapandı.");
    }
}
