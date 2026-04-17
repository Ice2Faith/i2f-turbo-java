package i2f.extension.groovy.delegating;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/4/17 9:17
 * @desc
 */
@Data
@NoArgsConstructor
public class GroovyInvocation {

    protected Type type;
    protected Object instance;
    protected String method;
    protected Object[] args;

    public GroovyInvocation(Type type, Object instance, String method, Object[] args) {
        this.type = type;
        this.instance = instance;
        this.method = method;
        this.args = args;
    }

    public static enum Type {
        invokeMissingMethod,
        invokeMethod,
        invokeMethodWithoutInstance,
        invokeStaticMethod
    }
}
