package i2f.jdbc.procedure.executor;

import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/5/13 17:08
 */
@FunctionalInterface
public interface FeatureFunction {
    Object feature(Object value, XmlNode node, Map<String, Object> context);
}
