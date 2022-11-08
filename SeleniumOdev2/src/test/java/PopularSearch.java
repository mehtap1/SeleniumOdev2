import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class PopularSearch
{
    ChromeDriver driver;
    private static final Logger logger = LogManager.getLogger(PopularSearch.class);

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

    @Tag("POPULAR_SEARCH")
    @ParameterizedTest
    @CsvSource({
            "iPhone 12 Mini,#search_cats ul li.cl4,1",
            "PlayStation 5,#search_cats ul li.cl3 div a h2,2",
            "Koşu Bandı,#search_cats ul li.cl3 div a h2,3",
            "Elektrikli Isıtıcı,#search_cats ul li.cl3 div a h2,4",
            "Toyota,#search_cats ul li.cl2 div a h2,5"})

    public void testPopularSearch() throws InterruptedException
    {
        WebDriverWait wait = new WebDriverWait(driver, 30);

        Thread.sleep(3000);
        setCookieToTestbox();
        driver.manage().addCookie(new Cookie("VISITOR_INFO1_LIVE", "true"));
        driver.navigate().refresh();
        try
        {

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@title='iphone 12 mini']"))).click();
            String categoryname1 = driver.findElement(By.xpath("//a[@title='iphone 12 min']")).getText();
            String categoryname2 = driver.findElement(By.xpath("//a[@title='iPhone 12 Mini']")).getText();
            Assertions.assertEquals(categoryname1, categoryname2);
            logger.info("Kategori adı :" + categoryname2);

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
        cookieList.add(new Cookie("testBox", "06", ".sahibinden.com", "/", null));
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
