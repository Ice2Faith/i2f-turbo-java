package i2f.jdbc.proxy.xml.mybatis.parameter;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/2/2 14:12
 * @desc
 */
@FunctionalInterface
public interface ParameterProvider {
    Object apply(String expression, Map<String, Object> params, boolean isDollar);
}
