package i2f.jdbc.procedure.provider;

import i2f.jdbc.procedure.context.ProcedureMeta;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/3/15 14:53
 */
@FunctionalInterface
public interface JdbcProcedureMetaProvider {
    Map<String, ProcedureMeta> getMetaMap();

    default ProcedureMeta getProcedure(String procedureId){
        return getMetaMap().get(procedureId);
    }
}
