package i2f.extension.browser.selenium.search.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/6/5 20:33
 * @desc
 */
@Data
@NoArgsConstructor
public class SearchContext {
    protected String question;
    protected List<SearchResult> results;
}
