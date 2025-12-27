package i2f.match.test;

import i2f.match.impl.AntMatcher;
import i2f.match.impl.RegexMatcher;
import i2f.match.std.IPriorMatcher;

/**
 * @author Ice2Faith
 * @date 2025/9/20 15:26
 */
public class TestMatcher {
    public static void main(String[] args) {
        IPriorMatcher matcher = null;

        matcher = new RegexMatcher();
        match(matcher, "\\d+", "12345678910", false);


        matcher = new AntMatcher("/");

        match(matcher, "/**", "/", true);
        match(matcher, "/**", "/user", true);
        match(matcher, "/**", "/user/login", true);
        match(matcher, "/**", "/user/auth/inner", true);
        match(matcher, "/**", "/user/auth/third/alipay", true);
        match(matcher, "/**", "/user/list", true);
        match(matcher, "/**", "/dict", true);
        match(matcher, "/**", "/dict/list", true);
        match(matcher, "/**", "/dict/tree/all", true);
        match(matcher, "/**", "", false);

        match(matcher, "/user/**", "/", false);
        match(matcher, "/user/**", "/user", false);
        match(matcher, "/user/**", "/user/login", true);
        match(matcher, "/user/**", "/user/auth/inner", true);
        match(matcher, "/user/**", "/user/auth/third/alipay", true);
        match(matcher, "/user/**", "/user/list", true);
        match(matcher, "/user/**", "/dict", false);
        match(matcher, "/user/**", "/dict/list", false);
        match(matcher, "/user/**", "/dict/tree/all", false);
        match(matcher, "/user/**", "", false);

        match(matcher, "/dict/**", "/", false);
        match(matcher, "/dict/**", "/user", false);
        match(matcher, "/dict/**", "/user/login", false);
        match(matcher, "/dict/**", "/user/auth/inner", false);
        match(matcher, "/dict/**", "/user/auth/third/alipay", false);
        match(matcher, "/dict/**", "/user/list", false);
        match(matcher, "/dict/**", "/dict", false);
        match(matcher, "/dict/**", "/dict/list", true);
        match(matcher, "/dict/**", "/dict/tree/all", true);
        match(matcher, "/dict/**", "", false);

        match(matcher, "/*er/**", "/", false);
        match(matcher, "/*er/**", "/user", false);
        match(matcher, "/*er/**", "/user/login", true);
        match(matcher, "/*er/**", "/user/auth/inner", true);
        match(matcher, "/*er/**", "/user/auth/third/alipay", true);
        match(matcher, "/*er/**", "/user/list", true);
        match(matcher, "/*er/**", "/dict", false);
        match(matcher, "/*er/**", "/dict/list", false);
        match(matcher, "/*ee/**", "/dict/tree/all", false);
        match(matcher, "/*er/**", "", false);

        match(matcher, "/*/list", "/", false);
        match(matcher, "/*/list", "/user", false);
        match(matcher, "/*/list", "/user/login", false);
        match(matcher, "/*/list", "/user/auth/inner", false);
        match(matcher, "/*/list", "/user/auth/third/alipay", false);
        match(matcher, "/*/list", "/user/list", true);
        match(matcher, "/*/list", "/dict", false);
        match(matcher, "/*/list", "/dict/list", true);
        match(matcher, "/*/list", "/dict/tree/all", false);
        match(matcher, "/*/list", "", false);

        match(matcher, "/**/list", "/", false);
        match(matcher, "/**/list", "/user", false);
        match(matcher, "/**/list", "/user/login", false);
        match(matcher, "/**/list", "/user/auth/inner", false);
        match(matcher, "/**/list", "/user/auth/list/alipay", false);
        match(matcher, "/**/list", "/user/list", true);
        match(matcher, "/**/list", "/dict", false);
        match(matcher, "/**/list", "/dict/list", true);
        match(matcher, "/**/list", "/dict/tree/list", true);
        match(matcher, "/**/list", "", false);

        match(matcher, "**/list/**", "/user/auth/list/alipay", true);
        match(matcher, "**/list/**", "/list/alipay", true);
        match(matcher, "**/list/**", "/dict/list", false);
        match(matcher, "**/list/**", "/dict/list2", false);

        match(matcher, "**/list", "/list", true);
        match(matcher, "**/list", "/dict/list", true);
        match(matcher, "**/list", "/dict/tree/list", true);
        match(matcher, "**/list", "/dict/list/tree", false);

        match(matcher, "**/*ut*/**", "/user/auth/inner", true);
        match(matcher, "**/*ut*/**", "/user/auth/list/alipay", true);
        match(matcher, "**/*ut*/**", "/user/aut/list/alipay", true);
        match(matcher, "**/*ut*/**", "/user/uth/list/alipay", true);
        match(matcher, "**/*ut*/**", "ut/list/alipay", true);
        match(matcher, "**/*ut*/**", "uth/list/alipay", true);
        match(matcher, "**/*ut*/**", "aut/list/alipay", true);
        match(matcher, "**/*ut*/**", "auth/list/alipay", true);
        match(matcher, "**/*ut*/**", "/list/ut", false);
        match(matcher, "**/*ut*/**", "/list/uth", false);
        match(matcher, "**/*ut*/**", "/list/aut", false);
        match(matcher, "**/*ut*/**", "/list/auth", false);
        match(matcher, "**/*ut*/**", "/user/xx/list/alipay", false);
    }

    public static void match(IPriorMatcher matcher, String pattern, String text, boolean expect) {
        try {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("matcher: " + matcher.getClass());
            System.out.println("pattern: " + pattern);
            System.out.println("text: " + text);
            double rate = matcher.matchRate(text, pattern);
            System.out.println("rate: " + rate);
            boolean matched = matcher.matched(rate);
            System.out.println("matched: " + matched);
            if (matched == expect) {
                System.out.println("-----------------ok");
            } else {
                System.out.println("*****************error");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
