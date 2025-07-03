package i2f.extension.aspectj;


import i2f.proxy.std.IProxyInvocationHandler;
import i2f.proxy.std.IProxyProvider;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Ice2Faith
 * @date 2022/3/26 19:23
 * @desc
 */
public class AspectjProxyProvider implements IProxyProvider {

    @Override
    public <T> T proxy(Object obj, IProxyInvocationHandler handler) {
        return (T) AspectjUtil.proxy((ProceedingJoinPoint) obj, handler);
    }
}
