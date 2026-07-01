package i2f.browser.std.search.consts;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Ice2Faith
 * @date 2026/7/1 10:05
 * @desc
 */
public interface SearchBlockConsts {
    CopyOnWriteArraySet<String> BLOCK_RESOURCE_TYPE = new CopyOnWriteArraySet<>(Arrays.asList("image", "media", "font"));
    CopyOnWriteArraySet<String> BLOCK_URL_SUFFIX = new CopyOnWriteArraySet<>(Arrays.asList(
            ".jpg", ".png", ".gif", ".jpeg", ".svg", ".webp", ".bmp",
            ".mp3", ".ogg", ".avi", ".wav",
            ".mp4", ".mkv", ".rmvb", ".flv",
            ".m3u8", ".ts", ".hls",
            ".ttf", ".woff", ".woff2"
    ));
}
