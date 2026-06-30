package i2f.extension.browser.selenium.search;

import i2f.extension.browser.selenium.BrowserSelenium;
import i2f.extension.browser.selenium.search.data.SearchResult;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * @author Ice2Faith
 * @date 2026/6/30 11:17
 * @desc
 */
public class WebPageScraper {
    public static SearchResult scraper(String url, String driverPath) {
        // 打开目标网页
        WebDriver driver = BrowserSelenium.getWebDriver(null, true, driverPath);
        try {
            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            try {
                wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("body"), 0));
            } catch (Exception e) {
                e.printStackTrace();
            }

            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

            WebElement body = driver.findElement(By.tagName("body"));
            String text = body.getText();
            if (text == null || text.trim().isEmpty()) {
                // 无文本，在等几秒
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            }

            body = driver.findElement(By.tagName("body"));
            text = body.getText();

            SeleniumUtil.removeNoContentElements(driver);
            SearchResult result = new SearchResult();
            result.setUrl(url);
            result.setDescription(null);

            result.setTitle(driver.getTitle());
            result.setHtml(driver.getPageSource());
            if (body != null) {
                result.setText(body.getText());
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            // 关闭浏览器
            driver.quit();
        }
    }
}
