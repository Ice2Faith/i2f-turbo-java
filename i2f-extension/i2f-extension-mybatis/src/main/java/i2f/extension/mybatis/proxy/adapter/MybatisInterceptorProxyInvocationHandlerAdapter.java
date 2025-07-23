package i2f.extension.mybatis.proxy.adapter;


import i2f.invokable.IInvokable;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.proxy.std.IProxyInvocationHandler;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author Ice2Faith
 * @date 2022/3/26 17:57
 * @desc
 */
@Data
@NoArgsConstructor
public class MybatisInterceptorProxyInvocationHandlerAdapter implements Interceptor {
    protected Properties properties = new Properties();

    protected IProxyInvocationHandler handler;

    public MybatisInterceptorProxyInvocationHandlerAdapter(IProxyInvocationHandler handler) {
        this.handler = handler;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        IInvokable invokable = new JdkMethod(method);
        return handler.invoke(invocation.getTarget(), invokable, invocation.getArgs());
    }

}
