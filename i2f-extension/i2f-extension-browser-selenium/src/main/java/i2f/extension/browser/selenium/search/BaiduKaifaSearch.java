package i2f.extension.browser.selenium.search;

import i2f.extension.browser.selenium.BrowserSelenium;
import i2f.extension.browser.selenium.search.data.SearchContext;
import i2f.extension.browser.selenium.search.data.SearchResult;
import i2f.extension.browser.selenium.search.enums.SearchType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2025/6/5 20:36
 * @desc
 */
public class BaiduKaifaSearch {
    public static final SecureRandom RANDOM = new SecureRandom();

    public static SearchContext search(String question) {
        return search(question, 5, null);
    }

    public static SearchContext search(String question, String driverPath) {
        return search(question, 5, driverPath);
    }

    public static SearchContext search(String question, int maxArticleCount, String driverPath) {
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
                result.setUrl("https://kaifa.baidu.com/searchPage?wd=" + URLEncoder.encode(context.getQuestion(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {

            }
            urlQueue.addLast(new AbstractMap.SimpleEntry<>(result, SearchType.SEARCH_FIRST));
        }

        AtomicInteger maxFetchCount = new AtomicInteger(maxArticleCount);
        // 打开目标网页
        WebDriver driver = BrowserSelenium.getWebDriver(null, true, driverPath);
        if(true) {
            driver.get("https://kaifa.baidu.com/");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            try {
                wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#home-search-container .ant-input-search"), 0));
            } catch (Exception e) {
                e.printStackTrace();
            }

            WebElement inputElem = driver.findElement(By.cssSelector("#home-search-container .ant-input-search input"));
            inputElem.click();
            inputElem.sendKeys(question);

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(RANDOM.nextInt(3) + 1));
            WebElement enterElem = driver.findElement(By.cssSelector("#home-search-container .ant-input-search button"));
            enterElem.click();
        }
        try {

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


                if (SearchType.SEARCH_FIRST != entry.getValue()) {
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
                } else {
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
                }
                try {
                    if(!Objects.equals(driver.getCurrentUrl(),entry.getKey().getUrl())) {
                        driver.navigate().to(entry.getKey().getUrl());
                    }
                } catch (Exception e) {
                    continue;
                }

                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(RANDOM.nextInt(5) + 1));

                if (SearchType.SEARCH_FIRST == entry.getValue()) {
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
                    try {
                        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#content-left .ant-list-items .ant-list-item"), 1));
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
                // 百度搜索页面
                if (Arrays.asList(SearchType.SEARCH_FIRST,
                        SearchType.SEARCH_PAGE).contains(entry.getValue())) {


                    // 普通条目聚合
                    List<WebElement> wwwElems = driver.findElements(By.cssSelector("#content-left .ant-list-items .ant-list-item "));
                    for (WebElement item : wwwElems) {
                        String text = item.getText();
                        System.out.println("www-response:\n" + text);
                        String href = item.getAttribute("mu");
//                        System.out.println("www-href:\n" + href);
                        List<WebElement> aElems = item.findElements(By.cssSelector("span:first-child > a"));
                        if (aElems == null || aElems.isEmpty()) {
                            continue;
                        }
                        WebElement aElem = aElems.get(0);
                        if (aElem != null) {
                            href = aElem.getAttribute("href");
                            System.out.println("www-href:\n" + href);
                            if (context != null) {
                                SearchResult result = new SearchResult();
                                result.setUrl(href);
                                result.setDescription(item.getText());
                                urlQueue.addLast(new AbstractMap.SimpleEntry<>(result, SearchType.ARTICLE));
                            }
                        }

                    }


                }

                // 百度搜索首页
                if (SearchType.SEARCH_FIRST == entry.getValue()) {
                    // 最大翻页
                    int maxPage = 3;
                    List<WebElement> pageElems = driver.findElements(By.cssSelector("#kf-search-page-footer #pagination-pc .pagination .pagination-item"));
                    for (int i = 0; i < pageElems.size(); i++) {
                        if (i == 0) {
                            continue;
                        }
                        WebElement page = pageElems.get(i);
                        page.click();

//                        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(RANDOM.nextInt(5) + 8));
                        TimeUnit.SECONDS.sleep(RANDOM.nextInt(3) + 2);

                        if(true){
                            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
                            try {
                                wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#content-left .ant-list-items .ant-list-item"), 1));
                            } catch (Exception e) {
                                e.printStackTrace();
                                break;
                            }
                        }

                        // 普通条目聚合
                        List<WebElement> wwwElems = driver.findElements(By.cssSelector("#content-left .ant-list-items .ant-list-item "));
                        for (WebElement item : wwwElems) {
                            String text = item.getText();
                            System.out.println("www-response:\n" + text);
//                        System.out.println("www-href:\n" + href);
                            List<WebElement> aElems = item.findElements(By.cssSelector("span:first-child > a"));
                            if (aElems == null || aElems.isEmpty()) {
                                continue;
                            }
                            WebElement aElem = aElems.get(0);
                            if (aElem != null) {
                                String href = aElem.getAttribute("href");
                                System.out.println("www-href:\n" + href);
                                if (context != null) {
                                    SearchResult result = new SearchResult();
                                    result.setUrl(href);
                                    result.setDescription(item.getText());
                                    urlQueue.addLast(new AbstractMap.SimpleEntry<>(result, SearchType.ARTICLE));
                                }
                            }

                        }

                        maxPage--;
                        if (maxPage <= 0) {
                            break;
                        }
                    }

                    // 等待AI回答响应完毕
                    if (SearchType.SEARCH_FIRST == entry.getValue()) {
//                            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(20000));
//                    TimeUnit.SECONDS.sleep(20);
                    }

                    // 第一页有可能有AI回答
                    List<WebElement> aiElems = driver.findElements(By.cssSelector("div[tpl=\"ai_index\"] .dqa-markdown_5emil"));
                    List<WebElement> ai2Elems = driver.findElements(By.cssSelector("div[tpl=\"new_baikan_index\"]"));
                    aiElems.addAll(ai2Elems);
                    for (WebElement item : aiElems) {
                        String text = item.getText();
                        System.out.println("ai-response:\n" + text);
                        if (context != null) {
                            SearchResult result = new SearchResult();
                            result.setUrl(entry.getKey().getUrl());
                            result.setTitle(context.getQuestion());
                            result.setDescription("AI answer");
                            result.setText(text);
                            context.getResults().add(result);
                        }
                    }

                    if (context != null) {
                        SearchResult result = entry.getKey();
                        result.setTitle(driver.getTitle());
                        result.setHtml(driver.getPageSource());
                        WebElement body = driver.findElement(By.tagName("body"));
                        if (body != null) {
                            result.setText(body.getText());
                        }
                    }

                }

                // 跳转的具体条目
                if (SearchType.ARTICLE == entry.getValue()) {
                    WebElement body = driver.findElement(By.tagName("body"));
                    String text = body.getText();
                    System.out.println("www-article:\n" + text);

                    if (context != null) {
                        SearchResult result = entry.getKey();
                        result.setTitle(driver.getTitle());
                        result.setHtml(driver.getPageSource());
                        if (body != null) {
                            result.setText(body.getText());
                        }
                        context.getResults().add(result);
                    }
                    maxFetchCount.decrementAndGet();
                }


                if (maxFetchCount.get() <= 0) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭浏览器
            driver.quit();
        }

        return context;
    }


}
