package i2f.bql.core.lambda.builder;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/12/4 19:55
 * @desc
 */
public class ValueMapBuilder extends AbsMapBuilder<Object> {

    public ValueMapBuilder() {
    }

    public ValueMapBuilder(Map<? extends Serializable, Object> map) {
        super(map);
    }

    public static ValueMapBuilder create() {
        return new ValueMapBuilder();
    }

    public static ValueMapBuilder create(Map<? extends Serializable, Object> map) {
        return new ValueMapBuilder(map);
    }

}
