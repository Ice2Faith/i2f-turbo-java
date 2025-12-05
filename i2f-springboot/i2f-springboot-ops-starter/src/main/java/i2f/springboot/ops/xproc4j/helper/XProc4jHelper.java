package i2f.springboot.ops.xproc4j.helper;

import i2f.reflect.ReflectResolver;
import i2f.springboot.ops.common.OpsException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2025/12/5 9:45
 * @desc
 */
@Data
@NoArgsConstructor
@Component
public class XProc4jHelper {

    @Autowired
    private ApplicationContext applicationContext;

    public static final String EXECUTOR_CLASS_NAME="JdbcProcedureExecutor";
    public static final String METHOD_GET_META_MAP = "getMetaMap";
    public static final String METHOD_CALL = "call";
    public static final String METHOD_EVAL_SCRIPT = "evalScript";

    private final AtomicBoolean hasInitHolder=new AtomicBoolean(false);
    private final AtomicReference<Object> executorHolder=new AtomicReference<>();
    private final AtomicReference<Method> metasMapMethodHolder=new AtomicReference<>();
    private final AtomicReference<Method> callByIdMethodHolder =new AtomicReference<>();
    private final AtomicReference<Method> evalScriptMethodHolder=new AtomicReference<>();


    public void initHolder(){
        if(hasInitHolder.getAndSet(true)){
            return;
        }
        try {
            String[] names = applicationContext.getBeanDefinitionNames();
            for (String name : names) {
                try {
                    Object bean = applicationContext.getBean(name);
                    String simpleName = bean.getClass().getSimpleName();
                    int idx=simpleName.lastIndexOf("$$EnhancerBySpring");
                    if(idx>=0){
                        simpleName=simpleName.substring(0,idx);
                    }
                    if(simpleName.equals(EXECUTOR_CLASS_NAME)){
                        executorHolder.set(bean);
                        break;
                    }
                } catch (Exception e) {

                }
            }
            Object executor = executorHolder.get();
            Class<?> clazz = executor.getClass();
            try {

                Method getMetaMap = clazz.getMethod(METHOD_GET_META_MAP);
                metasMapMethodHolder.set(getMetaMap);
            } catch (Exception e) {

            }

            try {

                Method call = clazz.getMethod(METHOD_CALL, String.class, Map.class);
                callByIdMethodHolder.set(call);
            } catch (Exception e) {

            }

            try {

                Method evalScript = clazz.getMethod(METHOD_EVAL_SCRIPT, String.class, String.class, Map.class);
                evalScriptMethodHolder.set(evalScript);
            } catch (Exception e) {

            }
        } catch (Exception e) {

        }
    }

    public Map<String,Map<String,Object>> getMetasMap() throws Exception {
        initHolder();
        Object executor = executorHolder.get();
        Method method = metasMapMethodHolder.get();
        if(method==null){
            throw new OpsException("not found method:"+METHOD_GET_META_MAP);
        }
        Map<String,Map<String,Object>> ret=new TreeMap<>();
        Map map = (Map) method.invoke(executor);
        for (Object rawKey : map.keySet()) {
            String key=String.valueOf(rawKey);
            Object rawValue = map.get(rawKey);
            LinkedHashMap<String, Object> value = ReflectResolver.bean2map(rawValue, new LinkedHashMap<>());
            value.put("target",null);
            ret.put(key,value);
        }
        return ret;
    }

    public Map<String, Object> call(String procedureId, Map<String,Object> params) throws Exception {
        if(params==null){
            params=new HashMap<>();
        }
        initHolder();
        Object executor = executorHolder.get();
        Method method = callByIdMethodHolder.get();
        if(method==null){
            throw new OpsException("not found method:"+ METHOD_CALL);
        }
        Map<String, Object> ret = (Map<String, Object>)method.invoke(executor, procedureId, params);
        return ret;
    }

    public Object evalScript(String lang, String script, Map<String, Object> params) throws Exception {
        if(params==null){
            params=new HashMap<>();
        }
        initHolder();
        Object executor = executorHolder.get();
        Method method = evalScriptMethodHolder.get();
        if(method==null){
            throw new OpsException("not found method:"+METHOD_EVAL_SCRIPT);
        }
        Object ret =method.invoke(executor, lang,script, params);
        return ret;
    }

    public Map trimContextParams(Map params){
        if(params==null){
            return params;
        }
        String[] trimKeys={
                "stack_lock",
                "context",
                "env",
                "beans",
                "datasources",
                "trace",
                "executor",
                "lru",
                "executorLru",
                "staticLru",
                "connections",
                "metas"
        };
        for (String key : trimKeys) {
            params.remove(key);
        }
        return params;
    }

}
