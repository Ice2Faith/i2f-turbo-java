package i2f.jdbc.procedure.context;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2025/1/22 13:57
 */
@Data
@NoArgsConstructor
public class ExecuteContext {
    protected Map<String, Object> params = new HashMap<>();
    protected Map<String, ProcedureMeta> nodeMap = new HashMap<>();

    public ExecuteContext(Map<String, Object> params) {
        this.params = params;
    }

    public ExecuteContext(Map<String, Object> params, Map<String, ProcedureMeta> nodeMap) {
        this.params = params;
        this.nodeMap = nodeMap;
    }


    public void paramsSet(String key, Object value) {
        this.params.put(key, value);
    }

    public <T> T paramsGet(String key) {
        return (T) params.get(key);
    }

    public <T> T paramsComputeIfAbsent(String key, Function<String, T> valueSupplier) {
        return (T) params.computeIfAbsent(key, valueSupplier);
    }


}
