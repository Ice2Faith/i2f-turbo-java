package i2f.agent.transformer;

import java.lang.instrument.Instrumentation;

/**
 * @author Ice2Faith
 * @date 2024/10/10 10:39
 */
public interface InstrumentTransformerFeature {
    default boolean canRetransform() {
        return true;
    }

    default void onAdded(Instrumentation inst) {

    }

    default void onRemoved(Instrumentation inst) {

    }
}
