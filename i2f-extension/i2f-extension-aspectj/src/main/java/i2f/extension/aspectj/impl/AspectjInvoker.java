package i2f.extension.aspectj.impl;

import i2f.invokable.method.impl.jdk.JdkMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author Ice2Faith
 * @date 2022/3/26 19:27
 * @desc
 */
public class AspectjInvoker extends JdkMethod {
    protected ProceedingJoinPoint pjp;

    public AspectjInvoker(ProceedingJoinPoint pjp) {
        super(((MethodSignature) pjp.getSignature()).getMethod());
        this.pjp = pjp;
    }

    @Override
    public Object invoke(Object ivkObj, Object... args) throws Throwable {
        return pjp.proceed(args);
    }

}
