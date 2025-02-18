package i2f.jdbc.procedure.caller.impl;

import i2f.jdbc.procedure.annotations.JdbcProcedure;
import i2f.jdbc.procedure.caller.JdbcProcedureJavaCallerMapSupplier;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.reflect.ReflectResolver;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2025/2/18 22:28
 * @desc
 */
@Data
@NoArgsConstructor
public class ListableJdbcProcedureJavaCallerMapSupplier implements JdbcProcedureJavaCallerMapSupplier {
    protected final ConcurrentHashMap<String, JdbcProcedureJavaCaller> javaMap = new ConcurrentHashMap<>();

    public ListableJdbcProcedureJavaCallerMapSupplier(Map<String, JdbcProcedureJavaCaller> map) {
        if (map != null) {
            javaMap.putAll(map);
        }
    }

    public ListableJdbcProcedureJavaCallerMapSupplier(Collection<JdbcProcedureJavaCaller> javaCallers) {
        addJavaCaller(javaCallers);
    }

    @Override
    public Map<String, JdbcProcedureJavaCaller> getJavaCallerMap() {
        return new HashMap<>(javaMap);
    }

    public void addAll(Map<String, JdbcProcedureJavaCaller> map) {
        if (map != null) {
            javaMap.putAll(map);
        }
    }

    public void addJavaCaller(JdbcProcedureJavaCaller... javaCallers) {
        addJavaCaller(Arrays.asList(javaCallers));
    }

    public void addJavaCaller(Collection<JdbcProcedureJavaCaller> javaCallers) {
        for (JdbcProcedureJavaCaller item : javaCallers) {
            addCaller(item, javaMap);
        }
    }

    public static void addCaller(JdbcProcedureJavaCaller caller, Map<String, JdbcProcedureJavaCaller> javaMap) {
        if (caller == null) {
            return;
        }
        JdbcProcedure ann = ReflectResolver.getAnnotation(caller.getClass(), JdbcProcedure.class);
        if (ann == null) {
            return;
        }
        String id = ann.value();
        if (id != null && !id.isEmpty()) {
            javaMap.put(id, caller);
        }
    }
}
