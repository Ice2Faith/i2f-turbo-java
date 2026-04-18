package i2f.extension.groovy.delegating;

import groovy.lang.DelegatingMetaClass;
import groovy.lang.MetaClass;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ice2Faith
 * @date 2026/4/17 9:22
 * @desc
 */
@Getter
@Setter
public class GroovyScriptDelegatingMetaClass extends DelegatingMetaClass {
    protected GroovyInvocationInterceptor interceptor;

    public GroovyScriptDelegatingMetaClass(MetaClass delegate, GroovyInvocationInterceptor interceptor) {
        super(delegate);
        this.interceptor = interceptor;
    }

    public GroovyScriptDelegatingMetaClass(Class theClass, GroovyInvocationInterceptor interceptor) {
        super(theClass);
        this.interceptor = interceptor;
    }

    @Override
    public Object invokeMissingMethod(Object instance, String methodName, Object[] arguments) {
        if (interceptor == null) {
            return super.invokeMissingMethod(instance, methodName, arguments);
        }
        try {
            GroovyInvocation invocation = new GroovyInvocation(GroovyInvocation.Type.invokeMissingMethod, instance, methodName, arguments);
            GroovyInvoker invoker = (e) -> {
                return super.invokeMissingMethod(e.getInstance(), e.getMethod(), e.getArgs());
            };
            return interceptor.proceed(invocation, invoker);
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public Object invokeMethod(String name, Object args) {
        if (interceptor == null) {
            return super.invokeMethod(name, args);
        }
        GroovyInvocation invocation = new GroovyInvocation(GroovyInvocation.Type.invokeMethodWithoutInstance, null, name, (Object[]) args);
        GroovyInvoker invoker = (e) -> {
            return super.invokeMethod(e.getMethod(), e.getArgs());
        };
        try {
            return interceptor.proceed(invocation, invoker);
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public Object invokeMethod(Object object, String methodName, Object args) {
        if (interceptor == null) {
            return super.invokeMethod(object, methodName, args);
        }
        try {
            GroovyInvocation invocation = new GroovyInvocation(GroovyInvocation.Type.invokeMethod, null, methodName, (Object[]) args);
            GroovyInvoker invoker = (e) -> {
                return super.invokeMethod(e.getInstance(), e.getMethod(), e.getArgs());
            };
            return interceptor.proceed(invocation, invoker);
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public Object invokeMethod(Object object, String methodName, Object[] arguments) {
        if (interceptor == null) {
            return super.invokeMethod(object, methodName, arguments);
        }
        try {
            GroovyInvocation invocation = new GroovyInvocation(GroovyInvocation.Type.invokeMethod, null, methodName, arguments);
            GroovyInvoker invoker = (e) -> {
                return super.invokeMethod(e.getInstance(), e.getMethod(), e.getArgs());
            };
            return interceptor.proceed(invocation, invoker);
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public Object invokeStaticMethod(Object object, String methodName, Object[] arguments) {
        if (interceptor == null) {
            return super.invokeStaticMethod(object, methodName, arguments);
        }
        try {
            GroovyInvocation invocation = new GroovyInvocation(GroovyInvocation.Type.invokeStaticMethod, object, methodName, arguments);
            GroovyInvoker invoker = (e) -> {
                return super.invokeStaticMethod(e.getInstance(), e.getMethod(), e.getArgs());
            };
            return interceptor.proceed(invocation, invoker);
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
