package i2f.extension.hazelcast;

import com.hazelcast.core.EntryView;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import i2f.cache.std.ext.IExpireContainerCache;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2025/8/3 18:36
 */
public class HazelcastCache<K,V> implements IExpireContainerCache<K,V> {
    private HazelcastInstance client;
    private String cacheName;

    public HazelcastCache(HazelcastInstance client, String cacheName) {
        this.client = client;
        this.cacheName = cacheName;
    }

    public IMap<K,V> getMap(){
        return client.getMap(cacheName);
    }

    @Override
    public Collection<K> keys() {
        return getMap().keySet();
    }

    @Override
    public void clean() {
        getMap().clear();
    }

    @Override
    public void set(K key, V value, long time, TimeUnit timeUnit) {
        getMap().put(key,value,time,timeUnit);
    }

    @Override
    public void expire(K key, long time, TimeUnit timeUnit) {
        getMap().setTtl(key, time, timeUnit);
    }

    @Override
    public Long getExpire(K key, TimeUnit timeUnit) {
        EntryView<K, V> view = getMap().getEntryView(key);
        if(view==null){
            return null;
        }
        long ts = view.getTtl();
        return timeUnit.convert(ts,TimeUnit.MILLISECONDS);
    }

    @Override
    public V get(K key) {
        return getMap().get(key);
    }

    @Override
    public void set(K key, V value) {
        getMap().put(key,value);
    }

    @Override
    public boolean exists(K key) {
        return getMap().containsKey(key);
    }

    @Override
    public void remove(K key) {
        getMap().remove(key);
    }
}
