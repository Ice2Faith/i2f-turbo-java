package i2f.jdbc.procedure.executor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/10/30 22:23
 * @desc
 */
@FunctionalInterface
public interface FeatureFunctionProvider {
    Map<String, FeatureFunction> getFeatures();
}
