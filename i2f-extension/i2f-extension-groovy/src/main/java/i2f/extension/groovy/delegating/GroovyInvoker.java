package i2f.extension.groovy.delegating;

/**
 * @author Ice2Faith
 * @date 2026/4/17 9:19
 * @desc
 */
public interface GroovyInvoker {
    Object invoke(GroovyInvocation invocation) throws Throwable;
}
