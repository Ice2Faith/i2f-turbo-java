package i2f.spring.mvc.metadata;

import i2f.match.regex.RegexUtil;
import org.springframework.core.env.Environment;

/**
 * @author Ice2Faith
 * @date 2026/9/9 19:48
 * @desc
 */
public class SpringMetadataUtil {
    public static String resolveParameters(String url, Environment environment) {
        if(environment==null || url==null){
            return url;
        }
        if(!url.contains("$")){
            return url;
        }
        return RegexUtil.regexFindAndReplace(url, "\\$\\{[^}]}+\\}", str -> {
            String expr = str.substring(2, str.length() - 1);
            String[] arr = expr.split(":", 2);
            String prop = arr[0];
            String val = environment.getProperty(prop);
            if (val == null) {
                if (arr.length > 1) {
                    val = arr[1];
                }
            }
            if (val == null) {
                return str;
            }
            return val;
        });
    }

}
