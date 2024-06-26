package i2f.extension.redis.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/26 14:13
 * @desc
 */
public interface IRedisClient {

    boolean flushDb();

    boolean hasKey(String key);

    boolean set(String key, String val);

    boolean set(String key, String val, long timeOutSecond);

    boolean expire(String key, long timeOutSecond);

    Long getExpire(String key);

    boolean setUnique(String key, String val, long timeOutSecond);

    String del(String key);

    String get(String key);

    Collection<String> keys(String patten);

    /**
     * Redis的List支持，也就是一个键key下面存放的是一个List
     *
     * @param key    Redis键
     * @param values List的变长值
     * @return 个数
     */
    Long listPush(String key, String... values);

    Long listLength(String key);

    List<String> listAll(String key);

    String listAt(String key, long index);

    String listSet(String key, long index, String value);

    /**
     * Redis的Hash支持
     * 也就是某一个键key下面存放的是一个Hash域值对(Field,Value)
     *
     * @param key   Redis键
     * @param field Hash域
     * @param value Hash
     * @return 返回个数
     */
    Long hashSet(String key, String field, String value);

    String hashGet(String key, String field);

    boolean hashSetMap(String key, Map<String, String> map);

    Map<String, String> hashGetAll(String key);

    Long hashSize(String key);

    Long hashDelete(String key, String... fields);
}
