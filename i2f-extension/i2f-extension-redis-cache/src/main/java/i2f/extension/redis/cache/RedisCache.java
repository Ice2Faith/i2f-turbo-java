package i2f.extension.redis.cache;

import i2f.cache.impl.IStringObjectExpireContainerCache;
import i2f.cache.persist.IDistributedCache;
import i2f.cache.persist.IPersistCache;
import i2f.extension.redis.api.IRedisClient;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/6/26 15:56
 * @desc
 */
public class RedisCache implements IStringObjectExpireContainerCache,
        IPersistCache<String, Object>,
        IDistributedCache<String, Object> {
    protected String prefix;
    protected IRedisClient client;
    protected Function<Object, String> encoder;
    protected Function<String, Object> decoder;

    public RedisCache(IRedisClient client, Function<Object, String> encoder, Function<String, Object> decoder) {
        this.client = client;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public String wrapKey(String key) {
        if (prefix == null) {
            return key;
        }
        return prefix + key;
    }

    @Override
    public Collection<String> keys() {
        return client.keys(wrapKey("*"));
    }

    @Override
    public void clean() {
        client.flushDb();
    }

    @Override
    public void set(String key, Object value, long time, TimeUnit timeUnit) {
        client.set(wrapKey(key), encoder.apply(value), (int) timeUnit.toSeconds(time));
    }

    @Override
    public void expire(String key, long time, TimeUnit timeUnit) {
        client.expire(wrapKey(key), (int) timeUnit.toSeconds(time));
    }

    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        long expire = client.getExpire(wrapKey(key));
        return timeUnit.convert(expire, TimeUnit.SECONDS);
    }

    @Override
    public Object get(String key) {
        String str = client.get(wrapKey(key));
        return decoder.apply(str);
    }

    @Override
    public void set(String key, Object value) {
        client.set(wrapKey(key), encoder.apply(value));
    }

    @Override
    public boolean exists(String key) {
        return client.hasKey(wrapKey(key));
    }

    @Override
    public void remove(String key) {
        client.del(wrapKey(key));
    }
}
