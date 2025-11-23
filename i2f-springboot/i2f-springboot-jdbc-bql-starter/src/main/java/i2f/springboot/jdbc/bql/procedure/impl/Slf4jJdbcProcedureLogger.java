package i2f.springboot.jdbc.bql.procedure.impl;

import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.log.JdbcProcedureLogger;
import i2f.jdbc.procedure.util.JdbcProcedureUtil;
import i2f.lru.LruMap;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/9/28 15:53
 */
@Slf4j
@Data
@NoArgsConstructor
public class Slf4jJdbcProcedureLogger implements JdbcProcedureLogger {
    private volatile AtomicBoolean debug = new AtomicBoolean(false);
    private final LruMap<String, Logger> cacheLoggerMap=new LruMap<>(1024);

    public Slf4jJdbcProcedureLogger(AtomicBoolean debug) {
        this.debug = debug;
    }

    public Logger getLogger(String name) {
        return cacheLoggerMap.computeIfAbsent(name,k->{
            return LoggerFactory.getLogger(name);
        });
    }

    public Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public Logger getLogger(){
        return getLogger(ContextHolder.TRACE_FILE.get());
    }

    @Override
    public void debug(boolean enable) {
        debug.set(enable);
    }

    @Override
    public boolean isDebug() {
        Boolean ok = ContextHolder.DEBUG_MODE.get();
        if (ok != null && ok) {
            return true;
        }
        return debug.get();
    }

    @Override
    public void logDebug(Supplier<Object> supplier,Throwable e) {
        if (isDebug()) {
            String location = ContextHolder.traceLocation();
            String msg = "near " + location + ", msg: " + supplier.get();
            if(e!=null){
                JdbcProcedureUtil.purifyStackTrace(e, true);
                getLogger().debug(msg,e);
            }else {
                getLogger().debug(msg);
            }
        }
    }


    @Override
    public void logInfo(Supplier<Object> supplier, Throwable e) {
        String location = ContextHolder.traceLocation();
        if (e != null) {
            JdbcProcedureUtil.purifyStackTrace(e, true);
            getLogger().info("near " + location + ", msg: " + supplier.get(), e);
        } else {
            getLogger().info("near " + location + ", msg: " + supplier.get());
        }
    }

    @Override
    public void logWarn(Supplier<Object> supplier, Throwable e) {
        String location = ContextHolder.traceLocation();
        if (e != null) {
            JdbcProcedureUtil.purifyStackTrace(e, true);
            getLogger().warn("near " + location + ", msg: " + supplier.get(), e);
        } else {
            getLogger().warn("near " + location + ", msg: " + supplier.get());
        }
    }

    @Override
    public void logError(Supplier<Object> supplier, Throwable e) {
        String location = ContextHolder.traceLocation();
        if (e != null) {
            JdbcProcedureUtil.purifyStackTrace(e, true);
            getLogger().error("near " + location + ", msg: " + supplier.get(), e);
        } else {
            getLogger().error("near " + location + ", msg: " + supplier.get());
        }
    }

}
