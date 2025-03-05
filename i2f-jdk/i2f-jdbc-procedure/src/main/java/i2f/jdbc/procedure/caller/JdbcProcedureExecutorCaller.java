package i2f.jdbc.procedure.caller;

import i2f.container.builder.map.MapBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/2/8 11:02
 */
public interface JdbcProcedureExecutorCaller {

    default <T> T invoke(String procedureId, Consumer<MapBuilder<String, Object, ? extends Map<String, Object>>> consumer) {
        MapBuilder<String, Object, HashMap<String, Object>> builder = new MapBuilder<>(new HashMap<String, Object>());
        consumer.accept(builder);
        return invoke(procedureId, builder.get());
    }

    <T> T invoke(String procedureId, Map<String, Object> params);

    default <T> T invoke(String procedureId, Object ... args){
        return invoke(procedureId,Arrays.asList(args));
    }

    <T> T invoke(String procedureId, List<Object> args);

    default  Map<String,Object> call(String procedureId, Consumer<MapBuilder<String, Object, ? extends Map<String, Object>>> consumer) {
        MapBuilder<String, Object, HashMap<String, Object>> builder = new MapBuilder<>(new HashMap<String, Object>());
        consumer.accept(builder);
        return call(procedureId, builder.get());
    }

    Map<String,Object> call(String procedureId, Map<String, Object> params);

    default Map<String,Object> call(String procedureId, Object ... args){
        return call(procedureId, Arrays.asList(args));
    }

    Map<String,Object> call(String procedureId, List<Object> args);
}
