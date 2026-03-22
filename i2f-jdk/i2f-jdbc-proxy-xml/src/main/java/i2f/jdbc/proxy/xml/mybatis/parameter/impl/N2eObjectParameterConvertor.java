package i2f.jdbc.proxy.xml.mybatis.parameter.impl;

import i2f.jdbc.proxy.xml.mybatis.parameter.ParameterConvertor;

/**
 * @author Ice2Faith
 * @date 2026/2/3 16:45
 * @desc 对参数，进行 如果 null 则转换为空串 ''
 */
public class N2eObjectParameterConvertor implements ParameterConvertor {
    public static final N2eObjectParameterConvertor INSTANCE = new N2eObjectParameterConvertor();

    public static final String NAME = "n2e";

    @Override
    public Object convert(Object obj, String expr, boolean isDollar) {
        return obj == null ? "" : obj;
    }

}
