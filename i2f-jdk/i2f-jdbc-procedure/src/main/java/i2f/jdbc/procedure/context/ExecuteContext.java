package i2f.jdbc.procedure.context;

import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.jdbc.procedure.parser.data.XmlNode;
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
    protected Map<String, XmlNode> nodeMap = new HashMap<>();
    protected Map<String, JdbcProcedureJavaCaller> javaMap = new HashMap<>();

    public ExecuteContext(Map<String, Object> params) {
        this.params = params;
    }

    public ExecuteContext(Map<String, Object> params, Map<String, XmlNode> nodeMap) {
        this.params = params;
        this.nodeMap = nodeMap;
    }

    public ExecuteContext(Map<String, Object> params, Map<String, XmlNode> nodeMap, Map<String, JdbcProcedureJavaCaller> javaMap) {
        this.params = params;
        this.nodeMap = nodeMap;
        this.javaMap = javaMap;
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
