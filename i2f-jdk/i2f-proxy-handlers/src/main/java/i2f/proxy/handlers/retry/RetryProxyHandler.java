package i2f.proxy.handlers.retry;

import i2f.annotations.ext.call.Retry;
import i2f.invokable.IInvokable;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.proxy.std.IProxyInvocationHandler;

import java.lang.reflect.Method;

/**
 * 重试代理
 * 对方法上具有 @Retry 的方法调用时进行重试操作
 *
 * @author Ice2Faith
 * @date 2025/7/23 15:18
 */
public class RetryProxyHandler implements IProxyInvocationHandler {
    @Override
    public Object invoke(Object ivkObj, IInvokable invokable, Object... args) throws Throwable {
        if (!(invokable instanceof JdkMethod)) {
            throw new IllegalStateException("un-support invokable type=" + invokable.getClass());
        }

        JdkMethod jdkMethod = (JdkMethod) invokable;
        Method method = jdkMethod.getMethod();

        Retry ann = method.getDeclaredAnnotation(Retry.class);
        if (ann == null) {
            return invokable.invoke(ivkObj, args);
        }

        double defaultSleepMs = ann.unit().toMillis(ann.delay());
        double maxSleepMs = ann.unit().toMillis(ann.maxDelay());
        Class<? extends Throwable>[] breakTypes = ann.breakOn();

        int maxCount = ann.value();
        double sleepMs = defaultSleepMs;
        double multiplier = ann.multiplier();

        Throwable ex = null;

        do {
            try {
                return invokable.invoke(ivkObj, args);
            } catch (Throwable e) {
                ex = e;
                Class<? extends Throwable> type = e.getClass();
                if (breakTypes != null) {
                    for (Class<? extends Throwable> breakType : breakTypes) {
                        if (breakType == type
                                || breakType.isAssignableFrom(type)) {
                            throw e;
                        }
                    }
                }

                sleepMs = sleepMs * multiplier;
                if (sleepMs <= 0) {
                    sleepMs = 0;
                }
                if (sleepMs > maxSleepMs) {
                    sleepMs = maxSleepMs;
                }

                try {
                    Thread.sleep((long) sleepMs);
                } catch (Exception ee) {

                }

                maxCount--;
            }
        } while (maxCount > 0);

        throw ex;
    }
}
