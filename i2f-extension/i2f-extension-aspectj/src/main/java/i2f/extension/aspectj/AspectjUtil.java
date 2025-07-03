package i2f.extension.aspectj;


import i2f.extension.aspectj.impl.AspectjInvoker;
import i2f.invokable.IInvokable;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.proxy.JdkProxyUtil;
import i2f.proxy.std.IProxyHandler;
import i2f.proxy.std.IProxyInvocationHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Ice2Faith
 * @date 2022/3/26 19:42
 * @desc
 */
public class AspectjUtil {
    public static ProceedingJoinPoint proxy(ProceedingJoinPoint pjp, IProxyHandler handler) {
        return proxy(pjp, IProxyInvocationHandler.of(handler));
    }

    public static ProceedingJoinPoint proxy(ProceedingJoinPoint pjp, IProxyInvocationHandler handler) {
        ProceedingJoinPoint proxy = JdkProxyUtil.proxy(pjp, (IProxyInvocationHandler) (ivkObj, invokable, args) -> {
            JdkMethod invoker = (JdkMethod) invokable;
            Method method = invoker.getMethod();
            if (method.getName().equals("proceed")) {
                return aop(pjp, handler);
            }
            return invokable.invoke(ivkObj, args);
        });
        return proxy;
    }

    public static Object aop(ProceedingJoinPoint pjp, IProxyInvocationHandler handler) throws Throwable {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
        Class clazz = method.getDeclaringClass();
        Parameter[] params = method.getParameters();
        Object[] args = pjp.getArgs();
        Object target = pjp.getTarget();

        IInvokable invokable = new AspectjInvoker(pjp);
        return handler.invoke(pjp, invokable, args);
    }
}
