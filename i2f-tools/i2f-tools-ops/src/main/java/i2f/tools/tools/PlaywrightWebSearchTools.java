package i2f.tools.tools;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.browser.std.search.data.SearchContext;
import i2f.browser.std.search.data.SearchResult;
import i2f.extension.browser.playwright.search.BaiduKaifaSearch;
import i2f.extension.browser.playwright.search.BaiduSearch;
import i2f.extension.browser.playwright.search.BiYingSearch;
import i2f.extension.browser.playwright.search.WebPageScraper;
import i2f.os.OsUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * @author Ice2Faith
 * @date 2026/6/30 10:37
 * @desc
 */
@ConditionalOnExpression("${ai.tools.playwright-web-search.enable:false}")
@Conditional(PlaywrightWebSearchTools.WindowsPlaywrightCondition.class)
@Data
@NoArgsConstructor
@Component
@Tools
public class PlaywrightWebSearchTools {

    public static class WindowsPlaywrightCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            if (!OsUtil.isWindows()) {
                return false;
            }
            try {
                return ClassUtils.isPresent("com.microsoft.playwright.Playwright", context.getClassLoader());
            } catch (Throwable e) {

            }
            return false;
        }
    }

    @Value("${ai.tools.playwright-web-search.web-ui:true}")
    protected boolean webUi = true;

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.RATE_LIMITED_VALUE
            },
            description = "search content by web, suitable for retrieving technical development materials, implement by https://kaifa.baidu.com"
    )
    public SearchContext playwright_web_search_by_kaifa_baidu(
            @ToolParam(value = "content", description = "the content of search, for example \"what's update of springboot3?\"")
            String content) {
        SearchContext ret = BaiduKaifaSearch.search(content, 5, webUi);
        return ret;
    }

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.RATE_LIMITED_VALUE
            },
            description = "search content by web, suitable for retrieving public web materials, implement by https://www.baidu.com"
    )
    public SearchContext playwright_web_search_by_baidu(
            @ToolParam(value = "content", description = "the content of search, for example \"what's update of springboot3?\"")
            String content) {
        SearchContext ret = BaiduSearch.search(content, 5, webUi);
        return ret;
    }

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.RATE_LIMITED_VALUE
            },
            description = "search content by web, suitable for retrieving public web materials, especially English or international content, implement by https://www.biying.com"
    )
    public SearchContext playwright_web_search_by_biying(
            @ToolParam(value = "content", description = "the content of search, for example \"what's update of springboot3?\"")
            String content) {
        SearchContext ret = BiYingSearch.search(content, 5, webUi);
        return ret;
    }

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.RATE_LIMITED_VALUE
            },
            description = "scrape the web page content from a specific URL"
    )
    public SearchResult playwright_scrape_web_page(
            @ToolParam(value = "url", description = "the url of scrape, for example \"https://spring.io/projects/spring-boot\"")
            String url) throws Exception {
        SearchResult ret = WebPageScraper.scraper(url, webUi);
        return ret;
    }
}
