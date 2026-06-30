package i2f.tools.tools;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.extension.browser.selenium.search.BaiduKaifaSearch;
import i2f.extension.browser.selenium.search.BaiduSearch;
import i2f.extension.browser.selenium.search.BiYingSearch;
import i2f.extension.browser.selenium.search.WebPageScraper;
import i2f.extension.browser.selenium.search.data.SearchContext;
import i2f.extension.browser.selenium.search.data.SearchResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * @author Ice2Faith
 * @date 2026/6/30 10:37
 * @desc
 */
@ConditionalOnExpression("${ai.tools.selenium-web-search.enable:false}")
@Component
@Tools
public class SeleniumWebSearchTools {
    @Tool(
            tags = {
                    AiTags.READONLY_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.RATE_LIMITED_VALUE
            },
            description = "search content by web, suitable for retrieving technical development materials, implement by https://kaifa.baidu.com"
    )
    public SearchContext selenium_web_search_by_kaifa_baidu(
            @ToolParam(value = "content", description = "the content of search, for example \"what's update of springboot3?\"")
            String content) {
        SearchContext ret = BaiduKaifaSearch.search(content, 5, null);
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
    public SearchContext selenium_web_search_by_baidu(
            @ToolParam(value = "content", description = "the content of search, for example \"what's update of springboot3?\"")
            String content) {
        SearchContext ret = BaiduSearch.search(content, 5, null);
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
    public SearchContext selenium_web_search_by_biying(
            @ToolParam(value = "content", description = "the content of search, for example \"what's update of springboot3?\"")
            String content) {
        SearchContext ret = BiYingSearch.search(content, 5, null);
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
    public SearchResult selenium_scrape_web_page(
            @ToolParam(value = "url", description = "the url of scrape, for example \"https://spring.io/projects/spring-boot\"")
            String url) {
        SearchResult ret = WebPageScraper.scraper(url, null);
        return ret;
    }
}
