package i2f.spring.redis;

import i2f.extension.redis.api.IRedisClient;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SpringRedisClient implements IRedisClient {

    protected String prefix;

    protected RedisTemplate<String, Object> template;

    public SpringRedisClient(String prefix, RedisTemplate<String, Object> template) {
        this.prefix = prefix;
        this.template = template;
    }

    public SpringRedisClient(RedisTemplate<String, Object> template) {
        this.template = template;
    }


    public String wrapKey(String key) {
        if (prefix == null) {
            return key;
        }
        return prefix + key;
    }


    @Override
    public boolean flushDb() {
        RedisConnection conn = template.getConnectionFactory().getConnection();
        conn.flushDb();
        return true;
    }

    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(template.hasKey(wrapKey(key)));
    }

    @Override
    public boolean set(String key, String val) {
        template.opsForValue().set(wrapKey(key), val);
        return true;
    }

    @Override
    public boolean set(String key, String val, long timeOutSecond) {
        if (timeOutSecond >= 0) {
            template.opsForValue().set(wrapKey(key), val, timeOutSecond, TimeUnit.SECONDS);
        } else {
            template.opsForValue().set(wrapKey(key), val);
        }
        return true;
    }

    @Override
    public boolean expire(String key, long timeOutSecond) {
        return Boolean.TRUE.equals(template.expire(wrapKey(key), timeOutSecond, TimeUnit.SECONDS));
    }

    @Override
    public Long getExpire(String key) {
        return template.getExpire(wrapKey(key), TimeUnit.SECONDS);
    }

    @Override
    public boolean setUnique(String key, String val, long timeOutSecond) {
        if (timeOutSecond >= 0) {
            return Boolean.TRUE.equals(template.opsForValue().setIfAbsent(wrapKey(key), val, timeOutSecond, TimeUnit.SECONDS));
        } else {
            return Boolean.TRUE.equals(template.opsForValue().setIfAbsent(wrapKey(key), val));
        }
    }

    @Override
    public String del(String key) {
        Object val = template.opsForValue().get(wrapKey(key));
        template.delete(wrapKey(key));
        return null == val ? null : String.valueOf(val);
    }

    @Override
    public String get(String key) {
        Object val = template.opsForValue().get(wrapKey(key));
        return null == val ? null : String.valueOf(val);
    }

    @Override
    public Collection<String> keys(String patten) {
        return template.keys(wrapKey(patten));
    }

    /**
     * Redis的List支持，也就是一个键key下面存放的是一个List
     *
     * @param key    Redis键
     * @param values List的变长值
     * @return 个数
     */
    @Override
    public Long listPush(String key, String... values) {
        return template.opsForList().rightPushAll(wrapKey(key), values);
    }

    @Override
    public Long listLength(String key) {
        return template.opsForList().size(wrapKey(key));
    }

    @Override
    public List<String> listAll(String key) {
        Long size = template.opsForList().size(wrapKey(key));
        if (size == null) {
            size = (long) Integer.MAX_VALUE;
        }
        List<Object> list = template.opsForList().range(wrapKey(key), 0, size);
        return list.stream()
                .map(e -> e == null ? null : String.valueOf(e))
                .collect(Collectors.toList());
    }

    @Override
    public String listAt(String key, long index) {
        Object val = template.opsForList().index(wrapKey(key), index);
        return val == null ? null : String.valueOf(val);
    }

    @Override
    public String listSet(String key, long index, String value) {
        String ret = listAt(wrapKey(key), index);
        template.opsForList().set(wrapKey(key), index, value);
        return ret;
    }

    /**
     * Redis的Hash支持
     * 也就是某一个键key下面存放的是一个Hash域值对(Field,Value)
     *
     * @param key   Redis键
     * @param field Hash域
     * @param value Hash
     * @return 返回个数
     */
    @Override
    public Long hashSet(String key, String field, String value) {
        template.opsForHash().put(wrapKey(key), field, value);
        return template.opsForHash().size(wrapKey(key));
    }

    @Override
    public String hashGet(String key, String field) {
        Object val = template.opsForHash().get(wrapKey(key), field);
        return val == null ? null : String.valueOf(val);
    }

    @Override
    public boolean hashSetMap(String key, Map<String, String> map) {
        template.opsForHash().putAll(wrapKey(key), map);
        return true;
    }

    @Override
    public Map<String, String> hashGetAll(String key) {
        Map<Object, Object> map = template.opsForHash().entries(wrapKey(key));
        Map<String, String> ret = new LinkedHashMap<>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            map.put(String.valueOf(entry.getKey()), (value == null ? null : String.valueOf(value)));
        }
        return ret;
    }

    @Override
    public Long hashSize(String key) {
        return template.opsForHash().size(wrapKey(key));
    }

    @Override
    public Long hashDelete(String key, String... fields) {
        return template.opsForHash().delete(wrapKey(key), fields);
    }
}
