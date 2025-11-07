package i2f.jdbc.procedure.context;


import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/17 16:09
 */
public interface ContextFunctions {
    String convertOracleRegexExpression(String regex);

    String convertOracleRegexReplacement(String replacement);

    void print(Object... objs);

    void println(Object... objs);

    String sys_env(String key);

    String jvm(String key);

    void gc();

    void exit(int status);

    void yield();

    long thread_id();

    String thread_name();

    boolean isnull(Object obj);

    boolean is_empty(Object obj);

    boolean is_blank(Object obj);

    List<Object> list_of(Object... arr);

    Map<Object, Object> map_of(Object... arr);

    int index_of(Object oStr, Object oSstr);

    int last_index_of(Object oStr, Object oSstr);

    String replace(Object str, Object target);

    String replace(Object oStr, Object oTarget, Object oReplacement);

    String regex_replace(Object str, String regex);

    String regex_replace(Object str, String regex, Object replacement);

    String regex_replace(Object oStr, String regex, Object oReplacement, int occurrence);

    String regexp_replace(Object str, String regex);

    String regexp_replace(Object str, String regex, Object replacement);

    String regexp_replace(Object str, String regex, Object replacement, int occurrence);

    boolean regex_like(Object oStr, String regex);

    boolean regexp_like(Object str, String regex);

    List<String> regex_find(Object oStr, String regex);

    boolean regex_contains(Object oStr, String regex);

    boolean regexp_contains(Object str, String regex);

    int regex_index(Object oStr, String regex);

    int regexp_index(Object str, String regex);

    int regex_index_end(Object oStr, String regex);

    int regexp_index_end(Object str, String regex);

    String regex_extra(Object oStr, String regex);

    String regexp_extra(Object str, String regex);

    String regex_find_join(Object str, String regex);

    String regex_find_join(Object str, String regex, Object separator);

    String to_camel(String str);

    String to_pascal(String str);

    String to_underscore(String str);

    String to_snake(String str);

    String join(Object obj);

    String join(Object obj, Object separator);

    String join(Object obj, Object separator, boolean ignoreNull);

    String join(Object obj, Object separator, boolean ignoreNull, boolean ignoreEmpty);

    String trim(String str);

    String upper(String str);

    String lower(String str);

    String chr(int ascii);

    int char_code(String str, int index);

    String rtrim(String str);

    String rtrim(String str, String substr);

    int length(Object obj);

    int lengthb(Object obj);

    String ltrim(String str);

    String ltrim(String str, String substr);

    String lpad(Object str, int len);

    String lpad(Object str, int len, Object padStr);

    String rpad(Object str, int len);

    String rpad(Object str, int len, Object padStr);

    Object nvl(Object v1, Object v2);

    Object ifnull(Object v1, Object v2);

    Object if_empty(Object v1, Object v2);

    Object evl(Object v1, Object v2);

    Object if_blank(Object v1, Object v2);

    Object bvl(Object v1, Object v2);

    Object if2(Object cond, Object trueVal, Object falseVal);

    Object nvl2(Object cond, Object trueVal, Object falseVal);

    Object decode(Object target, Object... args);

    Object cast(Object val, Object type);

    Object convert(Object val, Object type);

    ChronoUnit chrono_unit(String unit);

    Object date_sub(Object date, String unit, long interval);

    Object date_add(Object date, String unit, long interval);

    Date to_date(Object obj);

    Date to_date(Object obj, String pattern);

    String date_format(Object date, String pattern);

    Object last_day(Object date);

    String to_char(Object obj);

    String to_char(Object obj, String pattern);

    String to_string(Object obj);

    String to_string(Object obj, String pattern);

    String left(Object obj, int len);

    String right(Object obj, int len);

    String uuid();

    long snowflake_id();

    Date sysdate();

    Date now();

    long timestamp();

    Date timestamp_to_date(Object ts);

    Long date_to_timestamp(Object date);

    String reverse(Object str);

    Object add_months(Object date, long interval);

    Object add_days(Object date, long interval);

    Object trunc(Object obj);

    Object trunc(Object date, String format);

    Object trunc(Object number, Integer precision);

    Object round(Object number);

    Object round(Object number, Integer precision);

    String concat(Object... args);

    String concat(Iterable<?> args);

    String join(Object separator, Object... args);

    String join(Object separator, Iterable<?> args);

    Object first_day(Object date);

    int day(Object date);

    int month(Object date);

    int year(Object date);

    int hour(Object date);

    int minute(Object date);

    int second(Object date);

    int week(Object date);

    int extract(String fmt, Object date);

    BigDecimal to_number(Object obj);

    Integer to_int(Object obj);

    Long to_long(Object obj);

    boolean to_boolean(Object obj);

    void sleep(long seconds);

    int instr(Object obj, Object sub);

    String substr(Object obj, int index);

    String substr(Object obj, int index, int len);

    String substrb(Object str, int index);

    String substrb(Object str, int index, int len);

    String substr_index(Object obj, Object substr, int len);

    String substr_index(Object obj, Object substr);

    String substr_index_end(Object obj, Object substr, int len);

    String substr_index_end(Object obj, Object substr);

    String substr_regex_index(Object obj, String substr, int len);

    String substr_regex_index(Object obj, String substr);

    String substr_regex_index_end(Object obj, String substr, int len);

    String substr_regex_index_end(Object obj, String substr);

    ArrayList<String> regex_split(Object str, String regex);

    ArrayList<String> regex_split(Object oStr, String regex, int limit);

    ArrayList<String> split_literal(Object str, Object literal);

    ArrayList<String> split(Object str, Object literal);

    ArrayList<String> split_literal(Object oStr, Object oLiteral, int limit);

    ArrayList<String> split(Object str, Object literal, int limit);

    boolean contains(Object obj, Object substr);

    boolean like(Object obj, Object substr);

    boolean ends(Object obj, Object substr);

    boolean starts_with(Object obj, Object substr);

    boolean starts(Object obj, String substr);

    boolean ends_with(Object obj, Object substr);

    Object neg(Object number);

    Object abs(Object number);

    Object ln(Object number);

    Object add(Object number1, Object number2);

    Object sub(Object number1, Object number2);

    Object mul(Object number1, Object number2);

    Object div(Object number1, Object number2);

    Object mod(Object number1, Object number2);

    Object pow(Object number1, Object number2);

    Object sin(Object number1);

    Object cos(Object number1);

    Object tan(Object number1);

    Object asin(Object number1);

    Object acos(Object number1);

    Object atan(Object number1);

    Object sqrt(Object number1);

    String encode_url(Object obj);

    String decode_url(Object obj);

    String encode_base64(Object obj);

    String decode_base64(Object obj);

    String md5(Object data);

    String sha1(Object data);

    String sha256(Object data);

    String sha384(Object data);

    String sha512(Object data);

    String mds(String algorithm, Object data);

    String mds(String algorithm, Object data, String format);

    String mds(String algorithm, Object data, String format, String provider);
}
