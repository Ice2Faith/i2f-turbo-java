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
        logDebug(() -> obj);
    }

    void logDebug(Supplier<Object> supplier);

    default void logInfo(Object obj) {
        logInfo(() -> obj);
    }

    default void logInfo(Supplier<Object> supplier) {
        logInfo(supplier, null);
    }

    default void logInfo(Object obj, Throwable e) {
        logInfo(() -> obj, e);
    }

    void logInfo(Supplier<Object> supplier, Throwable e);

    default void logWarn(Object obj) {
        logWarn(() -> obj);
    }

    default void logWarn(Supplier<Object> supplier) {
        logWarn(supplier, null);
    }

    default void logWarn(Object obj, Throwable e) {
        logWarn(() -> obj, e);
    }

    void logWarn(Supplier<Object> supplier, Throwable e);

    default void logError(Object obj) {
        logError(() -> obj);
    }

    default void logError(Supplier<Object> supplier) {
        logError(supplier, null);
    }

    default void logError(Object obj, Throwable e) {
        logError(() -> obj, e);
    }

    void logError(Supplier<Object> supplier, Throwable e);
}
