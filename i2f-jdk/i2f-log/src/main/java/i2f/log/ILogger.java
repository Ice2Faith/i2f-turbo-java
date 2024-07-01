package i2f.log;

import i2f.log.enums.LogLevel;
import i2f.log.perf.PerfSupplier;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/7/1 9:58
 * @desc
 */
public interface ILogger {
    String getLocation();

    boolean enableLevel(LogLevel level);

    default boolean enableFatal() {
        return enableLevel(LogLevel.FATAL);
    }

    default boolean enableError() {
        return enableLevel(LogLevel.ERROR);
    }

    default boolean enableWarn() {
        return enableLevel(LogLevel.WARN);
    }

    default boolean enableInfo() {
        return enableLevel(LogLevel.INFO);
    }

    default boolean enableDebug() {
        return enableLevel(LogLevel.DEBUG);
    }

    default boolean enableTrace() {
        return enableLevel(LogLevel.TRACE);
    }

    void write(Object meta, LogLevel level, String format, Object... args);

    void write(Object meta, LogLevel level, Throwable ex, String format, Object... args);

    default void write(LogLevel level, String format, Object... args) {
        if (enableLevel(level)) {
            write(null, level, format, args);
        }
    }

    default void write(LogLevel level, Throwable ex, String format, Object... args) {
        if (enableLevel(level)) {
            write(null, level, ex, format, args);
        }
    }

    default void write(Object meta, LogLevel level, Supplier<?> supplier) {
        if (enableLevel(level)) {
            write(meta, level, String.valueOf(supplier.get()));
        }
    }

    default void write(Object meta, LogLevel level, Throwable ex, Supplier<?> supplier) {
        if (enableLevel(level)) {
            write(meta, level, ex, String.valueOf(supplier.get()));
        }
    }

    default void write(LogLevel level, Supplier<?> supplier) {
        if (enableLevel(level)) {
            write(null, level, String.valueOf(supplier.get()));
        }
    }

    default void write(LogLevel level, Throwable ex, Supplier<?> supplier) {
        if (enableLevel(level)) {
            write(null, level, ex, String.valueOf(supplier.get()));
        }
    }


    default void write(Object meta, LogLevel level, PerfSupplier<?> supplier, Object... args) {
        if (enableLevel(level)) {
            write(meta, level, String.valueOf(supplier.get(args)));
        }
    }

    default void write(Object meta, LogLevel level, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        if (enableLevel(level)) {
            write(meta, level, ex, String.valueOf(supplier.get(args)));
        }
    }

    default void write(LogLevel level, PerfSupplier<?> supplier, Object... args) {
        if (enableLevel(level)) {
            write(null, level, String.valueOf(supplier.get(args)));
        }
    }

    default void write(LogLevel level, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        if (enableLevel(level)) {
            write(null, level, ex, String.valueOf(supplier.get(args)));
        }
    }


    default <T> void write(Object meta, LogLevel level, Function<T, ?> supplier, T v1) {
        if (enableLevel(level)) {
            write(meta, level, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void write(Object meta, LogLevel level, Throwable ex, Function<T, ?> supplier, T v1) {
        if (enableLevel(level)) {
            write(meta, level, ex, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void write(LogLevel level, Function<T, ?> supplier, T v1) {
        if (enableLevel(level)) {
            write(null, level, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void write(LogLevel level, Throwable ex, Function<T, ?> supplier, T v1) {
        if (enableLevel(level)) {
            write(null, level, ex, String.valueOf(supplier.apply(v1)));
        }
    }


    default <T, U> void write(Object meta, LogLevel level, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableLevel(level)) {
            write(meta, level, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void write(Object meta, LogLevel level, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableLevel(level)) {
            write(meta, level, ex, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void write(LogLevel level, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableLevel(level)) {
            write(null, level, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void write(LogLevel level, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableLevel(level)) {
            write(null, level, ex, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default void fatalMeta(Object meta, String format, Object... args) {
        if (enableFatal()) {
            write(meta, LogLevel.FATAL, format, args);
        }
    }

    default void fatal(Object meta, Throwable ex, String format, Object... args) {
        if (enableFatal()) {
            write(meta, LogLevel.FATAL, ex, format, args);
        }
    }

    default void fatal(String format, Object... args) {
        if (enableFatal()) {
            write(null, LogLevel.FATAL, format, args);
        }
    }

    default void fatal(Throwable ex, String format, Object... args) {
        if (enableFatal()) {
            write(null, LogLevel.FATAL, ex, format, args);
        }
    }


    default void fatal(Object meta, Supplier<?> supplier) {
        if (enableFatal()) {
            write(meta, LogLevel.FATAL, String.valueOf(supplier.get()));
        }
    }

    default void fatal(Object meta, Throwable ex, Supplier<?> supplier) {
        if (enableFatal()) {
            write(meta, LogLevel.FATAL, ex, String.valueOf(supplier.get()));
        }
    }

    default void fatal(Supplier<?> supplier) {
        if (enableFatal()) {
            write(null, LogLevel.FATAL, String.valueOf(supplier.get()));
        }
    }

    default void fatal(Throwable ex, Supplier<?> supplier) {
        if (enableFatal()) {
            write(null, LogLevel.FATAL, ex, String.valueOf(supplier.get()));
        }
    }


    default void fatal(Object meta, PerfSupplier<?> supplier, Object... args) {
        if (enableFatal()) {
            write(meta, LogLevel.FATAL, String.valueOf(supplier.get(args)));
        }
    }

    default void fatal(Object meta, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        if (enableFatal()) {
            write(meta, LogLevel.FATAL, ex, String.valueOf(supplier.get(args)));
        }
    }

    default void fatal(PerfSupplier<?> supplier, Object... args) {
        if (enableFatal()) {
            write(null, LogLevel.FATAL, String.valueOf(supplier.get(args)));
        }
    }

    default void fatal(Throwable ex, PerfSupplier<?> supplier, Object... args) {
        if (enableFatal()) {
            write(null, LogLevel.FATAL, ex, String.valueOf(supplier.get(args)));
        }
    }

    default <T> void fatal(Object meta, Function<T, ?> supplier, T v1) {
        if (enableFatal()) {
            write(meta, LogLevel.FATAL, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void fatal(Object meta, Throwable ex, Function<T, ?> supplier, T v1) {
        if (enableFatal()) {
            write(meta, LogLevel.FATAL, ex, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void fatal(Function<T, ?> supplier, T v1) {
        if (enableFatal()) {
            write(null, LogLevel.FATAL, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void fatal(Throwable ex, Function<T, ?> supplier, T v1) {
        if (enableFatal()) {
            write(null, LogLevel.FATAL, ex, String.valueOf(supplier.apply(v1)));
        }
    }


    default <T, U> void fatal(Object meta, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableFatal()) {
            write(meta, LogLevel.FATAL, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void fatal(Object meta, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableFatal()) {
            write(meta, LogLevel.FATAL, ex, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void fatal(BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableFatal()) {
            write(null, LogLevel.FATAL, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void fatal(Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableFatal()) {
            write(null, LogLevel.FATAL, ex, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default void errorMeta(Object meta, String format, Object... args) {
        if (enableError()) {
            write(meta, LogLevel.ERROR, format, args);
        }
    }

    default void error(Object meta, Throwable ex, String format, Object... args) {
        if (enableError()) {
            write(meta, LogLevel.ERROR, ex, format, args);
        }
    }

    default void error(String format, Object... args) {
        if (enableError()) {
            write(null, LogLevel.ERROR, format, args);
        }
    }

    default void error(Throwable ex, String format, Object... args) {
        if (enableError()) {
            write(null, LogLevel.ERROR, ex, format, args);
        }
    }


    default void error(Object meta, Supplier<?> supplier) {
        if (enableError()) {
            write(meta, LogLevel.ERROR, String.valueOf(supplier.get()));
        }
    }

    default void error(Object meta, Throwable ex, Supplier<?> supplier) {
        if (enableError()) {
            write(meta, LogLevel.ERROR, ex, String.valueOf(supplier.get()));
        }
    }

    default void error(Supplier<?> supplier) {
        if (enableError()) {
            write(null, LogLevel.ERROR, String.valueOf(supplier.get()));
        }
    }

    default void error(Throwable ex, Supplier<?> supplier) {
        if (enableError()) {
            write(null, LogLevel.ERROR, ex, String.valueOf(supplier.get()));
        }
    }


    default void error(Object meta, PerfSupplier<?> supplier, Object... args) {
        if (enableError()) {
            write(meta, LogLevel.ERROR, String.valueOf(supplier.get(args)));
        }
    }

    default void error(Object meta, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        if (enableError()) {
            write(meta, LogLevel.ERROR, ex, String.valueOf(supplier.get(args)));
        }
    }

    default void error(PerfSupplier<?> supplier, Object... args) {
        if (enableError()) {
            write(null, LogLevel.ERROR, String.valueOf(supplier.get(args)));
        }
    }

    default void error(Throwable ex, PerfSupplier<?> supplier, Object... args) {
        if (enableError()) {
            write(null, LogLevel.ERROR, ex, String.valueOf(supplier.get(args)));
        }
    }

    default <T> void error(Object meta, Function<T, ?> supplier, T v1) {
        if (enableError()) {
            write(meta, LogLevel.ERROR, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void error(Object meta, Throwable ex, Function<T, ?> supplier, T v1) {
        if (enableError()) {
            write(meta, LogLevel.ERROR, ex, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void error(Function<T, ?> supplier, T v1) {
        if (enableError()) {
            write(null, LogLevel.ERROR, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void error(Throwable ex, Function<T, ?> supplier, T v1) {
        if (enableError()) {
            write(null, LogLevel.ERROR, ex, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T, U> void error(Object meta, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableError()) {
            write(meta, LogLevel.ERROR, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void error(Object meta, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableError()) {
            write(meta, LogLevel.ERROR, ex, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void error(BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableError()) {
            write(null, LogLevel.ERROR, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void error(Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableError()) {
            write(null, LogLevel.ERROR, ex, String.valueOf(supplier.apply(v1, v2)));
        }
    }


    default void warnMeta(Object meta, String format, Object... args) {
        if (enableWarn()) {
            write(meta, LogLevel.WARN, format, args);
        }
    }

    default void warn(Object meta, Throwable ex, String format, Object... args) {
        if (enableWarn()) {
            write(meta, LogLevel.WARN, ex, format, args);
        }
    }

    default void warn(String format, Object... args) {
        if (enableWarn()) {
            write(null, LogLevel.WARN, format, args);
        }
    }

    default void warn(Throwable ex, String format, Object... args) {
        if (enableWarn()) {
            write(null, LogLevel.WARN, ex, format, args);
        }
    }


    default void warn(Object meta, Supplier<?> supplier) {
        if (enableWarn()) {
            write(meta, LogLevel.WARN, String.valueOf(supplier.get()));
        }
    }

    default void warn(Object meta, Throwable ex, Supplier<?> supplier) {
        if (enableWarn()) {
            write(meta, LogLevel.WARN, ex, String.valueOf(supplier.get()));
        }
    }

    default void warn(Supplier<?> supplier) {
        if (enableWarn()) {
            write(null, LogLevel.WARN, String.valueOf(supplier.get()));
        }
    }

    default void warn(Throwable ex, Supplier<?> supplier) {
        if (enableWarn()) {
            write(null, LogLevel.WARN, ex, String.valueOf(supplier.get()));
        }
    }

    default void warn(Object meta, PerfSupplier<?> supplier, Object... args) {
        if (enableWarn()) {
            write(meta, LogLevel.WARN, String.valueOf(supplier.get(args)));
        }
    }

    default void warn(Object meta, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        if (enableWarn()) {
            write(meta, LogLevel.WARN, ex, String.valueOf(supplier.get(args)));
        }
    }

    default void warn(PerfSupplier<?> supplier, Object... args) {
        if (enableWarn()) {
            write(null, LogLevel.WARN, String.valueOf(supplier.get(args)));
        }
    }

    default void warn(Throwable ex, PerfSupplier<?> supplier, Object... args) {
        if (enableWarn()) {
            write(null, LogLevel.WARN, ex, String.valueOf(supplier.get(args)));
        }
    }


    default <T> void warn(Object meta, Function<T, ?> supplier, T v1) {
        if (enableWarn()) {
            write(meta, LogLevel.WARN, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void warn(Object meta, Throwable ex, Function<T, ?> supplier, T v1) {
        if (enableWarn()) {
            write(meta, LogLevel.WARN, ex, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void warn(Function<T, ?> supplier, T v1) {
        if (enableWarn()) {
            write(null, LogLevel.WARN, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void warn(Throwable ex, Function<T, ?> supplier, T v1) {
        if (enableWarn()) {
            write(null, LogLevel.WARN, ex, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T, U> void warn(Object meta, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableWarn()) {
            write(meta, LogLevel.WARN, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void warn(Object meta, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableWarn()) {
            write(meta, LogLevel.WARN, ex, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void warn(BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableWarn()) {
            write(null, LogLevel.WARN, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void warn(Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableWarn()) {
            write(null, LogLevel.WARN, ex, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default void infoMeta(Object meta, String format, Object... args) {
        if (enableInfo()) {
            write(meta, LogLevel.INFO, format, args);
        }
    }

    default void info(Object meta, Throwable ex, String format, Object... args) {
        if (enableInfo()) {
            write(meta, LogLevel.INFO, ex, format, args);
        }
    }

    default void info(String format, Object... args) {
        if (enableInfo()) {
            write(null, LogLevel.INFO, format, args);
        }
    }

    default void info(Throwable ex, String format, Object... args) {
        if (enableInfo()) {
            write(null, LogLevel.INFO, ex, format, args);
        }
    }


    default void info(Object meta, Supplier<?> supplier) {
        if (enableInfo()) {
            write(meta, LogLevel.INFO, String.valueOf(supplier.get()));
        }
    }

    default void info(Object meta, Throwable ex, Supplier<?> supplier) {
        if (enableInfo()) {
            write(meta, LogLevel.INFO, ex, String.valueOf(supplier.get()));
        }
    }

    default void info(Supplier<?> supplier) {
        if (enableInfo()) {
            write(null, LogLevel.INFO, String.valueOf(supplier.get()));
        }
    }

    default void info(Throwable ex, Supplier<?> supplier) {
        if (enableInfo()) {
            write(null, LogLevel.INFO, ex, String.valueOf(supplier.get()));
        }
    }

    default void info(Object meta, PerfSupplier<?> supplier, Object... args) {
        if (enableInfo()) {
            write(meta, LogLevel.INFO, String.valueOf(supplier.get(args)));
        }
    }

    default void info(Object meta, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        if (enableInfo()) {
            write(meta, LogLevel.INFO, ex, String.valueOf(supplier.get(args)));
        }
    }

    default void info(PerfSupplier<?> supplier, Object... args) {
        if (enableInfo()) {
            write(null, LogLevel.INFO, String.valueOf(supplier.get(args)));
        }
    }

    default void info(Throwable ex, PerfSupplier<?> supplier, Object... args) {
        if (enableInfo()) {
            write(null, LogLevel.INFO, ex, String.valueOf(supplier.get(args)));
        }
    }

    default <T> void info(Object meta, Function<T, ?> supplier, T v1) {
        if (enableInfo()) {
            write(meta, LogLevel.INFO, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void info(Object meta, Throwable ex, Function<T, ?> supplier, T v1) {
        if (enableInfo()) {
            write(meta, LogLevel.INFO, ex, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void info(Function<T, ?> supplier, T v1) {
        if (enableInfo()) {
            write(null, LogLevel.INFO, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void info(Throwable ex, Function<T, ?> supplier, T v1) {
        if (enableInfo()) {
            write(null, LogLevel.INFO, ex, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T, U> void info(Object meta, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableInfo()) {
            write(meta, LogLevel.INFO, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void info(Object meta, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableInfo()) {
            write(meta, LogLevel.INFO, ex, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void info(BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableInfo()) {
            write(null, LogLevel.INFO, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void info(Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableInfo()) {
            write(null, LogLevel.INFO, ex, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default void debugMeta(Object meta, String format, Object... args) {
        if (enableDebug()) {
            write(meta, LogLevel.DEBUG, format, args);
        }
    }

    default void debug(Object meta, Throwable ex, String format, Object... args) {
        if (enableDebug()) {
            write(meta, LogLevel.DEBUG, ex, format, args);
        }
    }

    default void debug(String format, Object... args) {
        if (enableDebug()) {
            write(null, LogLevel.DEBUG, format, args);
        }
    }

    default void debug(Throwable ex, String format, Object... args) {
        if (enableDebug()) {
            write(null, LogLevel.DEBUG, ex, format, args);
        }
    }

    default void debug(Object meta, Supplier<?> supplier) {
        if (enableDebug()) {
            write(meta, LogLevel.DEBUG, String.valueOf(supplier.get()));
        }
    }

    default void debug(Object meta, Throwable ex, Supplier<?> supplier) {
        if (enableDebug()) {
            write(meta, LogLevel.DEBUG, ex, String.valueOf(supplier.get()));
        }
    }

    default void debug(Supplier<?> supplier) {
        if (enableDebug()) {
            write(null, LogLevel.DEBUG, String.valueOf(supplier.get()));
        }
    }

    default void debug(Throwable ex, Supplier<?> supplier) {
        if (enableDebug()) {
            write(null, LogLevel.DEBUG, ex, String.valueOf(supplier.get()));
        }
    }

    default void debug(Object meta, PerfSupplier<?> supplier, Object... args) {
        if (enableDebug()) {
            write(meta, LogLevel.DEBUG, String.valueOf(supplier.get(args)));
        }
    }

    default void debug(Object meta, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        if (enableDebug()) {
            write(meta, LogLevel.DEBUG, ex, String.valueOf(supplier.get(args)));
        }
    }

    default void debug(PerfSupplier<?> supplier, Object... args) {
        if (enableDebug()) {
            write(null, LogLevel.DEBUG, String.valueOf(supplier.get(args)));
        }
    }

    default void debug(Throwable ex, PerfSupplier<?> supplier, Object... args) {
        if (enableDebug()) {
            write(null, LogLevel.DEBUG, ex, String.valueOf(supplier.get(args)));
        }
    }

    default <T> void debug(Object meta, Function<T, ?> supplier, T v1) {
        if (enableDebug()) {
            write(meta, LogLevel.DEBUG, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void debug(Object meta, Throwable ex, Function<T, ?> supplier, T v1) {
        if (enableDebug()) {
            write(meta, LogLevel.DEBUG, ex, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void debug(Function<T, ?> supplier, T v1) {
        if (enableDebug()) {
            write(null, LogLevel.DEBUG, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void debug(Throwable ex, Function<T, ?> supplier, T v1) {
        if (enableDebug()) {
            write(null, LogLevel.DEBUG, ex, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T, U> void debug(Object meta, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableDebug()) {
            write(meta, LogLevel.DEBUG, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void debug(Object meta, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableDebug()) {
            write(meta, LogLevel.DEBUG, ex, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void debug(BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableDebug()) {
            write(null, LogLevel.DEBUG, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void debug(Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableDebug()) {
            write(null, LogLevel.DEBUG, ex, String.valueOf(supplier.apply(v1, v2)));
        }
    }


    default void traceMeta(Object meta, String format, Object... args) {
        if (enableTrace()) {
            write(meta, LogLevel.TRACE, format, args);
        }
    }

    default void trace(Object meta, Throwable ex, String format, Object... args) {
        if (enableTrace()) {
            write(meta, LogLevel.TRACE, ex, format, args);
        }
    }


    default void trace(String format, Object... args) {
        if (enableTrace()) {
            write(null, LogLevel.TRACE, format, args);
        }
    }

    default void trace(Throwable ex, String format, Object... args) {
        if (enableTrace()) {
            write(null, LogLevel.TRACE, ex, format, args);
        }
    }


    default void trace(Object meta, Supplier<?> supplier) {
        if (enableTrace()) {
            write(meta, LogLevel.TRACE, String.valueOf(supplier.get()));
        }
    }

    default void trace(Object meta, Throwable ex, Supplier<?> supplier) {
        if (enableTrace()) {
            write(meta, LogLevel.TRACE, ex, String.valueOf(supplier.get()));
        }
    }


    default void trace(Supplier<?> supplier) {
        if (enableTrace()) {
            write(null, LogLevel.TRACE, String.valueOf(supplier.get()));
        }
    }

    default void trace(Throwable ex, Supplier<?> supplier) {
        if (enableTrace()) {
            write(null, LogLevel.TRACE, ex, String.valueOf(supplier.get()));
        }
    }

    default void trace(Object meta, PerfSupplier<?> supplier, Object... args) {
        if (enableTrace()) {
            write(meta, LogLevel.TRACE, String.valueOf(supplier.get(args)));
        }
    }

    default void trace(Object meta, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        if (enableTrace()) {
            write(meta, LogLevel.TRACE, ex, String.valueOf(supplier.get(args)));
        }
    }

    default void trace(PerfSupplier<?> supplier, Object... args) {
        if (enableTrace()) {
            write(null, LogLevel.TRACE, String.valueOf(supplier.get(args)));
        }
    }

    default void trace(Throwable ex, PerfSupplier<?> supplier, Object... args) {
        if (enableTrace()) {
            write(null, LogLevel.TRACE, ex, String.valueOf(supplier.get(args)));
        }
    }

    default <T> void trace(Object meta, Function<T, ?> supplier, T v1) {
        if (enableTrace()) {
            write(meta, LogLevel.TRACE, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void trace(Object meta, Throwable ex, Function<T, ?> supplier, T v1) {
        if (enableTrace()) {
            write(meta, LogLevel.TRACE, ex, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void trace(Function<T, ?> supplier, T v1) {
        if (enableTrace()) {
            write(null, LogLevel.TRACE, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T> void trace(Throwable ex, Function<T, ?> supplier, T v1) {
        if (enableTrace()) {
            write(null, LogLevel.TRACE, ex, String.valueOf(supplier.apply(v1)));
        }
    }

    default <T, U> void trace(Object meta, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableTrace()) {
            write(meta, LogLevel.TRACE, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void trace(Object meta, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableTrace()) {
            write(meta, LogLevel.TRACE, ex, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void trace(BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableTrace()) {
            write(null, LogLevel.TRACE, String.valueOf(supplier.apply(v1, v2)));
        }
    }

    default <T, U> void trace(Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        if (enableTrace()) {
            write(null, LogLevel.TRACE, ex, String.valueOf(supplier.apply(v1, v2)));
        }
    }

}
