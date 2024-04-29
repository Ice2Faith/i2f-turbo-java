package i2f.proxy.impl;

import i2f.proxy.IInvokable;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2022/3/28 9:14
 * @desc
 */
public interface IMethodAccessInvokable extends IInvokable {
    Method method();
}
