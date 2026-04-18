package i2f.extension.groovy.delegating;

/**
 * @author Ice2Faith
 * @date 2026/4/17 9:20
 * @desc
 */
public interface GroovyInvocationInterceptor {
    Object proceed(GroovyInvocation invocation, GroovyInvoker invoker) throws Throwable;
}
