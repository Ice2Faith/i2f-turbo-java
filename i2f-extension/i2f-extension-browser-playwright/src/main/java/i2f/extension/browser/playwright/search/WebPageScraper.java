package i2f.extension.browser.playwright.search;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.impl.TargetClosedError;
import i2f.browser.std.search.data.SearchResult;
import i2f.extension.browser.playwright.BrowserPlaywright;
import i2f.extension.browser.playwright.context.PlaywrightDriver;
import i2f.extension.browser.playwright.search.utils.PlaywrightUtil;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/6/30 11:17
 * @desc
 */
public class WebPageScraper {
    public static SearchResult scraper(String url, boolean webUi) throws Exception {

        // 打开目标网页
        try (PlaywrightDriver driver = BrowserPlaywright.getWebDriver(webUi, null)) {
            PlaywrightUtil.blockNetworkResources(driver.getPage());
            driver.getPage().setDefaultTimeout(Duration.ofSeconds(30).toMillis());
            driver.getPage().setDefaultNavigationTimeout(Duration.ofSeconds(60).toMillis());

            driver.getPage().navigate(url);

            try {
                driver.getPage().waitForSelector("body", new Page.WaitForSelectorOptions()
                        .setTimeout(TimeUnit.SECONDS.toMillis(30))
                );
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof TargetClosedError) {
                    throw e;
                }
            }


            ElementHandle body = driver.getPage().querySelector("body");
            String text = body.innerText();
            if (text == null || text.trim().isEmpty()) {
                // 无文本，在等几秒
                driver.getPage().waitForTimeout(TimeUnit.SECONDS.toMillis(5));
            }

            body = driver.getPage().querySelector("body");
            text = body.innerText();

            PlaywrightUtil.removeNoContentElements(driver.getPage());
            SearchResult result = new SearchResult();
            result.setUrl(url);
            result.setDescription(null);

            result.setTitle(driver.getPage().title());
            result.setHtml(driver.getPage().content());
            if (body != null) {
                result.setText(body.innerText());
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
