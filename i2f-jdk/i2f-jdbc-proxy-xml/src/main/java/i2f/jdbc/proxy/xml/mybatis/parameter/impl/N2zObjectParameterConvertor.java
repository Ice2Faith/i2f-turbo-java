package i2f.jdbc.proxy.xml.mybatis.parameter.impl;

import i2f.jdbc.proxy.xml.mybatis.parameter.ParameterConvertor;

/**
 * @author Ice2Faith
 * @date 2026/2/3 16:45
 * @desc 对参数，进行 如果 null 则转换为数值 0
 */
public class N2zObjectParameterConvertor implements ParameterConvertor {
    public static final N2zObjectParameterConvertor INSTANCE = new N2zObjectParameterConvertor();

    public static final String NAME = "n2z";

    @Override
    public Object convert(Object obj, String expr, boolean isDollar) {
        return obj == null ? 0 : obj;
    }

}
