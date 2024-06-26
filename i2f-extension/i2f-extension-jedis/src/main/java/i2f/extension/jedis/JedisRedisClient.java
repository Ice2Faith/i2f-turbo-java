package i2f.extension.jedis;

import i2f.extension.redis.api.IRedisClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class JedisRedisClient implements IRedisClient {

    protected String prefix;

    protected JedisPool pool;

    public JedisRedisClient(String prefix, JedisPool pool) {
        this.prefix = prefix;
        this.pool = pool;
    }

    public JedisRedisClient(JedisPool pool) {
        this.pool = pool;
    }

    synchronized public Jedis getJedis() {
        int timeoutCount = 0;
        Jedis jedis = null;
        while (true) {
            try {
                jedis = pool.getResource();
                break;
            } catch (Exception e) {
                // 底层原因是SocketTimeoutException，不过redis已经捕捉且抛出JedisConnectionException，不继承于前者
                if (e instanceof JedisConnectionException || e instanceof SocketTimeoutException) {
                    timeoutCount++;
                    if (timeoutCount > 3) {
                        break;
                    }
                } else {
                    break;
                }
            }

        }
        return jedis;
    }

    public void returnJedis(Jedis jedis) {
        if (jedis != null) {
            pool.returnResource(jedis);
        }
    }

    public <R> R delegate(Function<Jedis, R> consumer) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                throw new IllegalStateException("jedis cloud not get.");
            }
            R ret = consumer.apply(jedis);
            return ret;
        } finally {
            returnJedis(jedis);
        }
    }

    public String wrapKey(String key) {
        if (prefix == null) {
            return key;
        }
        return prefix + key;
    }

    @Override
    public boolean flushDb() {
        return delegate((jedis) -> {
            jedis.flushDB();
            return true;
        });
    }

    @Override
    public boolean hasKey(String key) {
        return delegate((jedis) -> jedis.exists(wrapKey(key)));
    }

    @Override
    public boolean set(String key, String val) {
        return set(key, val, -1);
    }

    @Override
    public boolean set(String key, String val, long timeOutSecond) {
        return delegate((jedis) -> {
            jedis.set(wrapKey(key), val);
            if (timeOutSecond >= 0) {
                jedis.expire(wrapKey(key), timeOutSecond);
            }
            return true;
        });
    }

    @Override
    public boolean expire(String key, long timeOutSecond) {
        return delegate((jedis) -> {
            jedis.expire(wrapKey(key), timeOutSecond);
            return true;
        });
    }

    @Override
    public Long getExpire(String key) {
        return delegate((jedis) -> {
            return jedis.ttl(wrapKey(key));
        });
    }

    @Override
    public boolean setUnique(String key, String val, long timeOutSecond) {
        return delegate((jedis) -> {
            jedis.setnx(wrapKey(key), val);
            if (timeOutSecond >= 0) {
                jedis.expire(wrapKey(key), timeOutSecond);
            }
            return true;
        });
    }

    @Override
    public String del(String key) {
        return delegate((jedis) -> {
            String ret = jedis.get(wrapKey(key));
            jedis.del(wrapKey(key));
            return ret;
        });
    }

    @Override
    public String get(String key) {
        return delegate((jedis) -> {
            return jedis.get(wrapKey(key));
        });
    }

    @Override
    public Collection<String> keys(String patten) {
        return delegate((jedis) -> {
            return jedis.keys(wrapKey(patten));
        });
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
        return delegate((jedis) -> {
            return jedis.lpush(wrapKey(key), values);
        });
    }

    @Override
    public Long listLength(String key) {
        return delegate((jedis) -> {
            return jedis.llen(wrapKey(key));
        });
    }

    @Override
    public List<String> listAll(String key) {
        return delegate((jedis) -> {
            return jedis.lrange(wrapKey(key), 0, jedis.llen(wrapKey(key)));
        });
    }

    @Override
    public String listAt(String key, long index) {
        return delegate((jedis) -> {
            return jedis.lindex(wrapKey(key), index);
        });
    }

    @Override
    public String listSet(String key, long index, String value) {
        return delegate((jedis) -> {
            return jedis.lset(wrapKey(key), index, value);
        });
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
        return delegate((jedis) -> {
            return jedis.hset(wrapKey(key), field, value);
        });
    }

    @Override
    public String hashGet(String key, String field) {
        return delegate((jedis) -> {
            return jedis.hget(wrapKey(key), field);
        });
    }

    @Override
    public boolean hashSetMap(String key, Map<String, String> map) {
        return delegate((jedis) -> {
            jedis.hmset(wrapKey(key), map);
            return true;
        });
    }

    @Override
    public Map<String, String> hashGetAll(String key) {
        return delegate((jedis) -> {
            return jedis.hgetAll(wrapKey(key));
        });
    }

    @Override
    public Long hashSize(String key) {
        return delegate((jedis) -> {
            return jedis.hlen(wrapKey(key));
        });
    }

    @Override
    public Long hashDelete(String key, String... fields) {
        return delegate((jedis) -> {
            return jedis.hdel(wrapKey(key), fields);
        });
    }
}
