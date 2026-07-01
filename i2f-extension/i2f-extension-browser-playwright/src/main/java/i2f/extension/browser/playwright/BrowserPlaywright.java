package i2f.extension.browser.playwright;

import com.microsoft.playwright.*;
import i2f.extension.browser.playwright.context.PlaywrightDriver;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/7/1 9:33
 * @desc
 */
public class BrowserPlaywright {
    public static PlaywrightDriver getWebDriver(boolean withUi, BrowserType.LaunchOptions launchOptions) {
        PlaywrightDriver ret = new PlaywrightDriver();

        Playwright playwright = Playwright.create();
        ret.setPlaywright(playwright);

        if (launchOptions == null) {
            launchOptions = new BrowserType.LaunchOptions();
        }

        Browser browser = playwright.chromium().launch(
                launchOptions.setHeadless(!withUi)
        );
        ret.setBrowser(browser);

        BrowserContext context = browser.newContext();
        ret.setContext(context);

        Page page = context.newPage();
        page.setDefaultNavigationTimeout(TimeUnit.SECONDS.toMillis(60));
        page.setDefaultTimeout(TimeUnit.SECONDS.toMillis(30));
        ret.setPage(page);

        return ret;
    }
}
