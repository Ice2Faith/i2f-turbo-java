package i2f.extension.browser.playwright.search.utils;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.impl.TargetClosedError;
import i2f.browser.std.search.consts.SearchBlockConsts;
import i2f.browser.std.search.utils.SearchUtil;

/**
 * @author Ice2Faith
 * @date 2026/6/30 14:13
 * @desc
 */
public class PlaywrightUtil {

    public static void removeNoContentElements(Page page) {

        try {
            page.evaluate("document.querySelectorAll('style, script, link , svg ,canvas, noscript, input[type=\"hidden\"]').forEach(function (el) {\n" +
                    "    try {\n" +
                    "        el.remove();\n" +
                    "    } catch (e) {\n" +
                    "    }\n" +
                    "});");
        } catch (Throwable e) {
            e.printStackTrace();
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

            page.evaluate(cleanAttributesScript);
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

            page.evaluate(cleanCommentScript);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void blockNetworkResources(Page page) {
        // 拦截所有请求
        page.route("**/*", route -> {
            boolean shouldBlock = false;

            // 获取请求的资源类型（如 image, media, font 等）
            String resourceType = route.request().resourceType();
            if (resourceType != null) {
                resourceType = resourceType.toLowerCase();
            }
            if (SearchBlockConsts.BLOCK_RESOURCE_TYPE.contains(resourceType)) {
                shouldBlock = true;
            }

            if (!shouldBlock) {

                String requestUrl = route.request().url();
                String suffix = SearchUtil.resolveUrlSuffix(requestUrl);
                if (SearchBlockConsts.BLOCK_URL_SUFFIX.contains(suffix)
                ) {
                    shouldBlock = true;
                }
            }


            // 判断如果是图片、音视频或字体，直接中止请求
            if (shouldBlock) {
                route.abort(); // 中止请求，释放带宽
            } else {
                route.resume(); // 放行其他请求（如 HTML, JS, CSS, XHR 等）
            }
        });
    }

    public static boolean isCannotRecoveryException(Throwable e) {
        if (e instanceof TargetClosedError) {
            return true;
        }
        return false;
    }
}
