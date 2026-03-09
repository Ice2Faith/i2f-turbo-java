package i2f.jdbc.proxy.xml.mybatis.parameter.impl;

import i2f.jdbc.proxy.xml.mybatis.parameter.ParameterProvider;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/2/3 16:43
 * @desc 支持参数从系统配置中获取
 */
public class SystemPropertiesParameterProvider implements ParameterProvider {
    public static final SystemPropertiesParameterProvider INSTANCE = new SystemPropertiesParameterProvider();

    public static final String NAME = "sys-prop";

    @Override
    public Object apply(String expression, Map<String, Object> params, boolean isDollar) {
        return System.getProperty(expression);
    }
}
