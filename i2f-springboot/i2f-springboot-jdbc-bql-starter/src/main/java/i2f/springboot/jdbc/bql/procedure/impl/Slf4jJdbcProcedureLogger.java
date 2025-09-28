package i2f.springboot.jdbc.bql.procedure.impl;

import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.log.JdbcProcedureLogger;
import i2f.jdbc.procedure.util.JdbcProcedureUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    public Slf4jJdbcProcedureLogger(AtomicBoolean debug) {
        this.debug = debug;
    }

    @Override
    public void debug(boolean enable) {
        debug.set(enable);
    }

    @Override
    public boolean isDebug() {
        return debug.get();
    }

    @Override
    public void logDebug(Supplier<Object> supplier) {
        if (isDebug()) {
            logDebug(supplier.get());
        }
    }

    @Override
    public void logDebug(Object obj) {
        if (isDebug()) {
            String location = ContextHolder.traceLocation();
            log.debug("near " + location + ", msg: " + obj);
        }
    }

    @Override
    public void logInfo(Supplier<Object> supplier, Throwable e) {
        String location = ContextHolder.traceLocation();
        if (e != null) {
            JdbcProcedureUtil.purifyStackTrace(e, true);
            log.info("near " + location + ", msg: " + supplier.get(), e);
        } else {
            log.info("near " + location + ", msg: " + supplier.get());
        }
    }

    @Override
    public void logWarn(Supplier<Object> supplier, Throwable e) {
        String location = ContextHolder.traceLocation();
        if (e != null) {
            JdbcProcedureUtil.purifyStackTrace(e, true);
            log.warn("near " + location + ", msg: " + supplier.get(), e);
        } else {
            log.warn("near " + location + ", msg: " + supplier.get());
        }
    }

    @Override
    public void logError(Supplier<Object> supplier, Throwable e) {
        String location = ContextHolder.traceLocation();
        if (e != null) {
            JdbcProcedureUtil.purifyStackTrace(e, true);
            log.error("near " + location + ", msg: " + supplier.get(), e);
        } else {
            log.error("near " + location + ", msg: " + supplier.get());
        }
    }

}
