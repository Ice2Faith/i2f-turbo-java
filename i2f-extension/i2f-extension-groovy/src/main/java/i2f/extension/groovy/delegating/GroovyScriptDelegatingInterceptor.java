package i2f.extension.groovy.delegating;

import i2f.invokable.method.IMethod;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.reflect.ReflectResolver;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2026/4/17 9:43
 * @desc
 */
@Data
@NoArgsConstructor
public class GroovyScriptDelegatingInterceptor implements GroovyInvocationInterceptor {
    protected CopyOnWriteArrayList<Object> methodsProviders = new CopyOnWriteArrayList<>();

    public GroovyScriptDelegatingInterceptor(Object... providers) {
        this.methodsProviders.addAll(Arrays.asList(providers));
    }

    public GroovyScriptDelegatingInterceptor(List<Object> providers) {
        this.methodsProviders.addAll(providers);
    }

    @Override
    public Object proceed(GroovyInvocation invocation, GroovyInvoker invoker) throws Throwable {
        String methodName = invocation.getMethod();
        Object[] args = invocation.getArgs();
        Class<?> callClass = null;
        IMethod callMethod = null;
        Object callInstance = null;
        for (Object provider : methodsProviders) {
            if (provider instanceof IMethod) {
                IMethod method = (IMethod) provider;
                callMethod = ReflectResolver.matchExecMethod(Collections.singletonList(method), Arrays.asList(args));
                if (callMethod != null) {
                    callClass = callMethod.getDeclaringClass();
                }
            } else if (provider instanceof Method) {
                Method method = (Method) provider;
                Method candidateMethod = ReflectResolver.matchExecutable(Collections.singletonList(method), Arrays.asList(args));
                if (candidateMethod != null) {
                    callMethod = new JdkMethod(candidateMethod);
                }
                if (callMethod != null) {
                    callClass = method.getDeclaringClass();
                }
            } else if (provider instanceof Class) {
                Class<?> clazz = (Class<?>) provider;
                Method candidateMethod = ReflectResolver.matchExecMethod(clazz, methodName, Arrays.asList(args));
                if (candidateMethod != null) {
                    callMethod = new JdkMethod(candidateMethod);
                }
                if (callMethod != null) {
                    callClass = clazz;
                }
            } else {
                Class<?> clazz = provider.getClass();
                Method candidateMethod = ReflectResolver.matchExecMethod(clazz, methodName, Arrays.asList(args));
                if (candidateMethod != null) {
                    callMethod = new JdkMethod(candidateMethod);
                }
                if (callMethod != null) {
                    callClass = clazz;
                    callInstance = provider;
                }
            }
            if (callMethod != null) {
                break;
            }
        }

        if (callMethod != null) {
            if (!Modifier.isStatic(callMethod.getModifiers())) {
                if (callInstance == null) {
                    callInstance = ReflectResolver.getInstance(callClass);
                }
            }
            return ReflectResolver.execMethod(callInstance, callMethod, Arrays.asList(args));
        }

        return invoker.invoke(invocation);
    }
}
