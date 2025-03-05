package i2f.jdbc.procedure.caller.impl;

import i2f.jdbc.procedure.annotations.JdbcProcedure;
import i2f.jdbc.procedure.caller.JdbcProcedureExecutorCaller;
import i2f.jdbc.procedure.caller.JdbcProcedureJavaCallerMapSupplier;
import i2f.jdbc.procedure.caller.JdbcProcedureNodeMapSupplier;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.NotFoundSignalException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
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
    protected JdbcProcedureExecutor executor;
    protected JdbcProcedureNodeMapSupplier nodeSupplier;
    protected JdbcProcedureJavaCallerMapSupplier javaCallerSupplier;

    public DefaultJdbcProcedureExecutorCaller(JdbcProcedureExecutor executor, ExecuteContext context) {
        this.executor = executor;
        this.nodeSupplier = new StaticJdbcProcedureNodeMapCacheSupplier(context.getNodeMap());
        this.javaCallerSupplier = new ListableJdbcProcedureJavaCallerMapSupplier(context.getJavaMap());
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
        Map<String, XmlNode> nodeMap = context.getNodeMap();
        XmlNode node = nodeMap.get(procedureId);
        if(node!=null){
            Map<String, String> attrMap = node.getTagAttrMap();
            int i=0;
            for (Map.Entry<String, String> entry : attrMap.entrySet()) {
                String name = entry.getKey();
                if(ParamsConsts.RETURN.equals(name)){
                    continue;
                }
                Object value=i<args.size()?args.get(i):null;
                ret.put(name,value);
                i++;
            }
            return ret;
        }
        Map<String, JdbcProcedureJavaCaller> javaMap = context.getJavaMap();
        JdbcProcedureJavaCaller javaCaller = javaMap.get(procedureId);
        if(javaCaller!=null){
            Class<?> clazz = javaCaller.getClass();
            JdbcProcedure ann = clazz.getDeclaredAnnotation(JdbcProcedure.class);
            if(ann!=null){
                String[] arguments = ann.arguments();
                int i=0;
                for (String argument : arguments) {
                    Map.Entry<String, List<String>> attrFeatures = JdbcProcedureParser.parseAttrFeatures(argument);
                    String name = attrFeatures.getKey();
                    if(ParamsConsts.RETURN.equals(name)){
                        continue;
                    }
                    Object value=i<args.size()?args.get(i):null;
                    ret.put(name,value);
                    i++;
                }
            }
        }
        throw new NotFoundSignalException("not found node: " + procedureId);
    }

    public ExecuteContext getBasicContext(){
        Map<String, XmlNode> nodeMap = nodeSupplier.getNodeMap();
        Map<String, JdbcProcedureJavaCaller> javaCallerMap = new HashMap<>(javaCallerSupplier.getJavaCallerMap());

        ExecuteContext context = new ExecuteContext(null, nodeMap, javaCallerMap);

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

        return context;
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
