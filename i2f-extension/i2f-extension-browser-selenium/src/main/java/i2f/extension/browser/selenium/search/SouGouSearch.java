package i2f.extension.browser.selenium.search;

import i2f.browser.std.search.data.SearchContext;
import i2f.browser.std.search.data.SearchResult;
import i2f.browser.std.search.enums.SearchType;
import i2f.extension.browser.selenium.BrowserSelenium;
import i2f.extension.browser.selenium.search.utils.SeleniumUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2025/6/5 20:36
 * @desc
 */
public class SouGouSearch {
    public static final SecureRandom RANDOM = new SecureRandom();

    public static SearchContext search(String question) {
        return search(question, 5, false, null);
    }

    public static SearchContext search(String question, String driverPath) {
        return search(question, 5, false, driverPath);
    }

    public static SearchContext search(String question, int maxArticleCount, String driverPath) {
        return search(question, maxArticleCount, false, driverPath);
    }

    public static SearchContext search(String question, int maxArticleCount, boolean webUi, String driverPath) {
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
        WebDriver driver = BrowserSelenium.getWebDriver(null, webUi, driverPath);
        SeleniumUtil.blockNetworkResources(driver);
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        if (true) {
            driver.get("https://www.sogou.com/");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            try {
                wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".search-box form"), 0));
            } catch (Exception e) {
                e.printStackTrace();
                if (SeleniumUtil.isCannotRecoveryException(e)) {
                    throw e;
                }
            }

            WebElement inputElem = driver.findElement(By.cssSelector(".search-box form .sec-input"));
            inputElem.click();
            inputElem.sendKeys(question);

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(RANDOM.nextInt(3) + 1));
            WebElement enterElem = driver.findElement(By.cssSelector(".search-box form .enter-input"));
            enterElem.click();
        }
        try {

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
                        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
                    } else {
                        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
                    }
                    try {
                        if (!Objects.equals(driver.getCurrentUrl(), entry.getKey().getUrl())) {
                            driver.navigate().to(entry.getKey().getUrl());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (SeleniumUtil.isCannotRecoveryException(e)) {
                            break;
                        }
                        continue;
                    }

                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(RANDOM.nextInt(5) + 1));
                    if (true) {
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
                        try {
                            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("body"), 0));
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (SeleniumUtil.isCannotRecoveryException(e)) {
                                break;
                            }
                        }
                    }

                    if (SearchType.SEARCH_FIRST == entry.getValue()) {
                        for (int i = 0; i < 3; i++) {
                            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
                            try {
                                wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".results .vrwrap[exposed=\"1\"] .vr-title a"), 0));
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                if(i==2){
                                    return context;
                                }
                                continue;
                            }
                        }

                        entry.getKey().setUrl(driver.getCurrentUrl());
                    }

                    // 百度搜索首页
                    if (SearchType.SEARCH_FIRST == entry.getValue()) {
                        // 最大翻页
                        int maxPage = 10;

                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        long lastHeight = (long) js.executeScript("return document.body.scrollHeight");

                        for (int i = 0; i < maxPage; i++) {
                            // 3. 执行 JS 滚动到页面最底部
                            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

                            // 4. 等待新内容加载 (根据网络情况和页面渲染速度调整时间)
                            Thread.sleep(2000);

                            // 5. 获取滚动后的新页面高度
                            long newHeight = (long) js.executeScript("return document.body.scrollHeight");

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
                        List<WebElement> wwwElems = driver.findElements(By.cssSelector(".results .vrwrap[exposed=\"1\"]"));
                        for (WebElement item : wwwElems) {
                            String text = item.getText();
//                            System.out.println("www-response:\n" + text);
                            List<WebElement> titleElems = item.findElements(By.cssSelector(".vr-title"));
                            if (titleElems == null || titleElems.isEmpty()) {
                                continue;
                            }
                            WebElement titleElem = titleElems.get(0);
                            if (titleElem == null) {
                                continue;
                            }
                            String title = titleElem.getText();
//                        System.out.println("www-href:\n" + href);
                            List<WebElement> aElems = item.findElements(By.cssSelector(".vr-title a"));
                            if (aElems == null || aElems.isEmpty()) {
                                continue;
                            }
                            WebElement aElem = aElems.get(0);
                            if (aElem != null) {
                                String href = aElem.getAttribute("href");
                                System.out.println("www-href:\n" + href);
                                if (context != null) {
                                    String itemText = item.getText();
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

                    if(SearchType.SEARCH_FIRST == entry.getValue()){
                        if (context != null) {
                            SeleniumUtil.removeNoContentElements(driver);
                            SearchResult result = entry.getKey();
                            result.setTitle(driver.getTitle());
                            result.setHtml(driver.getPageSource());
                            WebElement body = driver.findElement(By.tagName("body"));
                            if (body != null) {
                                result.setText(body.getText());
                            }
                        }
                    }

                    if (maxFetchCount.get() <= 0) {
                        return context;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (SeleniumUtil.isCannotRecoveryException(e)) {
                        break;
                    }
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
