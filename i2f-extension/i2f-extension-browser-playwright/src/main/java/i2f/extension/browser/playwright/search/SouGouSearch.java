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
public class SouGouSearch {
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
                result.setUrl("https://sogou.com/web?ie=utf8&from=index-nologin&s_from=index&query=" + URLEncoder.encode(context.getQuestion(), "UTF-8"));
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
                driver.getPage().navigate("https://www.sogou.com/");

                try {
                    driver.getPage().waitForSelector(".search-box form", new Page.WaitForSelectorOptions()
                            .setTimeout(Duration.ofSeconds(60).toMillis())
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    if (PlaywrightUtil.isCannotRecoveryException(e)) {
                        throw e;
                    }
                }

                ElementHandle inputElem = driver.getPage().querySelector(".search-box form .sec-input");
                inputElem.click();
                inputElem.fill(question);

                driver.getPage().waitForTimeout(Duration.ofSeconds(RANDOM.nextInt(3) + 1).toMillis());
                ElementHandle enterElem = driver.getPage().querySelector(".search-box form .enter-input");
                enterElem.click();
            }

            int nopCount=0;
            while (nopCount<1000) {

                Map.Entry<SearchResult, SearchType> entry = urlQueue.pollFirst();
                if (maxFetchCount.get() <= 0) {
                    break;
                }
                if (entry == null) {
                    nopCount++;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {

                    }
                    continue;
                }

                nopCount=0;

                try {
                    if (SearchType.SEARCH_FIRST != entry.getValue()) {
                        driver.getPage().setDefaultNavigationTimeout(Duration.ofSeconds(30).toMillis());
                    } else {
                        driver.getPage().setDefaultNavigationTimeout(Duration.ofSeconds(60).toMillis());
                    }
                    try {
                        if (!Objects.equals(driver.getPage().url(), entry.getKey().getUrl())) {
                            driver.getPage().navigate(entry.getKey().getUrl());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (PlaywrightUtil.isCannotRecoveryException(e)) {
                            break;
                        }
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
                            if (PlaywrightUtil.isCannotRecoveryException(e)) {
                                break;
                            }
                        }
                    }

                    if (SearchType.SEARCH_FIRST == entry.getValue()) {
                        for (int i = 0; i < 3; i++) {
                            try {
                                driver.getPage().waitForSelector(".results .vrwrap[exposed=\"1\"] .vr-title a", new Page.WaitForSelectorOptions()
                                        .setTimeout(Duration.ofSeconds(60).toMillis())
                                );
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                if(i==2){
                                    return context;
                                }
                                continue;
                            }
                        }
                    }

                    // 百度搜索首页
                    if (SearchType.SEARCH_FIRST == entry.getValue()) {
                        // 最大翻页
                        int maxPage = 10;

                        long lastHeight = (long) driver.getPage().evaluate("return document.body.scrollHeight");
                        for (int i = 0; i < maxPage; i++) {
                            // 3. 执行 JS 滚动到页面最底部
                            driver.getPage().evaluate("window.scrollTo(0, document.body.scrollHeight);");

                            // 4. 等待新内容加载 (根据网络情况和页面渲染速度调整时间)
                            Thread.sleep(2000);

                            // 5. 获取滚动后的新页面高度
                            long newHeight = (long) driver.getPage().evaluate("return document.body.scrollHeight");

                            // 6. 判断是否加载了新内容
                            if (newHeight == lastHeight) {
                                // 如果高度没有变化，说明已经到底部或没有更多数据，退出循环
                                break;
                            }

                            // 更新高度，继续下一次滚动
                            lastHeight = newHeight;

                        }


                    }

                    // 百度搜索页面
                    if (Arrays.asList(SearchType.SEARCH_FIRST,
                            SearchType.SEARCH_PAGE).contains(entry.getValue())) {


                        // 普通条目聚合
                        List<ElementHandle> wwwElems = driver.getPage().querySelectorAll(".results .vrwrap[exposed=\"1\"]");
                        for (ElementHandle item : wwwElems) {
                            String text = item.innerText();
//                            System.out.println("www-response:\n" + text);
                            List<ElementHandle> titleElems = item.querySelectorAll(".vr-title");
                            if (titleElems == null || titleElems.isEmpty()) {
                                continue;
                            }
                            ElementHandle titleElem = titleElems.get(0);
                            if (titleElem == null) {
                                continue;
                            }
                            String title = titleElem.innerText();
//                        System.out.println("www-href:\n" + href);
                            List<ElementHandle> aElems = item.querySelectorAll(".vr-title a");
                            if (aElems == null || aElems.isEmpty()) {
                                continue;
                            }
                            ElementHandle aElem = aElems.get(0);
                            if (aElem != null) {
                                String href = aElem.getAttribute("href");
                                System.out.println("www-href:\n" + href);
                                if (context != null) {
                                    String itemText = item.innerText();
                                    if (itemText == null || itemText.isEmpty()) {
                                        continue;
                                    }
                                    SearchResult result = new SearchResult();
                                    result.setUrl(href);
                                    result.setTitle(title);
                                    result.setDescription(itemText);
                                    maxFetchCount.decrementAndGet();
                                    context.getResults().add(result);
                                    if (maxFetchCount.get() <= 0) {
                                        return context;
                                    }
                                }
                            }

                        }


                    }

                    if (SearchType.SEARCH_FIRST == entry.getValue()) {

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

                    if (maxFetchCount.get() <= 0) {
                        return context;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (PlaywrightUtil.isCannotRecoveryException(e)) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return context;
    }


}
