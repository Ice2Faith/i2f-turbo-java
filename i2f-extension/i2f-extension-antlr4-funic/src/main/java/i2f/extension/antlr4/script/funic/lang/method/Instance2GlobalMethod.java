package i2f.extension.antlr4.script.funic.lang.method;

import i2f.invokable.method.IMethod;
import i2f.invokable.method.MethodWrapper;

import java.lang.reflect.Modifier;

/**
 * @author Ice2Faith
 * @date 2026/6/16 17:31
 * @desc FP(函数式编程)->OOP(面向对象编程)
 * 属于，统一函数调用语法（Uniform Function Call Syntax, 简称 UFCS）
 * 允许像使用函数式编程一样使用对象的实例函数
 * 当调用的全局函数，第一个参数不为空时，可以借用实例对象的实例函数
 * 规则：name(arg0,...args) -> arg0.name(...args)
 * 因此，这种委托调用需要第一个参数不为空
 */
public class Instance2GlobalMethod extends MethodWrapper {
    public Instance2GlobalMethod(IMethod method) {
        super(method);
    }

    @Override
    public int getModifiers() {
        return super.getModifiers() & (Modifier.STATIC) | Modifier.PUBLIC;
    }

    @Override
    public Object invoke(Object ivkObj, Object... args) throws Throwable {
        if (args.length < 1) {
            throw new IllegalArgumentException("UFCS expect least one argument.");
        }
        if (args.length == 1) {
            return super.invoke(args[0]);
        }
        Object[] arr = new Object[args.length - 1];
        System.arraycopy(args, 1, arr, 0, arr.length);
        return super.invoke(args[0], arr);
    }

    @Override
    public int getParameterCount() {
        return super.getParameterCount() + 1;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        Class<?>[] superTypes = super.getParameterTypes();
        Class<?>[] ret = new Class[superTypes.length + 1];
        System.arraycopy(superTypes, 0, ret, 1, superTypes.length);
        ret[0] = method.getDeclaringClass();
        return ret;
    }
}
