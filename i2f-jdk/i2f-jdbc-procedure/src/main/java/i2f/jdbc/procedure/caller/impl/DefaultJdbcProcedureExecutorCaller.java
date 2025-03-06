package i2f.jdbc.procedure.caller.impl;

import i2f.jdbc.procedure.caller.JdbcProcedureExecutorCaller;
import i2f.jdbc.procedure.caller.JdbcProcedureJavaCallerMapSupplier;
import i2f.jdbc.procedure.caller.JdbcProcedureNodeMapSupplier;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.impl.DefaultJdbcProcedureExecutor;
import i2f.jdbc.procedure.signal.impl.NotFoundSignalException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/8 11:02
 */
@Data
@NoArgsConstructor
public class DefaultJdbcProcedureExecutorCaller implements JdbcProcedureExecutorCaller {
    protected JdbcProcedureExecutor executor = new DefaultJdbcProcedureExecutor();
    protected JdbcProcedureContext context = new JdbcProcedureContext();

    public DefaultJdbcProcedureExecutorCaller(JdbcProcedureExecutor executor, Map<String, ProcedureMeta> map) {
        this.executor = executor;
        this.context = new JdbcProcedureContext(map);
    }

    public DefaultJdbcProcedureExecutorCaller(JdbcProcedureExecutor executor, JdbcProcedureContext context) {
        this.executor = executor;
        this.context = context;
    }

    public DefaultJdbcProcedureExecutorCaller(JdbcProcedureExecutor executor, JdbcProcedureNodeMapSupplier nodeSupplier) {
        this.executor = executor;
        this.context = new JdbcProcedureContext(nodeSupplier, null);
    }

    public DefaultJdbcProcedureExecutorCaller(JdbcProcedureExecutor executor, JdbcProcedureNodeMapSupplier nodeSupplier, JdbcProcedureJavaCallerMapSupplier javaCallerSupplier) {
        this.executor = executor;
        this.context = new JdbcProcedureContext(nodeSupplier, javaCallerSupplier);
    }

    @Override
    public <T> T invoke(String procedureId, Map<String, Object> params) {
        call(procedureId, params);
        return (T) executor.visit(ParamsConsts.RETURN, params);
    }

    @Override
    public <T> T invoke(String procedureId, List<Object> args) {
        Map<String, Object> params = castArgListAsProcedureMap(procedureId, args);
        return invoke(procedureId,params);
    }

    @Override
    public Map<String, Object> call(String procedureId, List<Object> args) {
        Map<String, Object> params = castArgListAsProcedureMap(procedureId, args);
        return call(procedureId,params);
    }

    public Map<String,Object> castArgListAsProcedureMap(String procedureId,List<Object> args){
        Map<String,Object> ret=new LinkedHashMap<>();
        ExecuteContext context = getBasicContext();
        Map<String, ProcedureMeta> nodeMap = context.getNodeMap();
        ProcedureMeta node = nodeMap.get(procedureId);
        if(node!=null){
            List<String> arguments = node.getArguments();
            int i=0;
            for (String name : arguments) {
                if(AttrConsts.ID.equals(name)){
                    continue;
                }
                if(ParamsConsts.RETURN.equals(name)){
                    continue;
                }
                Object value=i<args.size()?args.get(i):null;
                ret.put(name,value);
                i++;
            }
            return ret;
        }
        throw new NotFoundSignalException("not found node: " + procedureId);
    }

    public ExecuteContext getBasicContext(){
        ExecuteContext ret = new ExecuteContext(null, context.get());

        return ret;
    }

    public Map<String,Object> call(String procedureId, ExecuteContext context) {

        executor.exec(procedureId, context);

        return context.getParams();
    }

    @Override
    public Map<String,Object> call(String procedureId, Map<String, Object> params) {
        ExecuteContext context = getBasicContext();

        Map<String, Object> execParams = executor.createParams();
        for (Map.Entry<String, Object> entry : execParams.entrySet()) {
            params.putIfAbsent(entry.getKey(), entry.getValue());
        }

        context.setParams(params);

        return call(procedureId,context);
    }
}
