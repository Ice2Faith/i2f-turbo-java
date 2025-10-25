package i2f.extension.antlr4.script.tiny.impl.context;

import i2f.convert.obj.ObjectConvertor;

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

    default Date now() {
        return new Date();
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

    default String uuid() {
        return UUID.randomUUID().toString();
    }

    default void println(Object... obj) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object item : obj) {
            if (!isFirst) {
                builder.append(", ");
            }
            builder.append(item);
            isFirst = false;
        }
        System.out.println(builder.toString());
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

    default long length(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (is_array(obj)) {
            return arr_len(obj);
        } else if (obj instanceof Collection) {
            return length((Collection) obj);
        } else if (obj instanceof Map) {
            return length((Map) obj);
        } else if (obj instanceof CharSequence) {
            return length((CharSequence) obj);
        } else if (obj instanceof File) {
            return length((File) obj);
        }
        return 1;
    }

    default long hashcode(Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    default void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    default void yield() {
        Thread.yield();
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
