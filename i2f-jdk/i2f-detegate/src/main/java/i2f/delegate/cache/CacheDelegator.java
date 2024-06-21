package i2f.delegate.cache;

import i2f.functional.consumer.except.impl.IExConsumer3;
import i2f.functional.func.except.impl.IExFunction1;
import i2f.functional.func.except.impl.IExFunction2;

import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/6/21 15:51
 * @desc 缓存委托
 */
public class CacheDelegator {
    public static <T, U, R> R cache(T arg, U cacheHolder,
                                    IExFunction2<R, T, U> cacheSupplier,
                                    IExFunction1<R, T> supplier
    ) throws Throwable {
        return cache(arg, cacheHolder, cacheSupplier, supplier, null, null, null);
    }

    public static <T, U, R> R cache(T arg, U cacheHolder,
                                    IExFunction2<R, T, U> cacheSupplier,
                                    IExFunction1<R, T> supplier,
                                    IExConsumer3<R, T, U> cacheSaver
    ) throws Throwable {
        return cache(arg, cacheHolder, cacheSupplier, supplier, cacheSaver, null, null);
    }

    public static <T, U, R> R cache(T arg, U cacheHolder,
                                    IExFunction2<R, T, U> cacheSupplier,
                                    IExFunction1<R, T> supplier,
                                    IExConsumer3<R, T, U> cacheSaver,
                                    Predicate<R> cacheConfirmer
    ) throws Throwable {
        return cache(arg, cacheHolder, cacheSupplier, supplier, cacheSaver, cacheConfirmer, null);
    }

    /**
     * 缓存委托
     * 对supplier的返回值进行缓存
     * 如果cacheSupplier在cacheHolder中能够获取到cacheConfirmer确认的值，则直接返回
     * 否则获取supplier的返回值并进行cacheSaver保存之后返回
     * 同时，支持如果直接拿到缓存的情况下，需要进行刷新的场景的cacheRefresher
     *
     * @param arg            supplier的调用参数
     * @param cacheHolder    缓存的持有对象或者提供者
     * @param cacheSupplier  从缓存获取值，允许为null,则不使用缓存
     * @param supplier       直接获取数据
     * @param cacheSaver     保存数据到缓存
     * @param cacheConfirmer 确认从缓存获取到的数据正确
     * @param cacheRefresher 缓存刷新器，在直接获得缓存数据的情况下，支持刷新此缓存
     * @param <T>            参数类型
     * @param <U>            缓存类型
     * @param <R>            返回值类型
     * @return
     * @throws Throwable
     */
    public static <T, U, R> R cache(T arg, U cacheHolder,
                                    IExFunction2<R, T, U> cacheSupplier,
                                    IExFunction1<R, T> supplier,
                                    IExConsumer3<R, T, U> cacheSaver,
                                    Predicate<R> cacheConfirmer,
                                    IExConsumer3<R, T, U> cacheRefresher
    ) throws Throwable {
        if (cacheSupplier != null) {
            R ret = cacheSupplier.apply(arg, cacheHolder);
            if (cacheConfirmer == null || cacheConfirmer.test(ret)) {
                if (cacheRefresher != null) {
                    cacheRefresher.accept(ret, arg, cacheHolder);
                }
                return ret;
            }
        }
        R ret = supplier.apply(arg);
        if (cacheSaver != null) {
            cacheSaver.accept(ret, arg, cacheHolder);
        }
        return ret;
    }
}
