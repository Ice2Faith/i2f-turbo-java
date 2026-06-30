package i2f.extension.browser.selenium.search.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * @author Ice2Faith
 * @date 2026/6/30 14:13
 * @desc
 */
public class SeleniumUtil {
    public static void removeNoContentElements(WebDriver driver) {
        if (driver instanceof JavascriptExecutor) {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            try {
                executor.executeScript("document.querySelectorAll('style, script, link , svg ,canvas, noscript').forEach(function(el) { el.remove(); });");
            } catch (Throwable e) {

            }

            try {
                String cleanAttributesScript = "document.querySelectorAll('*').forEach(function(el) { " +
                        "    el.removeAttribute('class'); " +
                        "    el.removeAttribute('style'); " +
                        "    el.removeAttribute('id'); " +
                        "    el.removeAttribute('onclick'); " +
                        "});";

                executor.executeScript(cleanAttributesScript);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }
}
