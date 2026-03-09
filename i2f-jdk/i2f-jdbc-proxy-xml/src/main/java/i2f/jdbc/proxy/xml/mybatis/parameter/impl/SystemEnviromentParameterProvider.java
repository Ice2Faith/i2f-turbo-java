package i2f.jdbc.proxy.xml.mybatis.parameter.impl;

import i2f.jdbc.proxy.xml.mybatis.parameter.ParameterProvider;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/2/3 16:43
 * @desc 支持参数从环境变量中获取
 */
public class SystemEnviromentParameterProvider implements ParameterProvider {
    public static final SystemEnviromentParameterProvider INSTANCE = new SystemEnviromentParameterProvider();

    public static final String NAME = "sys-env";

    @Override
    public Object apply(String expression, Map<String, Object> params, boolean isDollar) {
        return System.getenv(expression);
    }
}
