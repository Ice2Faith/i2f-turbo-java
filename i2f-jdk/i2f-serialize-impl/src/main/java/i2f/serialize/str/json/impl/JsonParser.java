package i2f.serialize.str.json.impl;

import i2f.match.regex.RegexPattens;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2024/6/3 15:23
 * @desc
 */
public class JsonParser {

    public static List<String> splitTokens(String str, Function<String, String> filter) {
        AtomicInteger idx = new AtomicInteger(0);
        List<String> tokens = new ArrayList<>();
        while (true) {
            String part = nextBlock(str, idx);
            if (part == null) {
                break;
            }
            if (filter != null) {
                part = filter.apply(part);
            }
            if (part != null) {
                tokens.add(part);
            }
        }
        return tokens;
    }

    public static Object parse(String json) {
        List<String> tokens = splitTokens(json, part -> {
            String trim = part.trim();
            if (trim.isEmpty()) {
                return null;
            }
            if (trim.startsWith("//") || trim.startsWith("/*")) {
                return null;
            }
            return trim;
        });
        String bestJson = null;
        for (String token : tokens) {
            if (token.startsWith("{") || token.startsWith("[")) {
                bestJson = token;
                break;
            }
        }

        if (bestJson == null) {
            throw new IllegalArgumentException("non-json content found!");
        }

        String prefix = bestJson.substring(0, 1);
        String innerText = bestJson.substring(1, bestJson.length() - 1);

        tokens = splitTokens(innerText, part -> {
            String trim = part.trim();
            if (trim.isEmpty()) {
                return null;
            }
            if (trim.startsWith("//") || trim.startsWith("/*")) {
                return null;
            }
            return trim;
        });

        if ("{".equals(prefix)) {
            Map<String, Object> ret = new LinkedHashMap<>();

            List<String> flags = new ArrayList<>();
            for (String token : tokens) {
                char ch = token.charAt(0);
                if (ch == '[' || ch == '{' || ch == '(') {
                    flags.add(token);
                } else if (ch == '"' || ch == '\'') {
                    flags.add(token);
                } else {
                    String item = token;
                    if (item.startsWith(":")) {
                        flags.add(":");
                        item = item.substring(1);
                    }
                    if (item.endsWith(",")) {
                        item = item.substring(0, item.length() - 1);
                        if (!item.isEmpty()) {
                            flags.add(item);
                        }
                        flags.add(",");
                    } else {
                        if (!item.isEmpty()) {
                            flags.add(item);
                        }
                    }
                }
            }

            Map<String, String> kvs = new LinkedHashMap<>();
            String key = null;
            int i = 0;
            for (String token : flags) {
                if (i % 4 == 0) {
                    key = token;
                }
                if (i % 4 == 2) {
                    String val = token;
                    kvs.put(key, val);
                }
                i++;
            }

            for (Map.Entry<String, String> entry : kvs.entrySet()) {
                String k = unescape(entry.getKey());
                String v = entry.getValue();
                if (v.startsWith("{") || v.startsWith("[")) {
                    Object val = parse(v);
                    ret.put(k, val);
                } else if (v.startsWith("\"") || v.startsWith("'")) {
                    Object val = unescape(v);
                    ret.put(k, val);
                } else if (v.matches(RegexPattens.INTEGER_NUMBER_REGEX)) {
                    long val = Long.parseLong(v);
                    ret.put(k, val);
                } else if (v.matches(RegexPattens.DOUBLE_NUMBER_REGEX)) {
                    Object val = Double.parseDouble(v);
                    ret.put(k, val);
                } else if ("true".equals(v)) {
                    ret.put(k, true);
                } else if ("false".equals(v)) {
                    ret.put(k, false);
                } else if ("null".equals(v)) {
                    ret.put(k, null);
                } else {
                    throw new IllegalArgumentException("un-support parse value: " + v + " of key: " + k);
                }
            }

            return ret;
        } else if ("[".equals(prefix)) {
            List<Object> ret = new ArrayList<>();

            List<String> flags = new ArrayList<>();
            for (String token : tokens) {
                char ch = token.charAt(0);
                if (ch == '[' || ch == '{' || ch == '(') {
                    flags.add(token);
                } else if (ch == '"' || ch == '\'') {
                    flags.add(token);
                } else {
                    String item = token;
                    if (item.startsWith(",")) {
                        flags.add(",");
                        item = item.substring(1);
                    }
                    if (item.endsWith(",")) {
                        item = item.substring(0, item.length() - 1);
                        if (!item.isEmpty()) {
                            String[] arr = item.split(",");
                            for (int i = 0; i < arr.length; i++) {
                                if (i > 0) {
                                    flags.add(",");
                                }
                                flags.add(arr[i]);
                            }
                        }
                        flags.add(",");
                    } else {
                        if (!item.isEmpty()) {
                            String[] arr = item.split(",");
                            for (int i = 0; i < arr.length; i++) {
                                if (i > 0) {
                                    flags.add(",");
                                }
                                flags.add(arr[i]);
                            }
                        }
                    }
                }
            }

            List<String> elems = new ArrayList<>();
            int i = 0;
            for (String flag : flags) {
                if (i % 2 == 0) {
                    elems.add(flag);
                }
                i++;
            }

            for (String elem : elems) {
                String v = elem;
                if (v.startsWith("{") || v.startsWith("[")) {
                    Object val = parse(v);
                    ret.add(val);
                } else if (v.startsWith("\"") || v.startsWith("'")) {
                    Object val = unescape(v);
                    ret.add(val);
                } else if (v.matches(RegexPattens.INTEGER_NUMBER_REGEX)) {
                    long val = Long.parseLong(v);
                    ret.add(val);
                } else if (v.matches(RegexPattens.DOUBLE_NUMBER_REGEX)) {
                    Object val = Double.parseDouble(v);
                    ret.add(val);
                } else if ("true".equals(v)) {
                    ret.add(true);
                } else if ("false".equals(v)) {
                    ret.add(false);
                } else if ("null".equals(v)) {
                    ret.add(null);
                } else {
                    throw new IllegalArgumentException("un-support parse value: " + v);
                }
            }

            return ret;
        }

        return null;
    }

    public static String unescape(String str) {
        if (str == null) {
            return null;
        }
        String item = str;
        if (str.startsWith("\"") || str.startsWith("'")) {
            item = str.substring(1, str.length() - 1);
        }
        item = item.replaceAll("\\\\\\\\", "\\\\");
        item = item.replaceAll("\\\\n", "\n");
        item = item.replaceAll("\\\\r", "\r");
        item = item.replaceAll("\\\\t", "\t");
        item = item.replaceAll("\\\\\"", "\"");
        item = item.replaceAll("\\\\'", "'");
        return item;
    }

    public static String nextBlock(String str, AtomicInteger index) {
        if (index.get() >= str.length()) {
            return null;
        }
        char ch = str.charAt(index.get());
        if (ch == '"') {
            Pattern pattern = Pattern.compile(RegexPattens.QUOTE_STRING_REGEX);
            Matcher matcher = pattern.matcher(str);
            if (matcher.find(index.get())) {
                MatchResult result = matcher.toMatchResult();
                int start = result.start();
                int end = result.end();
                String part = str.substring(start, end);
                index.set(end);
                return part;
            }
        } else if (ch == '\'') {
            Pattern pattern = Pattern.compile(RegexPattens.SINGLE_QUOTE_STRING_REGEX);
            Matcher matcher = pattern.matcher(str);
            if (matcher.find(index.get())) {
                MatchResult result = matcher.toMatchResult();
                int start = result.start();
                int end = result.end();
                String part = str.substring(start, end);
                index.set(end);
                return part;
            }
        } else if (ch == '{') {
            String part = nextEnclose(str, index, '{', '}');
            return part;
        } else if (ch == '[') {
            String part = nextEnclose(str, index, '[', ']');
            return part;
        } else if (ch == '(') {
            String part = nextEnclose(str, index, '(', ')');
            return part;
        } else if (ch == '/') {
            if (index.get() + 1 < str.length()) {
                char nch = str.charAt(index.get() + 1);
                if (nch == '/') {
                    Pattern pattern = Pattern.compile(RegexPattens.SINGLE_LINE_COMMENT_REGEX);
                    Matcher matcher = pattern.matcher(str);
                    if (matcher.find(index.get())) {
                        MatchResult result = matcher.toMatchResult();
                        int start = result.start();
                        int end = result.end();
                        String part = str.substring(start, end);
                        index.set(end);
                        return part;
                    } else {
                        index.incrementAndGet();
                        return ch + "";
                    }
                } else if (nch == '*') {
                    Pattern pattern = Pattern.compile(RegexPattens.MULTI_LINE_COMMENT_REGEX);
                    Matcher matcher = pattern.matcher(str);
                    if (matcher.find(index.get())) {
                        MatchResult result = matcher.toMatchResult();
                        int start = result.start();
                        int end = result.end();
                        String part = str.substring(start, end);
                        index.set(end);
                        return part;
                    }
                }
            }
        } else if (ch == ' ' || ch == '\t' || ch == '\n') {
            StringBuilder builder = new StringBuilder();
            int i = index.get();
            while (i < str.length()) {
                ch = str.charAt(i);
                if (ch == ' ' || ch == '\t' || ch == '\n') {
                    builder.append(ch);
                } else {
                    break;
                }
                i++;
            }
            index.set(i);
            String part = builder.toString();
            return part;
        }

        StringBuilder builder = new StringBuilder();
        int i = index.get();
        while (i < str.length()) {
            ch = str.charAt(i);
            if (ch == ' ' || ch == '\t' || ch == '\n') {
                break;
            }
            if (ch == ';') {
                break;
            }
            if (ch == '"' || ch == '\'' || ch == '{' || ch == '[' || ch == '(') {
                break;
            }
            builder.append(ch);
            i++;

        }
        if (ch == ';') {
            builder.append(ch);
            i++;
        }
        index.set(i);
        String part = builder.toString();
        return part;

    }

    public static String nextEnclose(String expression, AtomicInteger index, char left, char right) {
        Stack<Character> stack = new Stack<>();
        String ret = "";
        while (index.get() < expression.length()) {
            char ch = expression.charAt(index.get());
            if (ch == left) {
                stack.push(ch);
            } else if (ch == right) {
                if (stack.isEmpty()) {
                    return "";
                }
                stack.pop();
                if (stack.isEmpty()) {
                    ret += ch;
                    index.incrementAndGet();
                    return ret;
                }
            } else if (ch == '"') {
                Pattern pattern = Pattern.compile(RegexPattens.QUOTE_STRING_REGEX);
                Matcher matcher = pattern.matcher(expression);
                if (matcher.find(index.get())) {
                    MatchResult result = matcher.toMatchResult();
                    int start = result.start();
                    int end = result.end();
                    String part = expression.substring(start, end);
                    index.set(end);
                    ret += part;
                    continue;
                }
            } else if (ch == '\'') {
                Pattern pattern = Pattern.compile(RegexPattens.SINGLE_QUOTE_STRING_REGEX);
                Matcher matcher = pattern.matcher(expression);
                if (matcher.find(index.get())) {
                    MatchResult result = matcher.toMatchResult();
                    int start = result.start();
                    int end = result.end();
                    String part = expression.substring(start, end);
                    index.set(end);
                    ret += part;
                    continue;
                }
            }
            ret += ch;
            index.incrementAndGet();

        }
        return "";
    }
}
