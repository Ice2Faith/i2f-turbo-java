package i2f.extension.aspectj.test;

import i2f.extension.aspectj.AspectjUtil;
import i2f.extension.aspectj.impl.AspectjInvoker;
import i2f.invokable.IInvokable;
import i2f.proxy.std.IProxyInvocationHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2025/7/3 14:16
 */
@Aspect
public class TestAspect {

    public static void main(String[] args) {
        TestService service = new TestService();
        service.sayHello();
        service.sayWorld();
        System.out.println("--------------");
        for (int i = 0; i < 4; i++) {
            service.say("123");
        }

        System.out.println("ok");
    }

    @Pointcut("execution(* i2f.extension.aspectj.test.TestService.*(..))")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        ProceedingJoinPoint ppjp = AspectjUtil.proxy(pjp, new IProxyInvocationHandler() {
            @Override
            public Object invoke(Object ivkObj, IInvokable invokable, Object... args) throws Throwable {
                AspectjInvoker invoker = (AspectjInvoker) invokable;
                Method method = invoker.getMethod();
                System.out.println("proxy invoke: " + method.getName());
                if (method.getName().equals("say")) {
                    if (Math.random() < 0.5) {
                        args[0] = "modify:" + args[0];
                    }
                }
                return invokable.invoke(ivkObj, args);
            }
        });
        System.out.println("start ...");
        Object ret = ppjp.proceed();
        System.out.println("end ...");
        return ret;
    }


}
