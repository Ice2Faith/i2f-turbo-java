package i2f.extension.browser.selenium.test;

import i2f.extension.browser.selenium.search.*;
import i2f.extension.browser.selenium.search.data.SearchContext;

/**
 * @author Ice2Faith
 * @date 2025/6/5 20:31
 * @desc
 */
public class TestSelenium {
    public static void main(String[] args) throws Exception {

        test();

    }

    public static void test() {
        long bts = System.currentTimeMillis();
        SearchContext ctx = BaiduKaifaSearch.search("selenium在Java中报错无法进行websocket连接", 5, "G:/webdriver");
        long ets = System.currentTimeMillis();
        long diff = (ets - bts) / 1000;
//
//        bts = System.currentTimeMillis();
//        ctx = BaiduSearch.search("selenium在Java中报错无法进行websocket连接", 5, "G:/webdriver");
//        ets = System.currentTimeMillis();
//        diff = (ets - bts) / 1000;
//        System.out.println("ok");
//
//        bts = System.currentTimeMillis();
//        ctx = BaiduSearch.search("selenium在Java中报错无法进行websocket连接", 10, "G:/webdriver");
//        ets = System.currentTimeMillis();
//        diff = (ets - bts) / 1000;
//        System.out.println("ok");
//
//        bts = System.currentTimeMillis();
//        ctx = BaiduSearch.search("selenium在Java中报错无法进行websocket连接", 3, "G:/webdriver");
//        ets = System.currentTimeMillis();
//        diff = (ets - bts) / 1000;
//        System.out.println("ok");

    }
}
