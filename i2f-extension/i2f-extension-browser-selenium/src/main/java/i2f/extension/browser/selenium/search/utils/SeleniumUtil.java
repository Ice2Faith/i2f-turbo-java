package i2f.extension.browser.selenium.search.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chromium.HasCdp;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.remote.http.ConnectionFailedException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
                executor.executeScript("document.querySelectorAll('style, script, link , svg ,canvas, noscript, input[type=\"hidden\"]').forEach(function (el) {\n" +
                        "    try {\n" +
                        "        el.remove();\n" +
                        "    } catch (e) {\n" +
                        "    }\n" +
                        "});");
            } catch (Throwable e) {

            }

            try {
                String cleanAttributesScript = "document.querySelectorAll('*').forEach(function (el) {\n" +
                        "    try {\n" +
                        "        el.removeAttribute('class');\n" +
                        "        el.removeAttribute('style');\n" +
                        "        el.removeAttribute('id');\n" +
                        "        el.removeAttribute('onclick');\n" +
                        "    } catch (e) {\n" +
                        "    }\n" +
                        "});";

                executor.executeScript(cleanAttributesScript);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String cleanCommentScript = "(function() {\n" +
                        "    const walker = document.createTreeWalker(\n" +
                        "        document.documentElement,\n" +
                        "        NodeFilter.SHOW_COMMENT,\n" +
                        "        null,\n" +
                        "        false\n" +
                        "    );\n" +
                        "\n" +
                        "    const nodesToRemove = [];\n" +
                        "    while (walker.nextNode()) {\n" +
                        "        nodesToRemove.push(walker.currentNode);\n" +
                        "    }\n" +
                        "    \n" +
                        "    nodesToRemove.forEach(node => node.parentNode.removeChild(node));\n" +
                        "})();";

                executor.executeScript(cleanCommentScript);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void blockNetworkResources(WebDriver driver) {
        if (driver instanceof HasCdp) {
            HasCdp hasCdp = (HasCdp) driver;
            try {
                // 1. 启用 Network 域
                hasCdp.executeCdpCommand("Network.enable", new HashMap<>());

                // 2. 设置需要阻断的资源 URL 规则（支持 glob 通配符）
                Map<String, Object> params = new HashMap<>();
                params.put("urls", Arrays.asList(
                        "*.png", "*.jpg", "*.jpeg", "*.gif", "*.webp", "*.svg", // 图片
                        "*.mp4", "*.webm", "*.ogg", "*.flv",                             // 视频
                        "*.mp3", "*.wav", "*.aac",                              // 音频
                        "*.woff", "*.woff2", "*.ttf",                            // 字体（可选）
                        "*.m3u8", "*.hls", "*.ts"                  // 流媒体
                ));
                hasCdp.executeCdpCommand("Network.setBlockedURLs", params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isCannotRecoveryException(Throwable e) {
        if (e instanceof NoSuchSessionException
                || e instanceof NoSuchWindowException
                || e instanceof ConnectionFailedException
                || e instanceof UnreachableBrowserException) {
            return true;
        }
        return false;
    }
}
