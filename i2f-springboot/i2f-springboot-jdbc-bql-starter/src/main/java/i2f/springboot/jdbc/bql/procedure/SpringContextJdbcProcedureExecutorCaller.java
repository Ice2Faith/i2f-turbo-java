package i2f.springboot.jdbc.bql.procedure;

import i2f.jdbc.procedure.annotations.JdbcProcedure;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.reflect.ReflectResolver;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/2/8 11:02
 */
@Data
@NoArgsConstructor
public class SpringContextJdbcProcedureExecutorCaller {
    protected JdbcProcedureExecutor executor;
    protected SpringJdbcProcedureNodeMapRefresher refresher;
    protected Supplier<List<JdbcProcedureJavaCaller>> callerSupplier;
    protected volatile Map<String,JdbcProcedureJavaCaller> javaMap;

    public SpringContextJdbcProcedureExecutorCaller(JdbcProcedureExecutor executor, SpringJdbcProcedureNodeMapRefresher refresher) {
        this.executor = executor;
        this.refresher = refresher;
    }

    public SpringContextJdbcProcedureExecutorCaller(JdbcProcedureExecutor executor, SpringJdbcProcedureNodeMapRefresher refresher, Supplier<List<JdbcProcedureJavaCaller>> callerSupplier) {
        this.executor = executor;
        this.refresher = refresher;
        this.callerSupplier = callerSupplier;
    }

    public void call(String procedureId, Map<String, Object> params) {
        Map<String, XmlNode> nodeMap = refresher.getNodeMap();
        XmlNode node = nodeMap.get(procedureId);
        if(javaMap==null){
            synchronized (this){
                javaMap=new HashMap<>();
                List<JdbcProcedureJavaCaller> list = callerSupplier.get();
                for (JdbcProcedureJavaCaller item : list) {
                    JdbcProcedure ann = ReflectResolver.getAnnotation(item.getClass(), JdbcProcedure.class);
                    if (ann == null) {
                        continue;
                    }
                    String id = ann.value();
                    if(id!=null && !id.isEmpty()){
                        javaMap.put(id,item);
                    }
                }
            }
        }
        Map<String, Object> execParams = executor.createParams();
        for (Map.Entry<String, Object> entry : execParams.entrySet()) {
            params.putIfAbsent(entry.getKey(),entry.getValue());
        }
        Map<String,JdbcProcedureJavaCaller> javaCallerMap=new HashMap<>(javaMap);

        ExecuteContext context = new ExecuteContext(params, nodeMap,javaCallerMap);
        Map<String,Object> beanMap = context.paramsGet(ParamsConsts.BEANS);
        if(beanMap!=null){
            for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
                Object bean = entry.getValue();
                if(!(bean instanceof JdbcProcedureJavaCaller)){
                    continue;
                }
                JdbcProcedureJavaCaller item=(JdbcProcedureJavaCaller)bean;
                JdbcProcedure ann = ReflectResolver.getAnnotation(item.getClass(), JdbcProcedure.class);
                if (ann == null) {
                    continue;
                }
                String id = ann.value();
                if(id!=null && !id.isEmpty()){
                    javaMap.put(id,item);
                }
            }
        }
        executor.exec(node, context);
    }
}
