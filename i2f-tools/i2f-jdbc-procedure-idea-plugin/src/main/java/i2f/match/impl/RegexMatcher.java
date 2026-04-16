package i2f.match.impl;

import i2f.lru.LruMap;
import i2f.match.std.IPriorMatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2025/9/20 16:25
 */
public class RegexMatcher implements IPriorMatcher {
    protected LruMap<String, Pattern> cache = new LruMap<>(1024);

    public Pattern patternOf(String pattern) {
        if (pattern == null) {
            return null;
        }
        Pattern ret = cache.get(pattern);
        if (ret != null) {
            return ret;
        }
        ret = Pattern.compile(pattern);
        cache.put(pattern, ret);
        return ret;
    }

    @Override
    public boolean matches(String str, String patten) {
        if (str == null) {
            return false;
        }
        if (patten == null) {
            return false;
        }
        Pattern regex = patternOf(patten);
        Matcher matcher = regex.matcher(str);
        return matcher.matches();
    }

    @Override
    public double matchRate(String str, String patten) {
        if (str == null) {
            return MATCH_FAILURE_VALUE;
        }
        if (patten == null) {
            return MATCH_FAILURE_VALUE;
        }
        Pattern regex = patternOf(patten);
        Matcher matcher = regex.matcher(str);
        boolean ok = matcher.matches();
        if (!ok) {
            return MATCH_FAILURE_VALUE;
        }
        String[] arr = {
                ".+", ".*",
                "\\d+", "\\s+", "\\w+",
                "\\D+", "\\S+", "\\W+",
                "\\d*", "\\s*", "\\w*",
                "\\D*", "\\S*", "\\W*",
                "\\d", "\\s", "\\w",
                "\\D", "\\S", "\\W",
                "?:", "?=", "?!",
        };
        String iter = patten;
        for (String item : arr) {
            iter = iter.replace(item, "");
            if (iter.isEmpty()) {
                break;
            }
        }
        int plen = iter.length();

        return calcMatchRate(str.length(), patten.length(), str.length(), patten.length(), Math.min(plen, str.length()));
    }

}
