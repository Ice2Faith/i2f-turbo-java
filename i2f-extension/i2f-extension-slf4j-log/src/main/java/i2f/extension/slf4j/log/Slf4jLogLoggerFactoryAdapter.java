package i2f.extension.slf4j.log;

import i2f.log.LoggerFactory;
import i2f.lru.LruMap;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;


/**
 * @author Ice2Faith
 * @date 2024/7/23 19:03
 * @desc
 */
public class Slf4jLogLoggerFactoryAdapter implements ILoggerFactory {
    public static final Slf4jLogLoggerFactoryAdapter INSTANCE=new Slf4jLogLoggerFactoryAdapter();
    public static LruMap<String, Logger> CACHE = new LruMap<>(1024);
    @Override
    public Logger getLogger(String location) {
        return CACHE.computeIfAbsent(location,(loc)->new Slf4jLogLoggerAdapter(LoggerFactory.getLogger(loc)));
    }
}
