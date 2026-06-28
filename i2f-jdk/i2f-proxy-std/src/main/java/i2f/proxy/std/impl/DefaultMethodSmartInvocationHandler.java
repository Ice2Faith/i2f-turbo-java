package i2f.proxy.std.impl;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2026/5/14 10:39
 * @desc
 */
public class DefaultMethodSmartInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 1. 处理 Object 类自带的方法（如 toString, hashCode 等）
        if (Object.class.equals(method.getDeclaringClass())) {
            return handleObjectMethod(proxy, method, args);
        }

        // 处理默认方法，default 实现的方法不进行代理
        // 这里就允许使用默认方法实现一些方法
        if (method.isDefault()) {
            return handleDefaultMethod(proxy, method, args);
        }

        return handleProxyMethod(proxy, method, args);
    }

    public Object handleObjectMethod(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(this, args);
    }

    public Object handleDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取特殊调用的 MethodHandle (unreflectSpecial)
        // 这里的关键是指定查找的起点是接口，并且绑定 this 为 proxy
        MethodHandle mh = MethodHandlesUtil.getDefaultMethodHandle(method);

        // 绑定实例（proxy）并调用
        return mh.bindTo(proxy).invokeWithArguments(args);
    }

    public Object handleProxyMethod(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(this, args);
    }
}
