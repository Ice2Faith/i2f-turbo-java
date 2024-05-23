package i2f.container.builder;

import i2f.container.builder.collection.CollectionBuilder;
import i2f.container.builder.collection.ListBuilder;
import i2f.container.builder.map.MapBuilder;
import i2f.container.builder.obj.ObjectBuilder;
import i2f.typeof.token.TypeToken;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/4/24 14:20
 * @desc
 */
public class Builders {

    public static <T> ObjectBuilder<T> obj(T obj) {
        return new ObjectBuilder<>(obj);
    }

    public static <T> ObjectBuilder<T> obj(T obj, Class<T> type) {
        return new ObjectBuilder<>(obj, type);
    }

    public static <T> ObjectBuilder<T> obj(T obj, TypeToken<T> type) {
        return new ObjectBuilder<>(obj, type);
    }

    public static <T> ObjectBuilder<T> newObj(Supplier<T> supplier) {
        return new ObjectBuilder<>(supplier.get());
    }

    public static <T> ObjectBuilder<T> newObj(Supplier<T> supplier, Class<T> type) {
        return new ObjectBuilder<>(supplier.get(), type);
    }

    public static <T> ObjectBuilder<T> newObj(Supplier<T> supplier, TypeToken<T> type) {
        return new ObjectBuilder<>(supplier.get(), type);
    }

    public static <K, V, M extends Map<K, V>> MapBuilder<K, V, M> map(M map) {
        return new MapBuilder<>(map);
    }

    public static <K, V, M extends Map<K, V>> MapBuilder<K, V, M> map(M map, Class<K> keyType, Class<V> valueType) {
        return new MapBuilder<>(map, keyType, valueType);
    }

    public static <K, V, M extends Map<K, V>> MapBuilder<K, V, M> map(M map, TypeToken<K> keyType, TypeToken<V> valueType) {
        return new MapBuilder<>(map, keyType, valueType);
    }

    public static <K, V, M extends Map<K, V>> MapBuilder<K, V, M> newMap(Supplier<M> supplier) {
        return new MapBuilder<>(supplier.get());
    }

    public static <K, V, M extends Map<K, V>> MapBuilder<K, V, M> newMap(Supplier<M> supplier, Class<K> keyType, Class<V> valueType) {
        return new MapBuilder<>(supplier.get(), keyType, valueType);
    }

    public static <K, V, M extends Map<K, V>> MapBuilder<K, V, M> newMap(Supplier<M> supplier, TypeToken<K> keyType, TypeToken<V> valueType) {
        return new MapBuilder<>(supplier.get(), keyType, valueType);
    }

    public static <K, V> MapBuilder<K, V, HashMap<K, V>> newHashMap(Class<K> keyType, Class<V> valueType) {
        return newMap(HashMap::new, keyType, valueType);
    }

    public static <K, V> MapBuilder<K, V, HashMap<K, V>> newHashMap(TypeToken<K> keyType, TypeToken<V> valueType) {
        return newMap(HashMap::new, keyType, valueType);
    }

    public static <K, V> MapBuilder<K, V, LinkedHashMap<K, V>> newLinkedHashMap(Class<K> keyType, Class<V> valueType) {
        return newMap(LinkedHashMap::new, keyType, valueType);
    }

    public static <K, V> MapBuilder<K, V, LinkedHashMap<K, V>> newLinkedHashMap(TypeToken<K> keyType, TypeToken<V> valueType) {
        return newMap(LinkedHashMap::new, keyType, valueType);
    }

    public static <K, V> MapBuilder<K, V, ConcurrentHashMap<K, V>> newConcurrentHashMap(Class<K> keyType, Class<V> valueType) {
        return newMap(ConcurrentHashMap::new, keyType, valueType);
    }

    public static <K, V> MapBuilder<K, V, ConcurrentHashMap<K, V>> newConcurrentHashMap(TypeToken<K> keyType, TypeToken<V> valueType) {
        return newMap(ConcurrentHashMap::new, keyType, valueType);
    }


    public static <E, C extends Collection<E>> CollectionBuilder<E, C> collection(C col) {
        return new CollectionBuilder<>(col);
    }

    public static <E, C extends Collection<E>> CollectionBuilder<E, C> collection(C col, Class<E> elementType) {
        return new CollectionBuilder<>(col, elementType);
    }

    public static <E, C extends Collection<E>> CollectionBuilder<E, C> collection(C col, TypeToken<E> elementType) {
        return new CollectionBuilder<>(col, elementType);
    }

    public static <E, C extends Collection<E>> CollectionBuilder<E, C> newCollection(Supplier<C> supplier) {
        return new CollectionBuilder<>(supplier.get());
    }

    public static <E, C extends Collection<E>> CollectionBuilder<E, C> newCollection(Supplier<C> supplier, Class<E> elementType) {
        return new CollectionBuilder<>(supplier.get(), elementType);
    }

    public static <E, C extends Collection<E>> CollectionBuilder<E, C> newCollection(Supplier<C> supplier, TypeToken<E> elementType) {
        return new CollectionBuilder<>(supplier.get(), elementType);
    }

    public static <E> CollectionBuilder<E, HashSet<E>> newHashSet(Class<E> elementType) {
        return newCollection(HashSet::new, elementType);
    }

    public static <E> CollectionBuilder<E, HashSet<E>> newHashSet(TypeToken<E> elementType) {
        return newCollection(HashSet::new, elementType);
    }


    public static <E> CollectionBuilder<E, LinkedHashSet<E>> newLinkedHashSet(Class<E> elementType) {
        return newCollection(LinkedHashSet::new, elementType);
    }

    public static <E> CollectionBuilder<E, LinkedHashSet<E>> newLinkedHashSet(TypeToken<E> elementType) {
        return newCollection(LinkedHashSet::new, elementType);
    }

    public static <E> CollectionBuilder<E, CopyOnWriteArraySet<E>> newCopyOnWriteArraySet(Class<E> elementType) {
        return newCollection(CopyOnWriteArraySet::new, elementType);
    }

    public static <E> CollectionBuilder<E, CopyOnWriteArraySet<E>> newCopyOnWriteArraySet(TypeToken<E> elementType) {
        return newCollection(CopyOnWriteArraySet::new, elementType);
    }

    public static <E, C extends List<E>> ListBuilder<E, C> list(C col) {
        return new ListBuilder<>(col);
    }

    public static <E, C extends List<E>> ListBuilder<E, C> list(C col, Class<E> elementType) {
        return new ListBuilder<>(col, elementType);
    }

    public static <E, C extends List<E>> ListBuilder<E, C> list(C col, TypeToken<E> elementType) {
        return new ListBuilder<>(col, elementType);
    }

    public static <E, C extends List<E>> ListBuilder<E, C> newList(Supplier<C> supplier) {
        return new ListBuilder<>(supplier.get());
    }

    public static <E, C extends List<E>> ListBuilder<E, C> newList(Supplier<C> supplier, Class<E> elementType) {
        return new ListBuilder<>(supplier.get(), elementType);
    }

    public static <E, C extends List<E>> ListBuilder<E, C> newList(Supplier<C> supplier, TypeToken<E> elementType) {
        return new ListBuilder<>(supplier.get(), elementType);
    }

    public static <E> ListBuilder<E, ArrayList<E>> newArrayList(Class<E> elementType) {
        return newList(ArrayList::new, elementType);
    }

    public static <E> ListBuilder<E, ArrayList<E>> newArrayList(TypeToken<E> elementType) {
        return newList(ArrayList::new, elementType);
    }

    public static <E> ListBuilder<E, LinkedList<E>> newLinkedList(Class<E> elementType) {
        return newList(LinkedList::new, elementType);
    }

    public static <E> ListBuilder<E, LinkedList<E>> newLinkedList(TypeToken<E> elementType) {
        return newList(LinkedList::new, elementType);
    }

    public static <E> ListBuilder<E, CopyOnWriteArrayList<E>> newCopyOnWriteArrayList(Class<E> elementType) {
        return newList(CopyOnWriteArrayList::new, elementType);
    }

    public static <E> ListBuilder<E, CopyOnWriteArrayList<E>> newCopyOnWriteArrayList(TypeToken<E> elementType) {
        return newList(CopyOnWriteArrayList::new, elementType);
    }
}
