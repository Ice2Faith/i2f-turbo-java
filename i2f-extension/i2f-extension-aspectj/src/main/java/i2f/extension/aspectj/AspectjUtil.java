package i2f.extension.aspectj;


import i2f.extension.aspectj.core.AspectjProxy;
import i2f.proxy.IProxyHandler;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author ltb
 * @date 2022/3/26 19:42
 * @desc
 */
public class AspectjUtil {
    public static AspectjProxy proxy(IProxyHandler handler) {
        return new AspectjProxyProvider().proxyNative(handler);
    }

    public static Object aop(ProceedingJoinPoint pjp, IProxyHandler handler) throws Throwable {
        AspectjProxy proxy = proxy(handler);
        return proxy.invoke(pjp);
    }
}
