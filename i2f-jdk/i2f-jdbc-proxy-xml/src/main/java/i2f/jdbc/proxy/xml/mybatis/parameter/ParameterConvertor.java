package i2f.jdbc.proxy.xml.mybatis.parameter;

/**
 * @author Ice2Faith
 * @date 2026/2/2 14:12
 * @desc 用于在XML的占位符中使用
 * #{name,convertor=ParameterConvertor}
 * ${name,convertor=ParameterConvertor}
 * 用以进行对name变量预先转换
 * 同时，也支持返回一个BindSql对象用于直接绑定变量的SQL片段
 */
@FunctionalInterface
public interface ParameterConvertor {
    /**
     *
     * @param obj 需要转换的对象
     * @param expr 取出此对象使用的表达式
     * @param isDollar 是否是在 ${} 占位符中使用
     * @return
     */
    Object convert(Object obj, String expr, boolean isDollar);
}
