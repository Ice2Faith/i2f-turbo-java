package i2f.extension.antlr4.script.tiny.impl.context;

import java.io.File;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/2/25 20:52
 * @desc
 */
public interface TinyScriptFunctions {
    long current_time_millis();

    long current_time_seconds();

    int rand();

    int rand(int bound);

    int rand(int min, int max);

    double random();

    String format(String format, Object... obj);

    void exec(String command);

    void exec(String... commandArray);

    void system(String command);

    void system(String... commandArray);

    Object new_array(Class<?> elemType, int len);

    <T> List<T> new_list();

    <K, V> Map<K, V> new_map();

    <T> Set<T> new_set();

    Collection append(Collection collection, Object... objs);

    Appendable append(Appendable appendable, Object... objs);

    void put(Map map, Object key, Object value);

    Object map_get(Map map, Object key);

    boolean map_contains(Map map, Object key);

    Object map_remove(Map map, Object key);

    Object list_get(List list, int index);

    boolean collection_contains(Collection collection, Object elem);

    boolean collection_remove(Collection collection, Object elem);

    Object list_remove(List list, int index);

    Object visit_get(Object obj, String expression);

    void visit_set(Object obj, String expression, Object value);

    void visit_del(Object obj, String expression);

    boolean iterator_has(Iterator iterator);

    Object iterator_next(Iterator iterator);

    boolean enumeration_has(Enumeration enumeration);

    Object enumeration_next(Enumeration enumeration);

    void clear(Collection collection);

    void clear(Map map);

    Map<String, Object> local_map();

    Object local_get(String key);

    void local_set(String key, Object value);

    void local_remove(String key);

    boolean local_contains(String key);

    void local_reset();

    void local_clear();

    int length(Collection collection);

    int length(Map map);

    int length(CharSequence sequence);

    long length(File file);

    long hashcode(Object obj);

    void sleep_ms(long millis);

    int compare(Object v1, Object v2);

    boolean cmp_eq(Object v1, Object v2);

    boolean cmp_neq(Object v1, Object v2);

    boolean cmp_gt(Object v1, Object v2);

    boolean cmp_lt(Object v1, Object v2);

    boolean cmp_gte(Object v1, Object v2);

    boolean cmp_lte(Object v1, Object v2);

    File file(String path);

    boolean is_file(File file);

    boolean file_exists(String path);

    boolean file_exists(File file);

    boolean is_file(String path);

    boolean is_dir(File file);

    boolean is_dir(String path);

    List<File> list_file(String path);

    List<File> list_file(File file);

    boolean is_array(Object arr);

    int arr_len(Object arr);

    <T> T arr_get(Object arr, int index);

    void arr_set(Object arr, int index, Object value);

    <T> List<T> arr_to_list(Object arr, Class<T> elemType, int index, int len);

    <T> List<T> arr_to_list(Object arr, int index, int len);

    <T> List<T> arr_to_list(Object arr, Class<T> elemType, int index);

    <T> List<T> arr_to_list(Object arr, int index);

    <T> List<T> arr_to_list(Object arr, Class<T> elemType);

    <T> List<T> arr_to_list(Object arr);

    <T> T[] list_to_array(Iterable<?> collection, Class<T> elemType, int index, int len);

    <T> T[] list_to_array(Iterable<?> collection, int index, int len);

    <T> T[] list_to_array(Iterable<?> collection, Class<T> elemType, int index);

    <T> T[] list_to_array(Iterable<?> collection, int index);

    <T> T[] list_to_array(Iterable<?> collection, Class<T> elemType);

    <T> T[] list_to_array(Iterable<?> collection);
}
