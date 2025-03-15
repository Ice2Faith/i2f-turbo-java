package i2f.jdbc.procedure.context;

import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/3/14 19:33
 */
public interface JdbcProcedureContext {

    void listener(JdbcProcedureContextRefreshListener listener);

    Map<String, ProcedureMeta> getMetaMap();

    void registry(String name, ProcedureMeta meta);

    void remove(String name);

    default void registry(Map<String, ProcedureMeta> map) {
        if (map == null) {
            return;
        }
        for (Map.Entry<String, ProcedureMeta> entry : map.entrySet()) {
            registry(entry.getKey(), entry.getValue());
        }
    }

    default void registry(Collection<ProcedureMeta> list) {
        if (list == null) {
            return;
        }
        for (ProcedureMeta meta : list) {
            registry(meta);
        }
    }

    default void registry(ProcedureMeta... args) {
        registry(Arrays.asList(args));
    }

    default void registry(ProcedureMeta meta) {
        registry(null, meta);
    }

    default void registry(String key, XmlNode value) {
        ProcedureMeta meta = ProcedureMeta.ofMeta(key, value);
        registry(meta);
    }

    default void registry(String key, JdbcProcedureJavaCaller value) {
        ProcedureMeta meta = ProcedureMeta.ofMeta(key, value);
        registry(meta);
    }
}
