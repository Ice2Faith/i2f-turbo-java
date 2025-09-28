package i2f.jdbc.procedure.log.impl;

import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.log.JdbcProcedureLogger;
import i2f.jdbc.procedure.util.JdbcProcedureUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/9/28 15:47
 */
@Data
@NoArgsConstructor
public class DefaultJdbcProcedureLogger implements JdbcProcedureLogger {
    protected volatile AtomicBoolean debug = new AtomicBoolean(false);
    protected final DateTimeFormatter logTimeFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS");

    public DefaultJdbcProcedureLogger(AtomicBoolean debug) {
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
            System.out.println(String.format("%s [%5s] [%10s] : %s", logTimeFormatter.format(LocalDateTime.now()), "DEBUG", Thread.currentThread().getName(), "near " + location + ", msg: " + obj));
        }
    }

    @Override
    public void logInfo(Supplier<Object> supplier, Throwable e) {
        String location = ContextHolder.traceLocation();
        System.out.println(String.format("%s [%5s] [%10s] : %s", logTimeFormatter.format(LocalDateTime.now()), "ERROR", Thread.currentThread().getName(), "near " + location + ", msg: " + supplier.get()));
        if (e != null) {
            JdbcProcedureUtil.purifyStackTrace(e, true);
            e.printStackTrace();
        }
    }

    @Override
    public void logWarn(Supplier<Object> supplier, Throwable e) {
        String location = ContextHolder.traceLocation();
        System.err.println(String.format("%s [%5s] [%10s] : %s", logTimeFormatter.format(LocalDateTime.now()), "ERROR", Thread.currentThread().getName(), "near " + location + ", msg: " + supplier.get()));
        if (e != null) {
            JdbcProcedureUtil.purifyStackTrace(e, true);
            e.printStackTrace();
        }
    }

    @Override
    public void logError(Supplier<Object> supplier, Throwable e) {
        String location = ContextHolder.traceLocation();
        System.err.println(String.format("%s [%5s] [%10s] : %s", logTimeFormatter.format(LocalDateTime.now()), "ERROR", Thread.currentThread().getName(), "near " + location + ", msg: " + supplier.get()));
        if (e != null) {
            JdbcProcedureUtil.purifyStackTrace(e, true);
            e.printStackTrace();
        }
    }

}
