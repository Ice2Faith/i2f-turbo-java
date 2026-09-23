package i2f.extension.antlr4.script.funic.lang.method;

import i2f.invokable.method.IMethod;
import i2f.invokable.method.MethodWrapper;

import java.lang.reflect.Modifier;

/**
 * @author Ice2Faith
 * @date 2026/5/11 14:41
 * @desc OOP(面向对象编程)->FP(函数式编程)
 * 属于，统一函数调用语法（Uniform Function Call Syntax, 简称 UFCS）
 * 允许像使用面向对象编程一样使用函数式函数
 * 当调用对象的函数时，可以拓展查找全局的函数式函数，作为实例函数一样使用
 * 规则：arg0.name(...args) -> name(arg0,...args)
 * 因此，这是一种无侵入式的对象行为拓展
 */
public class Global2InstanceMethod extends MethodWrapper {

    public Global2InstanceMethod(IMethod method) {
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
