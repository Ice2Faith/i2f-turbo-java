package i2f.jdbc.procedure.caller;

import i2f.container.builder.map.MapBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/2/8 11:02
 */
public interface JdbcProcedureExecutorCaller {

    default <T> T invoke(String procedureId, Consumer<MapBuilder<String,Object,? extends Map<String,Object>>> consumer){
        MapBuilder<String, Object, HashMap<String, Object>> builder = new MapBuilder<>(new HashMap<String, Object>());
        consumer.accept(builder);
        return invoke(procedureId,builder.get());
    }

    <T> T invoke(String procedureId, Map<String, Object> params);

    default void call(String procedureId, Consumer<MapBuilder<String,Object,? extends Map<String,Object>>> consumer){
        MapBuilder<String, Object, HashMap<String, Object>> builder = new MapBuilder<>(new HashMap<String, Object>());
        consumer.accept(builder);
        call(procedureId,builder.get());
    }

    void call(String procedureId, Map<String, Object> params);

}
