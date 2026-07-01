package i2f.extension.browser.playwright.search;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import i2f.browser.std.search.data.SearchContext;
import i2f.browser.std.search.data.SearchResult;
import i2f.browser.std.search.enums.SearchType;
import i2f.extension.browser.playwright.BrowserPlaywright;
import i2f.extension.browser.playwright.context.PlaywrightDriver;
import i2f.extension.browser.playwright.search.utils.PlaywrightUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2025/6/5 20:36
 * @desc
 */
public class TouTiaoSearch {
    public static final SecureRandom RANDOM = new SecureRandom();

    public static SearchContext search(String question) {
        return search(question, 5, false);
    }

    public static SearchContext search(String question, int maxArticleCount) {
        return search(question, maxArticleCount, false);
    }

    public static SearchContext search(String question, int maxArticleCount, boolean webUi) {
        if (maxArticleCount <= 0) {
            maxArticleCount = 5;
        }

        LinkedBlockingDeque<Map.Entry<SearchResult, SearchType>> urlQueue = new LinkedBlockingDeque<>();
        SearchContext context = new SearchContext();
        context.setQuestion(question);
        context.setResults(new ArrayList<>());


        if (context != null) {
            SearchResult result = new SearchResult();
            try {
                result.setUrl("https://so.toutiao.com/search?dvpf=pc&source=input&keyword=" + URLEncoder.encode(context.getQuestion(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {

            }
            urlQueue.addLast(new AbstractMap.SimpleEntry<>(result, SearchType.SEARCH_FIRST));
        }

        AtomicInteger maxFetchCount = new AtomicInteger(maxArticleCount);
        // 打开目标网页
        try (PlaywrightDriver driver = BrowserPlaywright.getWebDriver(webUi, null)) {
            PlaywrightUtil.blockNetworkResources(driver.getPage());
            driver.getPage().setDefaultTimeout(Duration.ofSeconds(30).toMillis());
            if (true) {
                driver.getPage().navigate("https://www.toutiao.com/");

                try {
                    driver.getPage().waitForSelector(".search-container .search", new Page.WaitForSelectorOptions()
                            .setTimeout(Duration.ofSeconds(60).toMillis())
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ElementHandle inputElem = driver.getPage().querySelector(".search-container .search input");
                inputElem.click();
                inputElem.press(question);

                driver.getPage().waitForTimeout(Duration.ofSeconds(RANDOM.nextInt(3) + 1).toMillis());
                ElementHandle enterElem = driver.getPage().querySelector(".search-container .search button");
                enterElem.click();
            }

            while (true) {

                Map.Entry<SearchResult, SearchType> entry = urlQueue.pollFirst();
                if (maxFetchCount.get() <= 0) {
                    break;
                }
                if (entry == null) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {

                    }
                    continue;
                }

                try {
                    if (SearchType.SEARCH_FIRST != entry.getValue()) {
                        driver.getPage().setDefaultNavigationTimeout(Duration.ofSeconds(15).toMillis());
                    } else {
                        driver.getPage().setDefaultNavigationTimeout(Duration.ofSeconds(60).toMillis());
                    }
                    try {
                        if (!Objects.equals(driver.getPage().url(), entry.getKey().getUrl())) {
                            driver.getPage().navigate(entry.getKey().getUrl());
                        }
                    } catch (Exception e) {
                        continue;
                    }

                    driver.getPage().waitForTimeout(Duration.ofSeconds(RANDOM.nextInt(5) + 1).toMillis());
                    if (true) {
                        try {
                            driver.getPage().waitForSelector("body", new Page.WaitForSelectorOptions()
                                    .setTimeout(Duration.ofSeconds(60).toMillis())
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (SearchType.SEARCH_FIRST == entry.getValue()) {
                        try {
                            driver.getPage().waitForSelector(".s-result-list .result-content .cs-card-content", new Page.WaitForSelectorOptions()
                                    .setTimeout(Duration.ofSeconds(60).toMillis())
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    // 百度搜索页面
                    if (Arrays.asList(SearchType.SEARCH_FIRST,
                            SearchType.SEARCH_PAGE).contains(entry.getValue())) {


                        // 普通条目聚合
                        List<ElementHandle> wwwElems = driver.getPage().querySelectorAll(".s-result-list .result-content .cs-card-content");
                        for (ElementHandle item : wwwElems) {
                            String text = item.innerText();
                            System.out.println("www-response:\n" + text);
                            ElementHandle headerElem = item.querySelector(".cs-header");
                            if (headerElem == null) {
                                continue;
                            }
                            String title = headerElem.innerText();
//                        System.out.println("www-href:\n" + href);
                            List<ElementHandle> aElems = item.querySelectorAll(".cs-header a");
                            if (aElems == null || aElems.isEmpty()) {
                                continue;
                            }
                            ElementHandle aElem = aElems.get(0);
                            if (aElem != null) {
                                String href = aElem.getAttribute("href");
                                System.out.println("www-href:\n" + href);
                                if (context != null) {
                                    SearchResult result = new SearchResult();
                                    result.setUrl(href);
                                    result.setTitle(title);
                                    result.setDescription(item.innerText());
                                    urlQueue.addLast(new AbstractMap.SimpleEntry<>(result, SearchType.ARTICLE));
                                }
                            }

                        }


                    }

                    // 百度搜索首页
                    if (SearchType.SEARCH_FIRST == entry.getValue()) {
                        // 最大翻页
                        int maxPage = 5;
                        List<ElementHandle> pageElems = driver.getPage().querySelectorAll(".s-result-list .result-content .cs-pagination a");
                        for (int i = 0; i < pageElems.size(); i++) {
                            if (i == 0 || i == pageElems.size() - 1) {
                                continue;
                            }
                            ElementHandle page = pageElems.get(i);
                            String href = page.getAttribute("href");

                            if (context != null) {
                                SearchResult result = new SearchResult();
                                result.setUrl(href);
                                urlQueue.addLast(new AbstractMap.SimpleEntry<>(result, SearchType.SEARCH_PAGE));
                            }

                            maxPage--;
                            if (maxPage <= 0) {
                                break;
                            }
                        }


                        if (context != null) {
                            PlaywrightUtil.removeNoContentElements(driver.getPage());
                            SearchResult result = entry.getKey();
                            result.setTitle(driver.getPage().title());
                            result.setHtml(driver.getPage().content());
                            ElementHandle body = driver.getPage().querySelector("body");
                            if (body != null) {
                                result.setText(body.innerText());
                            }
                        }

                    }

                    // 跳转的具体条目
                    if (SearchType.ARTICLE == entry.getValue()) {
                        ElementHandle body = driver.getPage().querySelector("body");
                        String text = body.innerText();
                        if (text == null || text.trim().isEmpty()) {
                            driver.getPage().waitForTimeout(Duration.ofSeconds(5).toMillis());
                        }
                        body = driver.getPage().querySelector("body");
                        text = body.innerText();
                        System.out.println("www-article:\n" + text);

                        if (context != null) {
                            PlaywrightUtil.removeNoContentElements(driver.getPage());
                            SearchResult result = entry.getKey();
                            result.setTitle(driver.getPage().title());
                            result.setHtml(driver.getPage().content());
                            if (body != null) {
                                result.setText(body.innerText());
                            }
                            context.getResults().add(result);
                        }
                        maxFetchCount.decrementAndGet();
                    }


                    if (maxFetchCount.get() <= 0) {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return context;
    }


}
