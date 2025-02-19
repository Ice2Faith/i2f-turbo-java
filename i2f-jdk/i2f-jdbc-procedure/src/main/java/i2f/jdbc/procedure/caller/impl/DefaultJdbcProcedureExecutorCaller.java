package i2f.jdbc.procedure.caller.impl;

import i2f.jdbc.procedure.caller.JdbcProcedureExecutorCaller;
import i2f.jdbc.procedure.caller.JdbcProcedureJavaCallerMapSupplier;
import i2f.jdbc.procedure.caller.JdbcProcedureNodeMapSupplier;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/8 11:02
 */
@Data
@NoArgsConstructor
public class DefaultJdbcProcedureExecutorCaller implements JdbcProcedureExecutorCaller {
    protected JdbcProcedureExecutor executor;
    protected JdbcProcedureNodeMapSupplier nodeSupplier;
    protected JdbcProcedureJavaCallerMapSupplier javaCallerSupplier;

    public DefaultJdbcProcedureExecutorCaller(JdbcProcedureExecutor executor, ExecuteContext context){
        this.executor=executor;
        this.nodeSupplier=new StaticJdbcProcedureNodeMapCacheSupplier(context.getNodeMap());
        this.javaCallerSupplier=new ListableJdbcProcedureJavaCallerMapSupplier(context.getJavaMap());
    }

    public DefaultJdbcProcedureExecutorCaller(JdbcProcedureExecutor executor, JdbcProcedureNodeMapSupplier nodeSupplier) {
        this.executor = executor;
        this.nodeSupplier = nodeSupplier;
    }

    public DefaultJdbcProcedureExecutorCaller(JdbcProcedureExecutor executor, JdbcProcedureNodeMapSupplier nodeSupplier, JdbcProcedureJavaCallerMapSupplier javaCallerSupplier) {
        this.executor = executor;
        this.nodeSupplier = nodeSupplier;
        this.javaCallerSupplier = javaCallerSupplier;
    }

    @Override
    public <T> T invoke(String procedureId, Map<String, Object> params) {
        call(procedureId, params);
        return (T) executor.visit(ParamsConsts.RETURN, params);
    }

    @Override
    public void call(String procedureId, Map<String, Object> params) {
        Map<String, XmlNode> nodeMap = nodeSupplier.getNodeMap();
        Map<String, Object> execParams = executor.createParams();
        for (Map.Entry<String, Object> entry : execParams.entrySet()) {
            params.putIfAbsent(entry.getKey(), entry.getValue());
        }

        Map<String, JdbcProcedureJavaCaller> javaCallerMap = new HashMap<>(javaCallerSupplier.getJavaCallerMap());

        ExecuteContext context = new ExecuteContext(params, nodeMap, javaCallerMap);

        Map<String, Object> beanMap = context.paramsGet(ParamsConsts.BEANS);
        if (beanMap != null) {
            for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
                Object bean = entry.getValue();
                if (!(bean instanceof JdbcProcedureJavaCaller)) {
                    continue;
                }
                JdbcProcedureJavaCaller item = (JdbcProcedureJavaCaller) bean;
                ListableJdbcProcedureJavaCallerMapSupplier.addCaller(item, javaCallerMap);
            }
        }
        executor.exec(procedureId, context);
    }
}
