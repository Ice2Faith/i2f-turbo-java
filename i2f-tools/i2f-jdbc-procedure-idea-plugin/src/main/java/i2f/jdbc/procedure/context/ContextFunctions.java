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

    default String convertOracleRegexExpression(String regex) {
        return null;
    }

    default String convertOracleRegexReplacement(String replacement) {
        return null;
    }

    default void print(Object... objs) {

    }

    default void println(Object... objs) {

    }

    default String sys_env(String key) {
        return null;
    }

    default String jvm(String key) {
        return null;
    }

    default void gc() {

    }

    default void exit(int status) {

    }

    default void yield() {

    }

    default long thread_id() {
        return -1;
    }

    default String thread_name() {
        return null;
    }

    default boolean isnull(Object obj) {
        return false;
    }

    default boolean is_empty(Object obj) {
        return false;
    }

    default boolean is_blank(Object obj) {
        return false;
    }

    default List<Object> list_of(Object... arr) {
        return null;
    }

    default Map<Object, Object> map_of(Object... arr) {
        return null;
    }

    default int index_of(String str, String sstr) {
        return -1;
    }

    default int last_index_of(String str, String sstr) {
        return -1;
    }

    default String replace(String str, String target) {
        return null;
    }

    default String replace(String str, String target, String replacement) {
        return null;
    }

    default String regex_replace(String str, String regex) {
        return null;
    }

    default String regex_replace(String str, String regex, String replacement) {
        return null;
    }

    default String regex_replace(String str, String regex, String replacement, int occurrence) {
        return null;
    }

    default String regexp_replace(String str, String regex) {
        return null;
    }

    default String regexp_replace(String str, String regex, String replacement) {
        return null;
    }

    default String regexp_replace(String str, String regex, String replacement, int occurrence) {
        return null;
    }

    default boolean regex_like(String str, String regex) {
        return false;
    }

    default boolean regexp_like(String str, String regex) {
        return false;
    }

    default List<String> regex_find(String str, String regex) {
        return null;
    }

    default boolean regex_contains(String str, String regex) {
        return false;
    }

    default boolean regexp_contains(String str, String regex) {
        return false;
    }

    default int regex_index(String str, String regex) {
        return -1;
    }

    default int regexp_index(String str, String regex) {
        return -1;
    }

    default String regex_extra(String str, String regex) {
        return null;
    }

    default String regexp_extra(String str, String regex) {
        return null;
    }

    default String regex_find_join(String str, String regex) {
        return null;
    }

    default String regex_find_join(String str, String regex, Object separator) {
        return null;
    }

    default String to_camel(String str) {
        return null;
    }

    default String to_pascal(String str) {
        return null;
    }

    default String to_underscore(String str) {
        return null;
    }

    default String to_snake(String str) {
        return null;
    }

    default String join(Object obj) {
        return null;
    }

    default String join(Object obj, Object separator) {
        return null;
    }

    default String join(Object obj, Object separator, boolean ignoreNull) {
        return null;
    }

    default String join(Object obj, Object separator, boolean ignoreNull, boolean ignoreEmpty) {
        return null;
    }

    default String trim(String str) {
        return null;
    }

    default String upper(String str) {
        return null;
    }

    default String lower(String str) {
        return null;
    }

    default String chr(int ascii) {
        return null;
    }

    default int char_code(String str, int index) {
        return -1;
    }

    default String rtrim(String str) {
        return null;
    }

    default String rtrim(String str, String substr) {
        return null;
    }

    default int length(Object obj) {
        return -1;
    }

    default int lengthb(Object obj) {
        return -1;
    }

    default String ltrim(String str) {
        return null;
    }

    default String ltrim(String str, String substr) {
        return null;
    }

    default String lpad(Object str, int len) {
        return null;
    }

    default String lpad(Object str, int len, Object padStr) {
        return null;
    }

    default String rpad(Object str, int len) {
        return null;
    }

    default String rpad(Object str, int len, Object padStr) {
        return null;
    }

    default Object nvl(Object v1, Object v2) {
        return null;
    }

    default Object ifnull(Object v1, Object v2) {
        return null;
    }

    default Object if_empty(Object v1, Object v2) {
        return null;
    }

    default Object evl(Object v1, Object v2) {
        return null;
    }

    default Object if_blank(Object v1, Object v2) {
        return null;
    }

    default Object bvl(Object v1, Object v2) {
        return null;
    }

    default Object if2(Object cond, Object trueVal, Object falseVal) {
        return null;
    }

    default Object nvl2(Object cond, Object trueVal, Object falseVal) {
        return null;
    }

    default Object decode(Object target, Object... args) {
        return null;
    }

    default Object cast(Object val, Object type) {
        return null;
    }

    default Object convert(Object val, Object type) {
        return null;
    }

    default ChronoUnit chrono_unit(String unit) {
        return null;
    }

    default Object date_sub(Object date, String unit, long interval) {
        return null;
    }

    default Object date_add(Object date, String unit, long interval) {
        return null;
    }

    default Date to_date(Object obj) {
        return null;
    }

    default Date to_date(Object obj, String pattern) {
        return null;
    }

    default String date_format(Object date, String pattern) {
        return null;
    }

    default Object last_day(Object date) {
        return null;
    }

    default String to_char(Object obj) {
        return null;
    }

    default String to_char(Object obj, String pattern) {
        return null;
    }

    default String to_string(Object obj) {
        return null;
    }

    default String to_string(Object obj, String pattern) {
        return null;
    }

    default String left(Object obj, int len) {
        return null;
    }

    default String right(Object obj, int len) {
        return null;
    }

    default String uuid() {
        return null;
    }

    default long snowflake_id() {
        return -1;
    }

    default Date sysdate() {
        return null;
    }

    default Date now() {
        return null;
    }

    default long timestamp() {
        return -1;
    }

    default Date timestamp_to_date(Object ts) {
        return null;
    }

    default Long date_to_timestamp(Object date) {
        return null;
    }

    default String reverse(Object str) {
        return null;
    }

    default Object add_months(Object date, long interval) {
        return null;
    }

    default Object add_days(Object date, long interval) {
        return null;
    }

    default Object trunc(Object obj) {
        return null;
    }

    default Object trunc(Object date, String format) {
        return null;
    }

    default Object trunc(Object number, Integer precision) {
        return null;
    }

    default Object round(Object number) {
        return null;
    }

    default Object round(Object number, Integer precision) {
        return null;
    }

    default String concat(Object... args) {
        return null;
    }

    default String concat(Iterable<?> args) {
        return null;
    }

    default String join(Object separator, Object... args) {
        return null;
    }

    default String join(Object separator, Iterable<?> args) {
        return null;
    }

    default Object first_day(Object date) {
        return null;
    }

    default int day(Object date) {
        return -1;
    }

    default int month(Object date) {
        return -1;
    }

    default int year(Object date) {
        return -1;
    }

    default int hour(Object date) {
        return -1;
    }

    default int minute(Object date) {
        return -1;
    }

    default int second(Object date) {
        return -1;
    }

    default int week(Object date) {
        return -1;
    }

    default int extract(String fmt, Object date) {
        return -1;
    }

    default BigDecimal to_number(Object obj) {
        return null;
    }

    default Integer to_int(Object obj) {
        return null;
    }

    default Long to_long(Object obj) {
        return null;
    }

    default boolean to_boolean(Object obj) {
        return false;
    }

    default void sleep(long seconds) {
    }

    default int instr(Object obj, Object sub) {
        return -1;
    }

    default String substr(Object obj, int index) {
        return null;
    }

    default String substr(Object obj, int index, int len) {
        return null;
    }

    default String substrb(Object str, int index) {
        return null;
    }

    default String substrb(Object str, int index, int len) {
        return null;
    }

    default String substr_index(Object obj, String substr, int len) {
        return null;
    }

    default ArrayList<String> splitRegex(String str, String regex) {
        return null;
    }

    default ArrayList<String> splitRegex(String str, String regex, int limit) {
        return null;
    }

    default ArrayList<String> splitLiteral(String str, String regex) {
        return null;
    }

    default ArrayList<String> splitLiteral(String str, String regex, int limit) {
        return null;
    }

    default boolean contains(Object obj, Object substr) {
        return false;
    }

    default boolean like(Object obj, Object substr) {
        return false;
    }

    default boolean ends(Object obj, Object substr) {
        return false;
    }

    default boolean starts_with(Object obj, Object substr) {
        return false;
    }

    default boolean starts(Object obj, String substr) {
        return false;
    }

    default boolean ends_with(Object obj, Object substr) {
        return false;
    }

    default Object neg(Object number) {
        return null;
    }

    default Object abs(Object number) {
        return null;
    }

    default Object ln(Object number) {
        return null;
    }

    default Object add(Object number1, Object number2) {
        return null;
    }

    default Object sub(Object number1, Object number2) {
        return null;
    }

    default Object mul(Object number1, Object number2) {
        return null;
    }

    default Object div(Object number1, Object number2) {
        return null;
    }

    default Object mod(Object number1, Object number2) {
        return null;
    }

    default Object pow(Object number1, Object number2) {
        return null;
    }

    default Object sin(Object number1) {
        return null;
    }

    default Object cos(Object number1) {
        return null;
    }

    default Object tan(Object number1) {
        return null;
    }

    default Object asin(Object number1) {
        return null;
    }

    default Object acos(Object number1) {
        return null;
    }

    default Object atan(Object number1) {
        return null;
    }

    default Object sqrt(Object number1) {
        return null;
    }

    default String encode_url(Object obj) {
        return null;
    }

    default String decode_url(Object obj) {
        return null;
    }

    default String encode_base64(Object obj) {
        return null;
    }

    default String decode_base64(Object obj) {
        return null;
    }

    default String md5(Object data) {
        return null;
    }

    default String sha1(Object data) {
        return null;
    }

    default String sha256(Object data) {
        return null;
    }

    default String sha384(Object data) {
        return null;
    }

    default String sha512(Object data) {
        return null;
    }

    default String mds(String algorithm, Object data) {
        return null;
    }

    default String mds(String algorithm, Object data, String format) {
        return null;
    }

    default String mds(String algorithm, Object data, String format, String provider) {
        return null;
    }
}
