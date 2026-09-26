package i2f.browser.std.search.data;

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
