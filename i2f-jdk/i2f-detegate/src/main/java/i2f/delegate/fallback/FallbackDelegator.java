package i2f.delegate.fallback;

import i2f.functional.func.except.impl.IExFunction1;
import i2f.functional.func.except.impl.IExFunction2;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/6/21 15:10
 * @desc 降级委托
 */
public class FallbackDelegator {
    public static <T, R> R fallback(T arg,
                                    IExFunction1<R, T> supplier,
                                    IExFunction2<R, T, Throwable> fallbacker,
                                    int retries
    ) throws Throwable {
        return fallback(arg, supplier, fallbacker, retries, null, -1, -1, null, null, null);
    }

    public static <T, R> R fallback(T arg,
                                    IExFunction1<R, T> supplier,
                                    IExFunction2<R, T, Throwable> fallbacker,
                                    int retries,
                                    Predicate<R> confirmer
    ) throws Throwable {
        return fallback(arg, supplier, fallbacker, retries, confirmer, -1, -1, null, null, null);
    }

    public static <T, R> R fallback(T arg,
                                    IExFunction1<R, T> supplier,
                                    IExFunction2<R, T, Throwable> fallbacker,
                                    int retries,
                                    Predicate<R> confirmer,
                                    int sleepTs
    ) throws Throwable {
        return fallback(arg, supplier, fallbacker, retries, confirmer, sleepTs, -1, null, null, null);
    }

    public static <T, R> R fallback(T arg,
                                    IExFunction1<R, T> supplier,
                                    IExFunction2<R, T, Throwable> fallbacker,
                                    int retries,
                                    Predicate<R> confirmer,
                                    int sleepTs,
                                    double multiplier
    ) throws Throwable {
        return fallback(arg, supplier, fallbacker, retries, confirmer, sleepTs, multiplier, null, null, null);
    }

    public static <T, R> R fallback(T arg,
                                    IExFunction1<R, T> supplier,
                                    IExFunction2<R, T, Throwable> fallbacker,
                                    int retries,
                                    Predicate<R> confirmer,
                                    int sleepTs,
                                    double multiplier,
                                    Predicate<Throwable> breaker
    ) throws Throwable {
        return fallback(arg, supplier, fallbacker, retries, confirmer, sleepTs, multiplier, breaker, null, null);
    }

    public static <T, R> R fallback(T arg,
                                    IExFunction1<R, T> supplier,
                                    IExFunction2<R, T, Throwable> fallbacker,
                                    int retries,
                                    Predicate<R> confirmer,
                                    int sleepTs,
                                    double multiplier,
                                    Predicate<Throwable> breaker,
                                    Predicate<Throwable> thrower
    ) throws Throwable {
        return fallback(arg, supplier, fallbacker, retries, confirmer, sleepTs, multiplier, breaker, thrower, null);
    }

    /**
     * 降级委托
     * 对supplier尝试最多执行retries次数，执行成功且confirmer确认结果有效就返回
     * 否则执行fallbacker进行降级获取返回值
     * 特别的
     * 如果发生的异常被thrower判定为true，则直接抛出这个异常，结束执行
     * 如果发生的异常被breaker判定为true，则跳出尝试，进行fallbacker流程
     * 每次尝试，支持调用延迟，通过sleepTs和multiplier进行实现
     * 每次延迟=上一次延迟*multiplier
     * 这样是为了适用于期待错误是短暂发生的，能够尽快恢复
     * 尝试次数越多，期待的错误恢复时间可能就更长
     * 因此multiplier一般是>1的
     * 举例来说，初始等待为sleepTs=100，multiplier增量为1.1
     * 则在执行retries=5的情况下，等待延迟如下
     * 0,100,110,121,133
     *
     * @param arg                提供一个调用参数
     * @param supplier           正常执行的内容，接受arg返回数据
     * @param fallbacker         降级处理的内容，接受arg和最后一次异常ex,返回数据，允许为null，则fallback返回为null
     * @param retries            尝试的最大次数，应>0,当<=0时，默认为1，默认执行一次
     * @param confirmer          返回值的确认，用于确认返回值是否是正常结果，允许为null,则supplier执行只要不发生异常，视为正确返回
     * @param sleepTs            睡眠等待下次调用的初始时间，当>=0时生效，<0时不进行睡眠等待
     * @param multiplier         睡眠等待增量系数，当>0时生效，<0时表示不使用增量，默认为1
     * @param breaker            尝试中断器，用于判定遇到指定异常时是否要中断尝试，直接进入fallback流程，允许为null，则不进行中断
     * @param thrower            尝试抛出器，用于判定遇到指定异常时，是否直接进行抛出异常，不再进行尝试，不进入fallback流程，允许为null，则不仅如此此逻辑
     * @param exceptionCollector 异常收集器，可用于收集尝试过程中的异常信息，允许为null，则此功能不生效
     * @param <T>                参数类型
     * @param <R>                返回类型
     * @return
     * @throws Throwable
     */
    public static <T, R> R fallback(T arg,
                                    IExFunction1<R, T> supplier,
                                    IExFunction2<R, T, Throwable> fallbacker,
                                    int retries,
                                    Predicate<R> confirmer,
                                    int sleepTs,
                                    double multiplier,
                                    Predicate<Throwable> breaker,
                                    Predicate<Throwable> thrower,
                                    Consumer<Throwable> exceptionCollector
    ) throws Throwable {
        if (retries <= 0) {
            retries = 1;
        }
        if (multiplier <= 0) {
            multiplier = 1;
        }
        double sleep = sleepTs;
        Throwable ex = null;
        for (int i = 0; i < retries; i++) {
            try {
                R ret = supplier.apply(arg);
                if (confirmer == null) {
                    return ret;
                }
                if (confirmer.test(ret)) {
                    return ret;
                }
            } catch (Throwable e) {
                ex = e;
                try {
                    if (exceptionCollector != null) {
                        exceptionCollector.accept(e);
                    }
                } catch (Throwable ep) {

                }
                if (thrower != null && thrower.test(e)) {
                    throw e;
                }
                if (breaker != null && breaker.test(e)) {
                    break;
                }
                if (sleepTs >= 0) {
                    try {
                        Thread.sleep((int) sleep);
                        sleep *= multiplier;
                    } catch (Exception ee) {

                    }
                }
            }
        }
        if (fallbacker == null) {
            return null;
        }
        R ret = fallbacker.apply(arg, ex);
        return ret;
    }
}
