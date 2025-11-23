package i2f.jdbc.procedure.log;


import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/9/28 15:46
 */
public interface JdbcProcedureLogger {

    void debug(boolean enable);

    boolean isDebug();

    default void logDebug(Object obj) {
        logDebug(obj,(Throwable)null);
    }

    default void logDebug(Object obj,Throwable e) {
        if(isDebug()) {
            logDebug(() -> obj, e);
        }
    }

    default void logDebug(Supplier<Object> supplier){
        logDebug(supplier,(Throwable)null);
    }

    void logDebug(Supplier<Object> supplier,Throwable e);

    default void logInfo(Object obj) {
        logInfo(() -> obj,(Throwable)null);
    }

    default void logInfo(Supplier<Object> supplier) {
        logInfo(supplier, (Throwable) null);
    }

    default void logInfo(Object obj, Throwable e) {
        logInfo(() -> obj, e);
    }

    void logInfo(Supplier<Object> supplier, Throwable e);

    default void logWarn(Object obj) {
        logWarn(() -> obj,(Throwable)null);
    }

    default void logWarn(Supplier<Object> supplier) {
        logWarn(supplier, (Throwable) null);
    }

    default void logWarn(Object obj, Throwable e) {
        logWarn(() -> obj, e);
    }

    void logWarn(Supplier<Object> supplier, Throwable e);

    default void logError(Object obj) {
        logError(() -> obj,(Throwable)null);
    }

    default void logError(Supplier<Object> supplier) {
        logError(supplier, (Throwable) null);
    }

    default void logError(Object obj, Throwable e) {
        logError(() -> obj, e);
    }

    void logError(Supplier<Object> supplier, Throwable e);
}
