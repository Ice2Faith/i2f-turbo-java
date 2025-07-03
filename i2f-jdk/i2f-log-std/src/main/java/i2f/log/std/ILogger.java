package i2f.log.std;

import i2f.log.std.enums.LogLevel;
import i2f.log.std.perf.PerfSupplier;

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

    default void writeArgs(Object meta, LogLevel level, Object... args) {
        try {
            if (enableLevel(level)) {
                write(meta, level, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void writeArgs(Object meta, LogLevel level, Throwable ex, Object... args) {
        try {
            if (enableLevel(level)) {
                write(meta, level, ex, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void writeArgs(LogLevel level, Object... args) {
        try {
            if (enableLevel(level)) {
                write(null, level, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void writeArgs(LogLevel level, Throwable ex, Object... args) {
        try {
            if (enableLevel(level)) {
                write(null, level, ex, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void write(LogLevel level, String format, Object... args) {
        try {
            if (enableLevel(level)) {
                write(null, level, format, args);
            }
        } catch (Throwable e) {
        }
    }


    default void write(LogLevel level, Throwable ex, String format, Object... args) {
        try {
            if (enableLevel(level)) {
                write(null, level, ex, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void write(Object meta, LogLevel level, Supplier<?> supplier) {
        try {
            if (enableLevel(level)) {
                write(meta, level, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void write(Object meta, LogLevel level, Throwable ex, Supplier<?> supplier) {
        try {
            if (enableLevel(level)) {
                write(meta, level, ex, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void write(LogLevel level, Supplier<?> supplier) {
        try {
            if (enableLevel(level)) {
                write(null, level, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void write(LogLevel level, Throwable ex, Supplier<?> supplier) {
        try {
            if (enableLevel(level)) {
                write(null, level, ex, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }


    default void write(Object meta, LogLevel level, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableLevel(level)) {
                write(meta, level, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void write(Object meta, LogLevel level, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableLevel(level)) {
                write(meta, level, ex, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void write(LogLevel level, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableLevel(level)) {
                write(null, level, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void write(LogLevel level, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableLevel(level)) {
                write(null, level, ex, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }


    default <T> void write(Object meta, LogLevel level, Function<T, ?> supplier, T v1) {
        try {
            if (enableLevel(level)) {
                write(meta, level, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void write(Object meta, LogLevel level, Throwable ex, Function<T, ?> supplier, T v1) {
        try {
            if (enableLevel(level)) {
                write(meta, level, ex, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void write(LogLevel level, Function<T, ?> supplier, T v1) {
        try {
            if (enableLevel(level)) {
                write(null, level, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void write(LogLevel level, Throwable ex, Function<T, ?> supplier, T v1) {
        try {
            if (enableLevel(level)) {
                write(null, level, ex, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }


    default <T, U> void write(Object meta, LogLevel level, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableLevel(level)) {
                write(meta, level, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void write(Object meta, LogLevel level, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableLevel(level)) {
                write(meta, level, ex, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void write(LogLevel level, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableLevel(level)) {
                write(null, level, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void write(LogLevel level, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableLevel(level)) {
                write(null, level, ex, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default void fatalMetaArgs(Object meta, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.FATAL, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void fatalArgs(Object meta, Throwable ex, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.FATAL, ex, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void fatalArgs(Throwable ex, Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.FATAL, ex, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void fatalArgs(Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.FATAL, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void fatalMeta(Object meta, String format, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.FATAL, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void fatal(Object meta, Throwable ex, String format, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.FATAL, ex, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void fatal(String format, Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.FATAL, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void fatal(Throwable ex, String format, Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.FATAL, ex, format, args);
            }
        } catch (Throwable e) {
        }
    }


    default void fatal(Object meta, Supplier<?> supplier) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.FATAL, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void fatal(Object meta, Throwable ex, Supplier<?> supplier) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.FATAL, ex, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void fatal(Supplier<?> supplier) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.FATAL, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void fatal(Throwable ex, Supplier<?> supplier) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.FATAL, ex, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }


    default void fatal(Object meta, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.FATAL, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void fatal(Object meta, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.FATAL, ex, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void fatal(PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.FATAL, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void fatal(Throwable ex, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.FATAL, ex, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void fatal(Object meta, Function<T, ?> supplier, T v1) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.FATAL, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void fatal(Object meta, Throwable ex, Function<T, ?> supplier, T v1) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.FATAL, ex, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void fatal(Function<T, ?> supplier, T v1) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.FATAL, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void fatal(Throwable ex, Function<T, ?> supplier, T v1) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.FATAL, ex, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }


    default <T, U> void fatal(Object meta, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.FATAL, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void fatal(Object meta, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.FATAL, ex, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void fatal(BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.FATAL, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void fatal(Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.FATAL, ex, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default void errorMetaArgs(Object meta, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.ERROR, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void errorArgs(Object meta, Throwable ex, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.ERROR, ex, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void errorArgs(Throwable ex, Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.ERROR, ex, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void errorArgs(Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.ERROR, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void errorMeta(Object meta, String format, Object... args) {
        try {
            if (enableError()) {
                write(meta, LogLevel.ERROR, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void error(Object meta, Throwable ex, String format, Object... args) {
        try {
            if (enableError()) {
                write(meta, LogLevel.ERROR, ex, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void error(String format, Object... args) {
        try {
            if (enableError()) {
                write(null, LogLevel.ERROR, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void error(Throwable ex, String format, Object... args) {
        try {
            if (enableError()) {
                write(null, LogLevel.ERROR, ex, format, args);
            }
        } catch (Throwable e) {
        }
    }


    default void error(Object meta, Supplier<?> supplier) {
        try {
            if (enableError()) {
                write(meta, LogLevel.ERROR, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void error(Object meta, Throwable ex, Supplier<?> supplier) {
        try {
            if (enableError()) {
                write(meta, LogLevel.ERROR, ex, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void error(Supplier<?> supplier) {
        try {
            if (enableError()) {
                write(null, LogLevel.ERROR, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void error(Throwable ex, Supplier<?> supplier) {
        try {
            if (enableError()) {
                write(null, LogLevel.ERROR, ex, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }


    default void error(Object meta, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableError()) {
                write(meta, LogLevel.ERROR, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void error(Object meta, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableError()) {
                write(meta, LogLevel.ERROR, ex, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void error(PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableError()) {
                write(null, LogLevel.ERROR, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void error(Throwable ex, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableError()) {
                write(null, LogLevel.ERROR, ex, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void error(Object meta, Function<T, ?> supplier, T v1) {
        try {
            if (enableError()) {
                write(meta, LogLevel.ERROR, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void error(Object meta, Throwable ex, Function<T, ?> supplier, T v1) {
        try {
            if (enableError()) {
                write(meta, LogLevel.ERROR, ex, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void error(Function<T, ?> supplier, T v1) {
        try {
            if (enableError()) {
                write(null, LogLevel.ERROR, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void error(Throwable ex, Function<T, ?> supplier, T v1) {
        try {
            if (enableError()) {
                write(null, LogLevel.ERROR, ex, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void error(Object meta, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableError()) {
                write(meta, LogLevel.ERROR, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void error(Object meta, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableError()) {
                write(meta, LogLevel.ERROR, ex, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void error(BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableError()) {
                write(null, LogLevel.ERROR, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void error(Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableError()) {
                write(null, LogLevel.ERROR, ex, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default void warnMetaArgs(Object meta, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.WARN, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void warnArgs(Object meta, Throwable ex, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.WARN, ex, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void warnArgs(Throwable ex, Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.WARN, ex, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void warnArgs(Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.WARN, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void warnMeta(Object meta, String format, Object... args) {
        try {
            if (enableWarn()) {
                write(meta, LogLevel.WARN, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void warn(Object meta, Throwable ex, String format, Object... args) {
        try {
            if (enableWarn()) {
                write(meta, LogLevel.WARN, ex, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void warn(String format, Object... args) {
        try {
            if (enableWarn()) {
                write(null, LogLevel.WARN, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void warn(Throwable ex, String format, Object... args) {
        try {
            if (enableWarn()) {
                write(null, LogLevel.WARN, ex, format, args);
            }
        } catch (Throwable e) {
        }
    }


    default void warn(Object meta, Supplier<?> supplier) {
        try {
            if (enableWarn()) {
                write(meta, LogLevel.WARN, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void warn(Object meta, Throwable ex, Supplier<?> supplier) {
        try {
            if (enableWarn()) {
                write(meta, LogLevel.WARN, ex, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void warn(Supplier<?> supplier) {
        try {
            if (enableWarn()) {
                write(null, LogLevel.WARN, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void warn(Throwable ex, Supplier<?> supplier) {
        try {
            if (enableWarn()) {
                write(null, LogLevel.WARN, ex, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void warn(Object meta, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableWarn()) {
                write(meta, LogLevel.WARN, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void warn(Object meta, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableWarn()) {
                write(meta, LogLevel.WARN, ex, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void warn(PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableWarn()) {
                write(null, LogLevel.WARN, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void warn(Throwable ex, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableWarn()) {
                write(null, LogLevel.WARN, ex, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }


    default <T> void warn(Object meta, Function<T, ?> supplier, T v1) {
        try {
            if (enableWarn()) {
                write(meta, LogLevel.WARN, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void warn(Object meta, Throwable ex, Function<T, ?> supplier, T v1) {
        try {
            if (enableWarn()) {
                write(meta, LogLevel.WARN, ex, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void warn(Function<T, ?> supplier, T v1) {
        try {
            if (enableWarn()) {
                write(null, LogLevel.WARN, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void warn(Throwable ex, Function<T, ?> supplier, T v1) {
        try {
            if (enableWarn()) {
                write(null, LogLevel.WARN, ex, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void warn(Object meta, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableWarn()) {
                write(meta, LogLevel.WARN, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void warn(Object meta, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableWarn()) {
                write(meta, LogLevel.WARN, ex, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void warn(BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableWarn()) {
                write(null, LogLevel.WARN, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void warn(Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableWarn()) {
                write(null, LogLevel.WARN, ex, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default void infoMetaArgs(Object meta, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.INFO, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void infoArgs(Object meta, Throwable ex, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.INFO, ex, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void infoArgs(Throwable ex, Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.INFO, ex, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void infoArgs(Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.INFO, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void infoMeta(Object meta, String format, Object... args) {
        try {
            if (enableInfo()) {
                write(meta, LogLevel.INFO, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void info(Object meta, Throwable ex, String format, Object... args) {
        try {
            if (enableInfo()) {
                write(meta, LogLevel.INFO, ex, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void info(String format, Object... args) {
        try {
            if (enableInfo()) {
                write(null, LogLevel.INFO, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void info(Throwable ex, String format, Object... args) {
        try {
            if (enableInfo()) {
                write(null, LogLevel.INFO, ex, format, args);
            }
        } catch (Throwable e) {
        }
    }


    default void info(Object meta, Supplier<?> supplier) {
        try {
            if (enableInfo()) {
                write(meta, LogLevel.INFO, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void info(Object meta, Throwable ex, Supplier<?> supplier) {
        try {
            if (enableInfo()) {
                write(meta, LogLevel.INFO, ex, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void info(Supplier<?> supplier) {
        try {
            if (enableInfo()) {
                write(null, LogLevel.INFO, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void info(Throwable ex, Supplier<?> supplier) {
        try {
            if (enableInfo()) {
                write(null, LogLevel.INFO, ex, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void info(Object meta, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableInfo()) {
                write(meta, LogLevel.INFO, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void info(Object meta, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableInfo()) {
                write(meta, LogLevel.INFO, ex, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void info(PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableInfo()) {
                write(null, LogLevel.INFO, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void info(Throwable ex, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableInfo()) {
                write(null, LogLevel.INFO, ex, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void info(Object meta, Function<T, ?> supplier, T v1) {
        try {
            if (enableInfo()) {
                write(meta, LogLevel.INFO, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void info(Object meta, Throwable ex, Function<T, ?> supplier, T v1) {
        try {
            if (enableInfo()) {
                write(meta, LogLevel.INFO, ex, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void info(Function<T, ?> supplier, T v1) {
        try {
            if (enableInfo()) {
                write(null, LogLevel.INFO, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void info(Throwable ex, Function<T, ?> supplier, T v1) {
        try {
            if (enableInfo()) {
                write(null, LogLevel.INFO, ex, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void info(Object meta, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableInfo()) {
                write(meta, LogLevel.INFO, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void info(Object meta, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableInfo()) {
                write(meta, LogLevel.INFO, ex, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void info(BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableInfo()) {
                write(null, LogLevel.INFO, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void info(Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableInfo()) {
                write(null, LogLevel.INFO, ex, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default void debugMetaArgs(Object meta, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.DEBUG, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void debugArgs(Object meta, Throwable ex, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.DEBUG, ex, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void debugArgs(Throwable ex, Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.DEBUG, ex, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void debugArgs(Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.DEBUG, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void debugMeta(Object meta, String format, Object... args) {
        try {
            if (enableDebug()) {
                write(meta, LogLevel.DEBUG, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void debug(Object meta, Throwable ex, String format, Object... args) {
        try {
            if (enableDebug()) {
                write(meta, LogLevel.DEBUG, ex, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void debug(String format, Object... args) {
        try {
            if (enableDebug()) {
                write(null, LogLevel.DEBUG, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void debug(Throwable ex, String format, Object... args) {
        try {
            if (enableDebug()) {
                write(null, LogLevel.DEBUG, ex, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void debug(Object meta, Supplier<?> supplier) {
        try {
            if (enableDebug()) {
                write(meta, LogLevel.DEBUG, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void debug(Object meta, Throwable ex, Supplier<?> supplier) {
        try {
            if (enableDebug()) {
                write(meta, LogLevel.DEBUG, ex, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void debug(Supplier<?> supplier) {
        try {
            if (enableDebug()) {
                write(null, LogLevel.DEBUG, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void debug(Throwable ex, Supplier<?> supplier) {
        try {
            if (enableDebug()) {
                write(null, LogLevel.DEBUG, ex, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void debug(Object meta, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableDebug()) {
                write(meta, LogLevel.DEBUG, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void debug(Object meta, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableDebug()) {
                write(meta, LogLevel.DEBUG, ex, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void debug(PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableDebug()) {
                write(null, LogLevel.DEBUG, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void debug(Throwable ex, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableDebug()) {
                write(null, LogLevel.DEBUG, ex, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void debug(Object meta, Function<T, ?> supplier, T v1) {
        try {
            if (enableDebug()) {
                write(meta, LogLevel.DEBUG, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void debug(Object meta, Throwable ex, Function<T, ?> supplier, T v1) {
        try {
            if (enableDebug()) {
                write(meta, LogLevel.DEBUG, ex, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void debug(Function<T, ?> supplier, T v1) {
        try {
            if (enableDebug()) {
                write(null, LogLevel.DEBUG, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void debug(Throwable ex, Function<T, ?> supplier, T v1) {
        try {
            if (enableDebug()) {
                write(null, LogLevel.DEBUG, ex, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void debug(Object meta, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableDebug()) {
                write(meta, LogLevel.DEBUG, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void debug(Object meta, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableDebug()) {
                write(meta, LogLevel.DEBUG, ex, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void debug(BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableDebug()) {
                write(null, LogLevel.DEBUG, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void debug(Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableDebug()) {
                write(null, LogLevel.DEBUG, ex, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default void traceMetaArgs(Object meta, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.TRACE, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void traceArgs(Object meta, Throwable ex, Object... args) {
        try {
            if (enableFatal()) {
                write(meta, LogLevel.TRACE, ex, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void traceArgs(Throwable ex, Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.TRACE, ex, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void traceArgs(Object... args) {
        try {
            if (enableFatal()) {
                write(null, LogLevel.TRACE, (String) null, args);
            }
        } catch (Throwable e) {
        }
    }

    default void traceMeta(Object meta, String format, Object... args) {
        try {
            if (enableTrace()) {
                write(meta, LogLevel.TRACE, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void trace(Object meta, Throwable ex, String format, Object... args) {
        try {
            if (enableTrace()) {
                write(meta, LogLevel.TRACE, ex, format, args);
            }
        } catch (Throwable e) {
        }
    }


    default void trace(String format, Object... args) {
        try {
            if (enableTrace()) {
                write(null, LogLevel.TRACE, format, args);
            }
        } catch (Throwable e) {
        }
    }

    default void trace(Throwable ex, String format, Object... args) {
        try {
            if (enableTrace()) {
                write(null, LogLevel.TRACE, ex, format, args);
            }
        } catch (Throwable e) {
        }
    }


    default void trace(Object meta, Supplier<?> supplier) {
        try {
            if (enableTrace()) {
                write(meta, LogLevel.TRACE, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void trace(Object meta, Throwable ex, Supplier<?> supplier) {
        try {
            if (enableTrace()) {
                write(meta, LogLevel.TRACE, ex, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }


    default void trace(Supplier<?> supplier) {
        try {
            if (enableTrace()) {
                write(null, LogLevel.TRACE, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void trace(Throwable ex, Supplier<?> supplier) {
        try {
            if (enableTrace()) {
                write(null, LogLevel.TRACE, ex, String.valueOf(supplier.get()));
            }
        } catch (Throwable e) {
        }
    }

    default void trace(Object meta, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableTrace()) {
                write(meta, LogLevel.TRACE, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void trace(Object meta, Throwable ex, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableTrace()) {
                write(meta, LogLevel.TRACE, ex, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void trace(PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableTrace()) {
                write(null, LogLevel.TRACE, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default void trace(Throwable ex, PerfSupplier<?> supplier, Object... args) {
        try {
            if (enableTrace()) {
                write(null, LogLevel.TRACE, ex, String.valueOf(supplier.get(args)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void trace(Object meta, Function<T, ?> supplier, T v1) {
        try {
            if (enableTrace()) {
                write(meta, LogLevel.TRACE, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void trace(Object meta, Throwable ex, Function<T, ?> supplier, T v1) {
        try {
            if (enableTrace()) {
                write(meta, LogLevel.TRACE, ex, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void trace(Function<T, ?> supplier, T v1) {
        try {
            if (enableTrace()) {
                write(null, LogLevel.TRACE, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T> void trace(Throwable ex, Function<T, ?> supplier, T v1) {
        try {
            if (enableTrace()) {
                write(null, LogLevel.TRACE, ex, String.valueOf(supplier.apply(v1)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void trace(Object meta, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableTrace()) {
                write(meta, LogLevel.TRACE, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void trace(Object meta, Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableTrace()) {
                write(meta, LogLevel.TRACE, ex, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void trace(BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableTrace()) {
                write(null, LogLevel.TRACE, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

    default <T, U> void trace(Throwable ex, BiFunction<T, U, ?> supplier, T v1, U v2) {
        try {
            if (enableTrace()) {
                write(null, LogLevel.TRACE, ex, String.valueOf(supplier.apply(v1, v2)));
            }
        } catch (Throwable e) {
        }
    }

}
