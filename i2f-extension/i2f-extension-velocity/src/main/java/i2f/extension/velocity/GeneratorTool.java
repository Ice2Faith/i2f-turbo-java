package i2f.extension.velocity;

import i2f.io.stream.StreamUtil;
import i2f.os.OsUtil;
import i2f.serialize.str.json.impl.Json2;
import i2f.serialize.str.xml.impl.Xml2;
import i2f.text.StringUtils;
import i2f.typeof.TypeOf;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2022/2/12 8:36
 * @desc
 */
public class GeneratorTool {
    private final String sharp = "#";
    private final String dolar = "$";

    public static final SecureRandom random = new SecureRandom();

    public String getSharp() {
        return sharp;
    }

    public String getDolar() {
        return dolar;
    }

    public String newline() {
        return "\n";
    }

    public static boolean isnull(Object obj) {
        return obj == null;
    }

    public static boolean notNull(Object obj) {
        return !isnull(obj);
    }

    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    public static boolean notEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isArray(Object obj) {
        if (isnull(obj)) {
            return false;
        }
        Class cls = obj.getClass();
        return cls.isArray();
    }

    public static boolean match(String str, String regex) {
        if (isnull(str) || isnull(regex)) {
            return false;
        }
        return str.matches(regex);
    }

    public static String str(Object obj) {
        return String.valueOf(obj);
    }

    public static Object ifnull(Object obj, Object defVal) {
        if (isnull(obj)) {
            return defVal;
        }
        return obj;
    }

    public static String ifEmpty(String obj, String defVal) {
        if (isEmpty(obj)) {
            return defVal;
        }
        return obj;
    }

    public static String lower(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.toLowerCase();
    }

    public static String upper(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.toUpperCase();
    }

    public static String[] split(String str, String sep, int limit) {
        if (isEmpty(str)) {
            return new String[0];
        }
        return str.split(sep, limit);
    }

    /**
     * fmt参数含义：%s 指代循环变量，有且只有一个%s,此参数可以为空
     *
     * @param obj
     * @param fmt
     * @param sep
     * @param open
     * @param close
     * @return
     */
    public static String join(Object obj, String fmt, String sep, String open, String close) {
        StringBuilder builder = new StringBuilder();
        if (open != null) {
            builder.append(open);
        }
        if (obj instanceof Collection) {
            boolean isFirst = true;
            Collection col = (Collection) obj;
            Iterator it = col.iterator();
            while (it.hasNext()) {
                if (!isFirst) {
                    if (sep != null) {
                        builder.append(sep);
                    }
                }
                String val = str(it.next());
                if (isnull(fmt)) {
                    builder.append(val);
                } else {
                    builder.append(String.format(fmt, val));
                }
                isFirst = false;
            }

        } else if (isArray(obj)) {
            boolean isFirst = true;
            for (int i = 0; i < Array.getLength(obj); i++) {
                if (!isFirst) {
                    if (sep != null) {
                        builder.append(sep);
                    }
                }
                String val = str(Array.get(obj, i));
                if (isnull(fmt)) {
                    builder.append(val);
                } else {
                    builder.append(String.format(fmt, val));
                }
                isFirst = false;

            }
        }
        if (close != null) {
            builder.append(close);
        }
        return builder.toString();
    }

    public static String replace(String str, String src, String dst) {
        if (isnull(str) || isnull(src)) {
            return str;
        }
        return str.replace(src, dst);
    }

    public static String replaceAll(String str, String src, String dst) {
        if (isnull(str) || isnull(src)) {
            return str;
        }
        return str.replaceAll(src, dst);
    }

    public static List<Integer> fori(int begin, int end, int step) {
        List<Integer> ret = new ArrayList<>();
        for (int i = begin; i != end; i += step) {
            ret.add(i);
        }
        return ret;
    }

    public static String firstLower(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return StringUtils.firstLower(str);
    }

    public static String firstUpper(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return StringUtils.firstUpper(str);
    }

    public static String format(String fmt, Object... args) {
        if (isEmpty(fmt)) {
            return fmt;
        }
        return String.format(fmt, args);
    }

    public static String format(Date date, String fmt) {
        if (isnull(date) || isEmpty(fmt)) {
            return str(date);
        }
        SimpleDateFormat f = new SimpleDateFormat(fmt);
        return f.format(date);
    }

    public static Date now() {
        return new Date();
    }

    public static Date date(String dt, String fmt) throws ParseException {
        if (isEmpty(dt) || isEmpty(fmt)) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat(fmt);
        return f.parse(dt);
    }

    public static int randInt(int bound) {
        return random.nextInt(bound);
    }

    public static int randInt(int begin, int end) {
        return random.nextInt(end - begin) + begin;
    }

    public static double randDouble() {
        return random.nextDouble();
    }

    public static double randDouble(double min, double max, int scale) {
        double pow = Math.pow(10, scale);
        int bound = (int) ((max - min) * pow);
        return (random.nextInt() % bound) / pow + min;
    }

    public static boolean contains(String str, String sub) {
        if (str == null && sub == null) {
            return true;
        }
        if (sub == null || sub.isEmpty()) {
            return true;
        }
        if (str == null) {
            return false;
        }
        return str.contains(sub);
    }

    public static int round(double a) {
        return (int) Math.round(a);
    }

    public static double add(double a, double b) {
        return a + b;
    }

    public static double sub(double a, double b) {
        return a - b;
    }

    public static double div(double a, double b) {
        return a / b;
    }

    public static double mul(double a, double b) {
        return a * b;
    }

    public static double mod(double a, double b) {
        return ((int) a) % ((int) b);
    }

    public static double pow(double a, double b) {
        return Math.pow(a, b);
    }

    public static double sqrt(double a) {
        return Math.sqrt(a);
    }

    public static double log(double a) {
        return Math.log(a);
    }

    public static boolean equal(Object obj1, Object obj2) {
        if (isnull(obj1) && isnull(obj2)) {
            return true;
        }
        if (isnull(obj1)) {
            return obj2.equals(obj1);
        }
        return obj1.equals(obj2);
    }

    public static String sysProperty(String key) {
        return System.getProperty(key);
    }

    public static String sysEnv(String key) {
        return System.getenv(key);
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String readFile(String filePath, String charset) throws IOException {
        return StreamUtil.readString(new File(filePath), charset);
    }

    public static void writeFile(String filePath, Object content, String charset) throws IOException {
        String os = str(content);
        StreamUtil.writeString(os, charset, new File(filePath));
    }

    public static File[] listFiles(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new File[0];
        }
        if (file.isFile()) {
            return new File[0];
        }
        return file.listFiles();
    }

    public static boolean existFile(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean isFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public static boolean isDir(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isDirectory();
    }

    public static void mkdirs(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void cmd(String cmdline, boolean wait) {
        try {
            Process process = Runtime.getRuntime().exec(cmdline);
            if (wait) {
                process.waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String cmdResult(String cmdLine, String charset) throws IOException, InterruptedException {
        return OsUtil.runCmd(cmdLine, charset);
    }

    public static List<Map<String, Object>> list(Object itr) {
        List<Map<String, Object>> ret = new ArrayList<>();
        if (itr instanceof Collection) {
            Collection col = (Collection) itr;
            int index = 0;
            int size = col.size();
            Iterator it = col.iterator();
            while (it.hasNext()) {
                Object val = it.next();
                Map<String, Object> obj = new HashMap<>();
                obj.put("first", index == 0);
                obj.put("last", index == (size - 1));
                obj.put("index", index);
                obj.put("size", size);
                obj.put("value", val);
                ret.add(obj);
                index++;
            }
        } else if (isArray(itr)) {
            int size = Array.getLength(itr);
            for (int i = 0; i < size; i++) {
                Object val = Array.get(itr, i);
                Map<String, Object> obj = new HashMap<>();
                obj.put("first", i == 0);
                obj.put("last", i == (size - 1));
                obj.put("index", i);
                obj.put("size", size);
                obj.put("value", val);
                ret.add(obj);
            }
        }
        return ret;
    }

    public static boolean instanceOf(Object obj, String typeName) {
        if (isnull(obj) || isEmpty(typeName)) {
            return false;
        }
        Class objCls = obj.getClass();
        Object[][] baseMapping = {
                {"string", String.class},
                {"int", Integer.class},
                {"short", Short.class},
                {"byte", Byte.class},
                {"char", Character.class},
                {"long", Long.class},
                {"float", Float.class},
                {"double", Double.class},
                {"date", Date.class}
        };
        String lowType = typeName.toLowerCase();
        for (Object[] item : baseMapping) {
            if (item[0].equals(lowType)) {
                if (isInTypes(objCls, (Class) item[1])) {
                    return true;
                }
            }
        }
        try {
            Class typeCls = Class.forName(typeName);
            return isInTypes(objCls, typeCls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String toPascal(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return StringUtils.toPascal(str);
    }

    public static String toCamel(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return StringUtils.toCamel(str);
    }

    public static String toUnderScore(String str) {
        if (str.contains("_")) {
            return str.trim();
        }
        return StringUtils.toUnderScore(str);
    }

    public static String toSnake(String str) {
        if (str.contains("-")) {
            return str.trim();
        }
        return StringUtils.toSnake(str);
    }

    public static String toPropertyCase(String str) {
        if (str.contains(".")) {
            return str.trim();
        }
        return StringUtils.toPropertyCase(str);
    }

    public static String toPathCase(String str) {
        if (str.contains("/")) {
            return str.trim();
        }
        return StringUtils.toPathCase(str);
    }

    public static String toColonCase(String str) {
        if (str.contains(":")) {
            return str.trim();
        }
        return StringUtils.toColonCase(str);
    }

    public static String toUrlEncoded(String str) {
        if (isEmpty(str)) {
            return str;
        }
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return str;
    }

    public static String toBase64(String str) {
        if (str == null) {
            return null;
        }
        try {
            return Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }
        return str;
    }

    public static String toXmlString(Object obj) {
        return Xml2.toXmlString(str(obj));
    }

    public static String toJsonString(Object obj) {
        return Json2.toJson(obj);
    }

    public static String[] split(String str, boolean trimBefore, String regex, int limit, boolean removeEmpty) {
        String[] ret = new String[]{};
        if (str == null) {
            return ret;
        }
        if (trimBefore) {
            str = str.trim();
        }
        ret = str.split(regex, limit);
        Vector<String> result = new Vector<>();
        for (String item : ret) {
            if (removeEmpty) {
                if ("".equals(item)) {
                    continue;
                }
            }
            result.add(item);
        }
        ret = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
            ret[i] = result.get(i);
        }
        return ret;
    }

    protected static boolean isInTypes(Class target, Class... types) {
        if (target == null) {
            return false;
        }
        return TypeOf.typeOfAny(target, types);
    }
}
