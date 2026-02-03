package i2f.jdbc.proxy.xml.mybatis.parameter.impl;

import i2f.jdbc.proxy.xml.mybatis.parameter.ParameterProvider;
import i2f.reflect.vistor.Visitor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/2/3 16:43
 * @desc 支持参数从共享的线程变量中获取
 */
public class ThreadLocalParameterProvider implements ParameterProvider {
    public static final ThreadLocalParameterProvider INSTANCE = new ThreadLocalParameterProvider();
    public static final String NAME = "thread-local";
    public static final InheritableThreadLocal<Object> HOLDER = new InheritableThreadLocal<>();

    @Override
    public Object apply(String expression, Map<String, Object> params, boolean isDollar) {
        Object root = HOLDER.get();
        Visitor visitor = Visitor.visit(expression, root);
        return visitor.get();
    }
}
