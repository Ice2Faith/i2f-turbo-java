package i2f.mixin.impl;


import i2f.convert.obj.ObjectConvertor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/5/14 9:00
 * @desc
 */
public interface ArrayMixins {
    default Object new_array(Class<?> elemType, int len) {
        return Array.newInstance(elemType, len);
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
