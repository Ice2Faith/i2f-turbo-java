package i2f.bql.core.lambda.builder;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/12/4 19:55
 * @desc
 */
public class AliasMapBuilder extends AbsMapBuilder<String> {

    public AliasMapBuilder() {
    }

    public AliasMapBuilder(Map<? extends Serializable, String> map) {
        super(map);
    }

    public static AliasMapBuilder create() {
        return new AliasMapBuilder();
    }

    public static AliasMapBuilder create(Map<? extends Serializable, String> map) {
        return new AliasMapBuilder(map);
    }

}
