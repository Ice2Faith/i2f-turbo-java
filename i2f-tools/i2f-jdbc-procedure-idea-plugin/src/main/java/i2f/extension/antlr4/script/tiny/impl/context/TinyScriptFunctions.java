package i2f.extension.antlr4.script.tiny.impl.context;

import java.io.File;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/2/25 20:52
 * @desc
 */
public interface TinyScriptFunctions {
    Date now();

    int rand();

    int rand(int bound);

    int rand(int min, int max);

    double random();

    String uuid();

    void println(Object... obj);

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

    int length(Collection collection);

    int length(Map map);

    int length(CharSequence sequence);

    long length(File file);

    long length(Object obj);

    long hashcode(Object obj);

    void sleep(long millis);

    void yield();

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
