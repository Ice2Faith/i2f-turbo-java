package i2f.browser.std.search.utils;

import java.net.URL;

/**
 * @author Ice2Faith
 * @date 2026/7/1 10:06
 * @desc
 */
public class SearchUtil {
    public static String resolveUrlSuffix(String requestUrl) {
        String suffix = "";

        try {
            URL url = new URL(requestUrl);
            String path = url.getPath();
            int idx = path.lastIndexOf("#");
            if (idx >= 0) {
                path = path.substring(0, idx);
            }
            idx = path.lastIndexOf("?");
            if (idx >= 0) {
                path = path.substring(0, idx);
            }
            String name = path;
            idx = path.lastIndexOf("/");
            if (idx >= 0) {
                name = path.substring(idx + 1);
            }
            idx = name.lastIndexOf(".");
            if (idx >= 0) {
                suffix = name.substring(idx);
            }
            suffix = suffix.toLowerCase();
        } catch (Exception e) {
            // ignore
        }
        return suffix;
    }
}
