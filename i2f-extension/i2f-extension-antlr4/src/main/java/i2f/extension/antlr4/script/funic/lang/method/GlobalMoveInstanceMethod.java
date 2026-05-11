package i2f.extension.antlr4.script.funic.lang.method;

import i2f.invokable.method.IMethod;
import i2f.invokable.method.MethodWrapper;

import java.lang.reflect.Modifier;

/**
 * @author Ice2Faith
 * @date 2026/5/11 14:41
 * @desc
 */
public class GlobalMoveInstanceMethod extends MethodWrapper {

    public GlobalMoveInstanceMethod(IMethod method) {
        super(method);
    }

    @Override
    public int getModifiers() {
        return super.getModifiers() & (~Modifier.STATIC) | Modifier.PUBLIC;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        Class<?>[] arr = super.getParameterTypes();
        Class<?>[] ret = new Class[arr.length - 1];
        System.arraycopy(arr, 1, ret, 0, ret.length);
        return ret;
    }

    @Override
    public int getParameterCount() {
        return super.getParameterCount() - 1;
    }

    @Override
    public Object invoke(Object ivkObj, Object... args) throws Throwable {
        Object[] callArgs = new Object[args.length + 1];
        callArgs[0] = ivkObj;
        System.arraycopy(args, 0, callArgs, 1, args.length);
        return super.invoke(null, callArgs);
    }
}
