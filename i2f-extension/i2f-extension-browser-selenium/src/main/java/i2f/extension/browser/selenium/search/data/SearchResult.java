package i2f.extension.browser.selenium.search.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/6/5 20:34
 * @desc
 */
@Data
@NoArgsConstructor
public class SearchResult {
    protected String url;
    protected String title;
    protected String description;
    protected String text;
    protected String html;
}
