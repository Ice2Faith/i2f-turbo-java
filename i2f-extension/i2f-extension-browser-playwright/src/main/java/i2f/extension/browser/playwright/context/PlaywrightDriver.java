package i2f.extension.browser.playwright.context;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2026/7/1 10:09
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class PlaywrightDriver implements Closeable {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @Override
    public void close() throws IOException {
        if (page != null) {
            page.close();
            page = null;
        }
        if (context != null) {
            context.close();
            context = null;
        }
        if (browser != null) {
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }
}
