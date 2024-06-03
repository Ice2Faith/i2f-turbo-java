package i2f.match.regex;


import i2f.match.regex.data.RegexFindPartMeta;
import i2f.match.regex.data.RegexMatchItem;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2023/8/31 9:38
 * @desc
 */
public class RegexUtil {
    public static List<RegexMatchItem> regexFinds(String str, String regex) {
        List<RegexMatchItem> ret = new ArrayList<>();
        Pattern patten = Pattern.compile(regex);
        Matcher matcher = patten.matcher(str);
        while (matcher.find()) {
            MatchResult result = matcher.toMatchResult();

            RegexMatchItem item = new RegexMatchItem();
            item.srcStr = str;
            item.regexStr = regex;
            item.matchStr = matcher.group();
            item.idxStart = result.start();
            item.idxEnd = result.end();

            ret.add(item);
        }
        return ret;
    }

    /**
     * 将字符串分解为描述的有序对象，对象中包含匹配串或者非匹配串部分，为有序ArrayList
     * 以便根据此List分别对匹配和非匹配部分提取或者重新构造字符串
     *
     * @param str
     * @param regex
     * @return
     */
    public static List<RegexFindPartMeta> regexFindParts(String str, String regex) {
        List<RegexFindPartMeta> ret = new ArrayList<>(64);
        Pattern patten = Pattern.compile(regex);
        Matcher matcher = patten.matcher(str);
        int lidx = 0;
        while (matcher.find()) {
            MatchResult result = matcher.toMatchResult();

            RegexFindPartMeta oth = new RegexFindPartMeta();
            oth.part = str.substring(lidx, result.start());
            oth.isMatch = false;
            if (!"".equals(oth.part)) {
                ret.add(oth);
            }

            lidx = result.end();
            String group = matcher.group();

            RegexFindPartMeta mth = new RegexFindPartMeta();
            mth.part = group;
            mth.isMatch = true;
            ret.add(mth);

        }

        RegexFindPartMeta oth = new RegexFindPartMeta();
        oth.part = str.substring(lidx);
        oth.isMatch = false;
        if (!"".equals(oth.part)) {
            ret.add(oth);
        }

        return ret;
    }

    /**
     * 对匹配到的内容进行使用Map进行转换后重新恢复完整串
     * 例如：
     * str=a1b2c3
     * regex=\d+
     * mapper=(str)->String.format("0x%x",Integer.parseInf(str))
     * 则返回值为
     * ret=a0x1b0x2c0x3
     *
     * @param str
     * @param regex
     * @param mapper
     * @return
     */
    public static String regexFindAndReplace(String str, String regex, Function<String, String> mapper) {
        List<RegexFindPartMeta> list = regexFindParts(str, regex);
        StringBuilder builder = new StringBuilder();
        for (RegexFindPartMeta item : list) {
            if (!item.isMatch) {
                builder.append(item.part);
            } else {
                builder.append(mapper.apply(item.part));
            }
        }
        return builder.toString();
    }

    /**
     * 按索引格式化字符串
     * 匹配模式：{索引 格式修饰符:格式}
     * 索引如果不指定，则直接使用占位符的索引
     * 格式修饰符，可以用于指定特定的标志位，限定为一个字符，可以不使用
     * 其中格式可以不指定
     * 匹配的格式举例如下：
     * {}
     * {:yyyy-MM}
     * {0}
     * {-1}
     * {2:yyyy-MM-dd HH:mm:ss}
     * {3: \{yyyyMMdd\}-HHmmss.SSS}
     * { 4 :%02d}
     * { 5 t:%02d}
     * {6e:}
     * 特别说明：
     * 索引支持负值，表示从后往前的负索引，上面-1就是案例，-1表示最后一个元素的索引，原因是没有-0的说法
     * 冒号之前的索引，支持前后空白符号，上面的4就是案例
     * 冒号及后面的格式，不会忽略空白符号，上面3就是案例
     * 格式修饰符必须紧跟冒号，否则不会匹配，上面5就是案例
     * 同时，如果只写了冒号，后面的格式为空串，则忽略这个模式，上面6就是案例
     * 在格式中，\{和\}会被恢复转义替换为{和}
     * 其他的\后面的转义不会处理，保留原状
     * 作为格式，
     * 对于时间类型，指定为日期时间的格式，例如上面的2/3
     * 否则，则使用String.format使用的格式，例如上面的4
     * 注意，如果参数类型是数组类型，则不受格式影响
     * 格式修饰符
     * s: 使用String.format进行格式化
     * t: 打印的是元素的class的简单类名class.getSimpleName()
     * T: 打印的是元素的class的全限定类名class.getName()
     * e: 当元素为null时，打印空字符串，而不是"null"
     * c: 当元素为Throwable时，打印包含堆栈
     *
     * @param format
     * @param args
     * @return
     */
    public static String format(String format, Object... args) {
        return replace(format, RegexPattens.INDEXED_FORMAT_PLACEHOLDER_REGEX, (s, i) -> {
            String prefix = "";
            if (!s.startsWith("{")) {
                prefix = s.substring(0, 1);
                s = s.substring(1);
            }
            s = s.substring(1, s.length() - 1);
            s = s.replaceAll("\\\\\\{", "{");
            s = s.replaceAll("\\\\}", "}");

            int idx = i;
            char flag = 0;
            String patten = null;
            if (s.contains(":")) {
                String[] arr = s.split(":", 2);
                if (!arr[0].isEmpty()) {
                    char ch = arr[0].charAt(arr[0].length() - 1);
                    if (ch < '0' || ch > '9') {
                        flag = ch;
                        arr[0] = arr[0].substring(0, arr[0].length() - 1);
                    }
                }
                try {
                    idx = Integer.parseInt(arr[0].trim());
                    if (idx < 0) {
                        idx = args.length + idx;
                    }
                } catch (Exception e) {

                }
                if (arr.length > 1) {
                    patten = arr[1];
                    if (patten.isEmpty()) {
                        patten = null;
                    }
                }
            } else {
                if (!s.isEmpty()) {
                    char ch = s.charAt(s.length() - 1);
                    if (ch < '0' || ch > '9') {
                        flag = ch;
                        s = s.substring(0, s.length() - 1);
                    }
                }
                try {
                    idx = Integer.parseInt(s.trim());
                    if (idx < 0) {
                        idx = args.length - 1 + idx;
                    }
                } catch (Exception e) {

                }
            }

            Object val = null;
            if (idx < args.length && idx >= 0) {
                val = args[i];
            } else {
                return prefix + s;
            }
            if (val == null) {
                return prefix + (flag == 'e' ? "" : "null");
            }
            if (flag == 't' || flag == 'T') {
                Class<?> clazz = val.getClass();
                if (flag == 't') {
                    return prefix + clazz.getSimpleName();
                }
                if (flag == 'T') {
                    return prefix + clazz.getName();
                }
            }
            if (val.getClass().isArray()) {
                return prefix + stringify(val, flag == 'e' ? "" : "null");
            }
            if (val instanceof Throwable) {
                if (flag == 'c') {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(bos);
                    ((Throwable) val).printStackTrace(ps);
                    ps.close();
                    byte[] bytes = bos.toByteArray();
                    String stackTrace = new String(bytes);
                    return prefix + String.valueOf(val) + "\n" + stackTrace;
                }
            }
            if (patten == null) {
                return prefix + stringify(val, flag == 'e' ? "" : "null");
            }

            if (flag == 's') {
                return prefix + String.format(patten, val);
            }
            if (val instanceof java.util.Date) {
                SimpleDateFormat fmt = new SimpleDateFormat(patten);
                return prefix + fmt.format(val);
            } else if (val instanceof LocalDateTime) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern(patten);
                return prefix + ((LocalDateTime) val).format(fmt);
            } else if (val instanceof LocalTime) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern(patten);
                return prefix + ((LocalTime) val).format(fmt);
            } else if (val instanceof LocalDate) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern(patten);
                return prefix + ((LocalDate) val).format(fmt);
            }


            return prefix + String.format(patten, val);
        });
    }

    public static String stringify(Object val, String nullAs) {
        if (val == null) {
            return nullAs;
        }
        Class<?> clazz = val.getClass();
        if (clazz.isArray()) {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            int len = Array.getLength(val);
            for (int j = 0; j < len; j++) {
                if (j > 0) {
                    builder.append(", ");
                }
                Object item = Array.get(val, j);
                builder.append(stringify(item, nullAs));
            }
            builder.append("]");
            return builder.toString();
        }
        return val.toString();
    }

    public static String trimComment(String str) {
        return replace(str, RegexPattens.COMMON_COMMENT_REGEX, s -> "");
    }

    public static String replace(String str, String regex, String replacement) {
        return replace(str, regex, (s, i) -> replacement);
    }

    public static String replace(String str, String regex, Function<String, String> replacer) {
        return replace(str, regex, (s, i) -> replacer == null ? s : replacer.apply(s));
    }

    public static String replace(String str, String regex, BiFunction<String, Integer, String> replacer) {
        if (str == null) {
            return null;
        }
        if (regex == null) {
            return str;
        }
        StringBuilder builder = new StringBuilder();
        Pattern patten = Pattern.compile(regex);
        Matcher matcher = patten.matcher(str);
        int cnt = 0;
        int lidx = 0;
        while (matcher.find()) {
            MatchResult result = matcher.toMatchResult();

            String oth = str.substring(lidx, result.start());
            builder.append(oth);

            lidx = result.end();
            String group = matcher.group();
            if (replacer != null) {
                group = replacer.apply(group, cnt);
            }
            builder.append(group);

            cnt++;
        }

        String oth = str.substring(lidx);
        builder.append(oth);

        return builder.toString();
    }
}
