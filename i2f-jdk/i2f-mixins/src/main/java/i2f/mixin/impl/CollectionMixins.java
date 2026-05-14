package i2f.mixin.impl;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/5/14 9:00
 * @desc
 */
public interface CollectionMixins {
    default <T> List<T> new_list() {
        return new ArrayList<>();
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

    default List<Object> list_of(Object... arr) {
        return new ArrayList<>(Arrays.asList(arr));
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

    default int length(Collection collection) {
        return collection.size();
    }

}
