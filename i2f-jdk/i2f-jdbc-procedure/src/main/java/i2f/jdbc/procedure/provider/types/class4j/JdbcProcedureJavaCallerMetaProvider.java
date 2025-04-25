package i2f.jdbc.procedure.provider.types.class4j;

import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.jdbc.procedure.provider.JdbcProcedureMetaProvider;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/18 22:06
 * @desc
 */
@FunctionalInterface
public interface JdbcProcedureJavaCallerMetaProvider extends JdbcProcedureMetaProvider {
    Map<String, JdbcProcedureJavaCaller> getJavaCallerMap();

    @Override
    default Map<String, ProcedureMeta> getMetaMap() {
        Map<String, ProcedureMeta> metaMap = new LinkedHashMap<>();
        Map<String, JdbcProcedureJavaCaller> javaCallerMap = getJavaCallerMap();
        if (javaCallerMap != null) {
            for (Map.Entry<String, JdbcProcedureJavaCaller> entry : javaCallerMap.entrySet()) {
                String key = entry.getKey();
                JdbcProcedureJavaCaller value = entry.getValue();
                ProcedureMeta meta = ProcedureMeta.ofMeta(key, value);
                if (meta != null) {
                    metaMap.put(meta.getName(), meta);
                }
            }
        }
        return metaMap;
    }

    default JdbcProcedureJavaCaller getJavaCaller(String procedureId) {
        return getJavaCallerMap().get(procedureId);
    }

}
