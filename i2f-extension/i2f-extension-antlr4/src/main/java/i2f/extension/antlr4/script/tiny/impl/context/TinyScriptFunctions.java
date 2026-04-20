package i2f.extension.antlr4.script.tiny.impl.context;

import i2f.convert.obj.ObjectConvertor;
import i2f.extension.antlr4.script.tiny.impl.DefaultTinyScriptResolver;
import i2f.reflect.vistor.Visitor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/2/25 20:52
 * @desc
 */
public interface TinyScriptFunctions {
    TinyScriptFunctions INSTANCE = new TinyScriptFunctions() {
    };

    SecureRandom RANDOM = new SecureRandom();

    ThreadLocal<Map<String, Object>> LOCAL = ThreadLocal.withInitial(HashMap::new);

    default long current_time_millis() {
        return System.currentTimeMillis();
    }

    default long current_time_seconds() {
        return System.currentTimeMillis() / 1000;
    }

    default int rand() {
        return RANDOM.nextInt();
    }

    default int rand(int bound) {
        return RANDOM.nextInt(bound);
    }

    default int rand(int min, int max) {
        return RANDOM.nextInt(max - min) + min;
    }

    default double random() {
        return RANDOM.nextDouble();
    }

    default String format(String format, Object... obj) {
        return String.format(format, obj);
    }

    default void exec(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void exec(String... commandArray) {
        try {
            Runtime.getRuntime().exec(commandArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void system(String command) {
        exec(command);
    }

    default void system(String... commandArray) {
        exec(commandArray);
    }

    default Object new_array(Class<?> elemType, int len) {
        return Array.newInstance(elemType, len);
    }

    default <T> List<T> new_list() {
        return new ArrayList<>();
    }

    default <K, V> Map<K, V> new_map() {
        return new HashMap<>();
    }

    default <T> Set<T> new_set() {
        return new HashSet<>();
    }

    default Collection append(Collection collection, Object... objs) {
        for (Object o : objs) {
            collection.add(o);
        }
        return collection;
    }

    default Appendable append(Appendable appendable, Object... objs) {
        for (Object o : objs) {
            try {
                appendable.append(String.valueOf(o));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return appendable;
    }

    default void put(Map map, Object key, Object value) {
        map.put(key, value);
    }

    default Object map_get(Map map, Object key) {
        return map.get(key);
    }

    default boolean map_contains(Map map, Object key) {
        return map.containsKey(key);
    }

    default Object map_remove(Map map, Object key) {
        return map.remove(key);
    }

    default Object list_get(List list, int index) {
        return list.get(index);
    }

    default boolean collection_contains(Collection collection, Object elem) {
        return collection.contains(elem);
    }

    default boolean collection_remove(Collection collection, Object elem) {
        return collection.remove(elem);
    }

    default Object list_remove(List list, int index) {
        return list.remove(index);
    }

    default Object visit_get(Object obj, String expression) {
        return Visitor.visit(expression, obj).get();
    }

    default void visit_set(Object obj, String expression, Object value) {
        Visitor.visit(expression, obj).set(value);
    }

    default void visit_del(Object obj, String expression) {
        Visitor.visit(expression, obj).delete();
    }

    default boolean iterator_has(Iterator iterator) {
        return iterator.hasNext();
    }

    default Object iterator_next(Iterator iterator) {
        return iterator.next();
    }

    default boolean enumeration_has(Enumeration enumeration) {
        return enumeration.hasMoreElements();
    }

    default Object enumeration_next(Enumeration enumeration) {
        return enumeration.nextElement();
    }

    default void clear(Collection collection) {
        if (collection == null) {
            return;
        }
        collection.clear();
    }

    default void clear(Map map) {
        if (map == null) {
            return;
        }
        map.clear();
    }

    default Map<String, Object> local_map() {
        Map<String, Object> map = LOCAL.get();
        if (map == null) {
            map = new HashMap<>();
            LOCAL.set(map);
        }
        return map;
    }

    default Object local_get(String key) {
        return local_map().get(key);
    }

    default void local_set(String key, Object value) {
        local_map().put(key, value);
    }

    default void local_remove(String key) {
        local_map().remove(key);
    }

    default boolean local_contains(String key) {
        return local_map().containsKey(key);
    }

    default void local_reset() {
        LOCAL.set(new HashMap<>());
    }

    default void local_clear() {
        local_map().clear();
    }

    default int length(Collection collection) {
        return collection.size();
    }

    default int length(Map map) {
        return map.size();
    }

    default int length(CharSequence sequence) {
        return sequence.length();
    }

    default long length(File file) {
        return file.length();
    }

    default long hashcode(Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    default void sleep_ms(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    default int compare(Object v1, Object v2) {
        return DefaultTinyScriptResolver.compare(v1, v2);
    }

    default boolean cmp_eq(Object v1, Object v2) {
        return compare(v1, v2) == 0;
    }

    default boolean cmp_neq(Object v1, Object v2) {
        return compare(v1, v2) != 0;
    }

    default boolean cmp_gt(Object v1, Object v2) {
        return compare(v1, v2) > 0;
    }

    default boolean cmp_lt(Object v1, Object v2) {
        return compare(v1, v2) < 0;
    }

    default boolean cmp_gte(Object v1, Object v2) {
        return compare(v1, v2) >= 0;
    }

    default boolean cmp_lte(Object v1, Object v2) {
        return compare(v1, v2) <= 0;
    }

    default File file(String path) {
        return new File(path);
    }

    default boolean is_file(File file) {
        if (file == null) {
            return false;
        }
        return file.isFile();
    }

    default boolean file_exists(String path) {
        return file_exists(file(path));
    }

    default boolean file_exists(File file) {
        if (file == null) {
            return false;
        }
        return file.exists();
    }

    default boolean is_file(String path) {
        return is_file(file(path));
    }

    default boolean is_dir(File file) {
        if (file == null) {
            return false;
        }
        return file.isDirectory();
    }

    default boolean is_dir(String path) {
        return is_dir(file(path));
    }

    default List<File> list_file(String path) {
        return list_file(file(path));
    }

    default List<File> list_file(File file) {
        List<File> ret = new ArrayList<>();
        if (file == null) {
            return ret;
        }
        File[] files = file.listFiles();
        if (files == null) {
            return ret;
        }
        ret.addAll(Arrays.asList(files));
        return ret;
    }

    default boolean is_array(Object arr) {
        if (arr == null) {
            return false;
        }
        return arr.getClass().isArray();
    }

    default int arr_len(Object arr) {
        if (!is_array(arr)) {
            throw new IllegalArgumentException("expect array type,but found:" + (arr == null ? arr : arr.getClass()));
        }
        return Array.getLength(arr);
    }

    default <T> T arr_get(Object arr, int index) {
        if (!is_array(arr)) {
            throw new IllegalArgumentException("expect array type,but found:" + (arr == null ? arr : arr.getClass()));
        }
        int len = arr_len(arr);
        if (index < 0) {
            index = len + index;
        }
        if (index < 0 || index >= len) {
            throw new IndexOutOfBoundsException("array length is:" + len + " , but require index :" + index);
        }
        return (T) Array.get(arr, index);
    }

    default void arr_set(Object arr, int index, Object value) {
        if (!is_array(arr)) {
            throw new IllegalArgumentException("expect array type,but found:" + (arr == null ? arr : arr.getClass()));
        }
        int len = arr_len(arr);
        if (index < 0) {
            index = len + index;
        }
        if (index < 0 || index >= len) {
            throw new IndexOutOfBoundsException("array length is:" + len + " , but require index :" + index);
        }
        Array.set(arr, index, value);
    }

    default <T> List<T> arr_to_list(Object arr, Class<T> elemType, int index, int len) {
        if (!is_array(arr)) {
            throw new IllegalArgumentException("expect array type,but found:" + (arr == null ? arr : arr.getClass()));
        }
        int maxLen = arr_len(arr);
        List<T> ret = new ArrayList<T>();
        for (int i = index, j = 0; i < maxLen; i++, j++) {
            if (len >= 0 && j >= len) {
                break;
            }
            Object obj = arr_get(arr, i);
            T item = null;
            if (elemType != null) {
                item = (T) ObjectConvertor.tryConvertAsType(obj, elemType);
            } else {
                item = (T) obj;
            }
            ret.add(item);
        }
        return ret;
    }

    default <T> List<T> arr_to_list(Object arr, int index, int len) {
        return arr_to_list(arr, null, index, len);
    }

    default <T> List<T> arr_to_list(Object arr, Class<T> elemType, int index) {
        return arr_to_list(arr, elemType, index, -1);
    }

    default <T> List<T> arr_to_list(Object arr, int index) {
        return arr_to_list(arr, null, index, -1);
    }

    default <T> List<T> arr_to_list(Object arr, Class<T> elemType) {
        return arr_to_list(arr, elemType, 0, -1);
    }

    default <T> List<T> arr_to_list(Object arr) {
        return arr_to_list(arr, null, 0, -1);
    }

    default <T> T[] list_to_array(Iterable<?> collection, Class<T> elemType, int index, int len) {
        Iterator<?> iterator = collection.iterator();
        int i = 0;
        int j = 0;
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            if (i >= index) {
                if (len >= 0 && j >= len) {
                    break;
                }
                Object obj = iterator.next();
                T item = null;
                if (elemType != null) {
                    item = (T) ObjectConvertor.tryConvertAsType(obj, elemType);
                } else {
                    item = (T) obj;
                }
                list.add(item);
                j++;
            }
            i++;
        }
        Object ret = Array.newInstance(elemType, list.size());
        j = 0;
        for (T item : list) {
            arr_set(ret, j, item);
            j++;
        }
        return (T[]) ret;
    }

    default <T> T[] list_to_array(Iterable<?> collection, int index, int len) {
        return list_to_array(collection, null, index, -1);
    }

    default <T> T[] list_to_array(Iterable<?> collection, Class<T> elemType, int index) {
        return list_to_array(collection, elemType, index, -1);
    }

    default <T> T[] list_to_array(Iterable<?> collection, int index) {
        return list_to_array(collection, null, index, -1);
    }

    default <T> T[] list_to_array(Iterable<?> collection, Class<T> elemType) {
        return list_to_array(collection, elemType, 0, -1);
    }

    default <T> T[] list_to_array(Iterable<?> collection) {
        return list_to_array(collection, null, 0, -1);
    }
}
