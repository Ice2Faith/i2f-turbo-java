package i2f.proxy;


import i2f.invokable.IInvokable;

/**
 * @author Ice2Faith
 * @date 2022/3/25 20:29
 * @desc
 */
public interface IProxyHandler {

    Object initContext();

    /**
     * 在调用之前Hook
     * 返回值不为null,则表示提前返回，不再执行代理对象的调用
     *
     * @param ivkObj
     * @param invokable
     * @param args
     * @return
     */
    Object before(Object context, Object ivkObj, IInvokable invokable, Object... args);

    /**
     * 在调用之后Hook，返回值为调用返回后的值
     * 一般情况下，返回值应该和第三个入参retVal一致
     *
     * @param ivkObj
     * @param invokable
     * @param retVal
     * @param args
     * @return
     */
    Object after(Object context, Object ivkObj, IInvokable invokable, Object retVal, Object... args);

    /**
     * 在调用发生异常时Hook，返回值为调用抛出的异常对象
     * 一般情况下，返回值应该和第三个入参ex一致
     *
     * @param ivkObj
     * @param invokable
     * @param ex
     * @param args
     * @return
     */
    Throwable except(Object context, Object ivkObj, IInvokable invokable, Throwable ex, Object... args);

    /**
     * 调用完毕之后执行finally块
     * 主要是提供完成的代理流程
     *
     * @param ivkObj
     * @param invokable
     * @param args
     * @return
     */
    void onFinally(Object context, Object ivkObj, IInvokable invokable, Object... args);
}
