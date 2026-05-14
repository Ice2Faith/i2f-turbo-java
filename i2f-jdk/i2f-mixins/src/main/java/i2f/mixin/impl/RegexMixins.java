package i2f.mixin.impl;


import i2f.match.regex.RegexUtil;
import i2f.match.regex.data.RegexFindPartMeta;
import i2f.match.regex.data.RegexMatchItem;
import i2f.mixin.MixinProxyFactory;
import i2f.mixin.consts.MixinConsts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/5/14 9:11
 * @desc
 */
public interface RegexMixins {
    default String convertOracleRegexExpression(String regex) {
        if (regex == null) {
            return null;
        }
        for (String[] arr : MixinConsts.ORACLE_REGEX_REPLACE_MAPPING) {
            regex = regex.replace(arr[0], arr[1]);
        }
        return regex;
    }

    default String convertOracleRegexReplacement(String replacement) {
        if (replacement == null) {
            return null;
        }
        replacement = RegexUtil.regexFindAndReplace(replacement, "[\\\\]\\d+", (str) -> {
            return "$" + str.substring(1);
        });
        return replacement;
    }

    default String regex_replace(Object str, String regex) {
        return regex_replace(str, regex, "");
    }

    default String regex_replace(Object str, String regex, Object replacement) {
        return regex_replace(str, regex, replacement, -1);
    }

    default String regex_replace(Object oStr, String regex, Object oReplacement, int occurrence) {
        if (oStr == null) {
            return null;
        }
        if (regex == null) {
            return null;
        }
        if (oReplacement == null) {
            oReplacement = "";
        }
        String str = String.valueOf(oStr);
        String replacement = String.valueOf(oReplacement);
        regex = convertOracleRegexExpression(regex);
        replacement = convertOracleRegexReplacement(replacement);
        if (occurrence <= 0) {
            return str.replaceAll(regex, replacement);
        } else {
            AtomicInteger count = new AtomicInteger(0);
            final String reg = regex;
            final String rep = replacement;
            return RegexUtil.regexFindAndReplace(str, regex, (s) -> {
                count.incrementAndGet();
                if (count.get() == occurrence) {
                    return s.replaceFirst(reg, rep);
                }
                return s;
            });
        }
    }

    default String regexp_replace(Object str, String regex) {
        return regexp_replace(str, regex, "");
    }

    default String regexp_replace(Object str, String regex, Object replacement) {
        return regex_replace(str, regex, replacement);
    }

    default String regexp_replace(Object str, String regex, Object replacement, int occurrence) {
        return regex_replace(str, regex, replacement, occurrence);
    }

    default boolean regex_match(Object oStr, String regex) {
        if (oStr == null) {
            return false;
        }
        if (regex == null) {
            return false;
        }
        String str = String.valueOf(oStr);
        regex = convertOracleRegexExpression(regex);
        return str.matches(regex);
    }

    default boolean regexp_match(Object str, String regex) {
        return regex_match(str, regex);
    }

    default List<String> regex_find(Object oStr, String regex) {
        if (oStr == null) {
            return null;
        }
        if (regex == null) {
            return new ArrayList<>();
        }
        String str = String.valueOf(oStr);
        regex = convertOracleRegexExpression(regex);
        return RegexUtil.regexFinds(str, regex)
                .stream()
                .map(e -> e.getMatchStr())
                .collect(Collectors.toList());
    }

    default boolean regex_contains(Object oStr, String regex) {
        if (oStr == null) {
            return false;
        }
        if (regex == null) {
            return false;
        }
        String str = String.valueOf(oStr);
        regex = convertOracleRegexExpression(regex);
        List<RegexMatchItem> list = RegexUtil.regexFinds(str, regex, 1);
        if (!list.isEmpty()) {
            return true;
        }
        return false;
    }

    default boolean regexp_contains(Object str, String regex) {
        return regex_contains(str, regex);
    }

    default boolean regex_like(Object oStr, String regex) {
        return regex_contains(oStr, regex);
    }

    default boolean regexp_like(Object str, String regex) {
        return regex_contains(str, regex);
    }

    default int regex_index(Object oStr, String regex) {
        if (oStr == null) {
            return -1;
        }
        if (regex == null) {
            return -1;
        }
        String str = String.valueOf(oStr);
        regex = convertOracleRegexExpression(regex);
        List<RegexMatchItem> list = RegexUtil.regexFinds(str, regex, 1);
        if (!list.isEmpty()) {
            RegexMatchItem item = list.get(0);
            return item.getIdxStart();
        }
        return -1;
    }

    default int regexp_index(Object str, String regex) {
        return regex_index(str, regex);
    }

    default int regex_index_end(Object oStr, String regex) {
        if (oStr == null) {
            return -1;
        }
        if (regex == null) {
            return -1;
        }
        String str = String.valueOf(oStr);
        regex = convertOracleRegexExpression(regex);
        List<RegexMatchItem> list = RegexUtil.regexFinds(str, regex, 1);
        if (!list.isEmpty()) {
            RegexMatchItem item = list.get(0);
            return item.getIdxEnd();
        }
        return -1;
    }

    default int regexp_index_end(Object str, String regex) {
        return regex_index_end(str, regex);
    }

    default String regex_extra(Object oStr, String regex) {
        return regex_extra(oStr, regex, 0);
    }

    default String regex_extra(Object oStr, String regex, int index) {
        if (oStr == null) {
            return null;
        }
        if (regex == null) {
            return null;
        }
        String str = String.valueOf(oStr);
        regex = convertOracleRegexExpression(regex);
        List<RegexMatchItem> list = RegexUtil.regexFinds(str, regex, index >= 0 ? index + 1 : -1);
        if (!list.isEmpty()) {
            int size = list.size();
            if (index < 0) {
                index = size - index;
            }
            if (index >= 0 && index < size) {
                RegexMatchItem item = list.get(index);
                return item.getMatchStr();
            }
        }
        return null;
    }

    default String regexp_extra(Object str, String regex) {
        return regex_extra(str, regex, 0);
    }

    default String regexp_extra(Object str, String regex, int index) {
        return regex_extra(str, regex, index);
    }

    default String regex_drop(Object oStr, String regex) {
        return regex_drop(oStr, regex, 0, -1);
    }

    default String regex_drop(Object oStr, String regex, int index) {
        return regex_drop(oStr, regex, index, -1);
    }

    default String regex_drop(Object oStr, String regex, int index, int count) {
        if (oStr == null) {
            return null;
        }
        if (regex == null) {
            return null;
        }
        String str = String.valueOf(oStr);
        regex = convertOracleRegexExpression(regex);
        List<RegexFindPartMeta> parts = RegexUtil.regexFindParts(str, regex);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        int n = 0;
        for (RegexFindPartMeta part : parts) {
            if (part.isMatch()) {
                if (i >= index) {
                    if (n < count || count < 0) {
                        builder.append(part.getPart());
                    }
                    n++;
                }
                i++;
            } else {
                builder.append(part.getPart());
            }
        }
        return builder.toString();
    }

    default String regex_find_join(Object str, String regex) {
        return regex_find_join(str, regex, ",");
    }

    default String regex_find_join(Object str, String regex, Object separator) {
        List<String> list = regex_find(str, regex);
        return MixinProxyFactory.getMixinInstance(StringMixins.class).join(list, separator);
    }

    default String substr_regex_index(Object obj, String substr, int len) {
        int idx = regex_index(obj, substr);
        if (idx < 0) {
            return "";
        }
        if (len < 0) {
            return MixinProxyFactory.getMixinInstance(StringMixins.class).substr(obj, idx);
        }
        return MixinProxyFactory.getMixinInstance(StringMixins.class).substr(obj, idx, len);
    }

    default String substr_regex_index(Object obj, String substr) {
        return substr_regex_index(obj, substr, -1);
    }

    default String substr_regex_index_end(Object obj, String substr, int len) {
        int idx = regex_index_end(obj, substr);
        if (idx < 0) {
            return "";
        }
        if (len < 0) {
            return MixinProxyFactory.getMixinInstance(StringMixins.class).substr(obj, idx);
        }
        return MixinProxyFactory.getMixinInstance(StringMixins.class).substr(obj, idx, len);
    }

    default String substr_regex_index_end(Object obj, String substr) {
        return substr_regex_index_end(obj, substr, -1);
    }

    default ArrayList<String> regex_split(Object str, String regex) {
        return regex_split(str, regex, -1);
    }

    default ArrayList<String> regex_split(Object oStr, String regex, int limit) {
        if (oStr == null) {
            return new ArrayList<>();
        }
        String str = String.valueOf(oStr);
        if (regex == null) {
            return new ArrayList<>(Collections.singletonList(str));
        }
        regex = convertOracleRegexExpression(regex);
        String[] arr = RegexUtil.getPattern(regex).split(str, limit);
        return new ArrayList<>(Arrays.asList(arr));
    }

}
