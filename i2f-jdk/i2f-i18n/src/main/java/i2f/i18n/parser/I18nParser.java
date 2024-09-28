package i2f.i18n.parser;

import i2f.i18n.data.I18nItem;

import java.util.Collection;


/**
 * @author Ice2Faith
 * @date 2024/9/28 14:18
 */
public interface I18nParser {
    static String unescape(String str) {
        if (str == null) {
            return str;
        }
        return str.replaceAll("\\t", "\t")
                .replaceAll("\\n", "\n")
                .replaceAll("\\r", "\r");
    }

    Collection<I18nItem> parse();
}
