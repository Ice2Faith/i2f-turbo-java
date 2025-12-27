package i2f.spring.matcher;

import i2f.match.std.IPriorMatcher;
import org.springframework.util.AntPathMatcher;

/**
 * @author Ice2Faith
 * @date 2025/9/20 17:13
 */
public class SpringAntPathMatcher implements IPriorMatcher {
    public static final SpringAntPathMatcher PATH = new SpringAntPathMatcher("/");
    public static final SpringAntPathMatcher PKG = new SpringAntPathMatcher(".");

    private AntPathMatcher matcher = new AntPathMatcher("/");

    public SpringAntPathMatcher() {
    }

    public SpringAntPathMatcher(AntPathMatcher matcher) {
        this.matcher = matcher;
    }

    public SpringAntPathMatcher(String separator) {
        this.matcher = new AntPathMatcher(separator);
    }

    @Override
    public boolean matches(String str, String patten) {
        if (str == null) {
            return false;
        }
        if (patten == null) {
            return false;
        }
        boolean ok = matcher.match(patten, str);
        if (!ok) {
            return false;
        }
        return true;
    }

    @Override
    public double matchRate(String str, String patten) {
        if (str == null) {
            return MATCH_FAILURE_VALUE;
        }
        if (patten == null) {
            return MATCH_FAILURE_VALUE;
        }
        boolean ok = matcher.match(patten, str);
        if (!ok) {
            return MATCH_FAILURE_VALUE;
        }
        String[] arr = {
                "**", "*"
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
