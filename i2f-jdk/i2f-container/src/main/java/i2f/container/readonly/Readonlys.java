package i2f.container.readonly;

import i2f.container.readonly.adapter.ReadonlyCollectionAdapter;
import i2f.container.readonly.adapter.ReadonlyListAdapter;
import i2f.container.readonly.adapter.ReadonlyMapAdapter;
import i2f.container.readonly.adapter.ReadonlySetAdapter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2024/5/19 22:51
 * @desc
 */
public class Readonlys {
    public static<E> Collection<E> collection(Collection<E> collection){
        return new ReadonlyCollectionAdapter<>(collection);
    }

    public static<E> List<E> list(List<E> list){
        return new ReadonlyListAdapter<>(list);
    }

    public static<E> Set<E> set(Set<E> list){
        return new ReadonlySetAdapter<>(list);
    }

    public static<K,V> Map<K,V> map(Map<K,V> list){
        return new ReadonlyMapAdapter<>(list);
    }
}
