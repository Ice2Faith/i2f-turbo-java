package i2f.jdbc.proxy.xml.mybatis.parameter;

/**
 * @author Ice2Faith
 * @date 2026/2/2 14:12
 * @desc
 */
@FunctionalInterface
public interface ParameterConvertor {
    Object convert(Object obj);
}
