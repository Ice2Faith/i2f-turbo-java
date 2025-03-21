package i2f.springboot.jdbc.bql.procedure.impl;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.executor.impl.DefaultJdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/2/8 9:50
 */
@Data
@NoArgsConstructor
public class SpringContextJdbcProcedureExecutor extends DefaultJdbcProcedureExecutor {
    protected ApplicationContext applicationContext;
    protected Environment environment;
    protected static final Logger log = LoggerFactory.getLogger(SpringContextJdbcProcedureExecutor.class);
    protected static final AtomicBoolean hasApplyNodes = new AtomicBoolean(false);


    public SpringContextJdbcProcedureExecutor(JdbcProcedureContext context,ApplicationContext applicationContext) {
        super(context);
        this.applicationContext = applicationContext;
        this.environment = applicationContext.getEnvironment();
    }

    public SpringContextJdbcProcedureExecutor(JdbcProcedureContext context,ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
    }

    public void applyNodeExecutorComponents(){
        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            Object bean = applicationContext.getBean(name);
            if(bean instanceof ExecutorNode){
                this.nodes.add(0,(ExecutorNode) bean);
            }
        }
    }

    @Override
    public List<ExecutorNode> getNodes() {
        if (!hasApplyNodes.getAndSet(true)) {
            applyNodeExecutorComponents();
        }
        return super.getNodes();
    }

    @Override
    public Map<String, Object> createParams() {
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put(ParamsConsts.CONTEXT, applicationContext);
        ret.put(ParamsConsts.ENVIRONMENT, environment);

        String[] names = applicationContext.getBeanDefinitionNames();
        Map<String, Object> beanMap = new TreeMap<>();
        for (String name : names) {
            Object bean = applicationContext.getBean(name);
            beanMap.put(name, bean);
        }
        ret.put(ParamsConsts.BEANS, beanMap);

        ret.put(ParamsConsts.DATASOURCES, getDatasourceMap());

        ret.put(ParamsConsts.CONNECTIONS, new HashMap<>());

        ret.put(ParamsConsts.GLOBAL, new HashMap<>());

        return ret;
    }

    public Map<String, DataSource> getDatasourceMap() {
        try {
            AbstractRoutingDataSource bean = applicationContext.getBean(AbstractRoutingDataSource.class);
            if (bean != null) {
                Map<String, DataSource> ret = new HashMap<>();
                Map<Object, DataSource> dataSources = bean.getResolvedDataSources();
                for (Map.Entry<Object, DataSource> entry : dataSources.entrySet()) {
                    String name = String.valueOf(entry.getKey());
                    if(name.toLowerCase().endsWith("datasource")){
                        name=name.substring(0,name.length()-"datasource".length());
                    }
                    ret.put(name, entry.getValue());
                }
                detectPrimaryDatasource(ret);
                return ret;
            }
        } catch (Exception e) {
        }
        try {
            DynamicRoutingDataSource bean = applicationContext.getBean(DynamicRoutingDataSource.class);
            Map<String, DataSource> ret = bean.getDataSources();
            detectPrimaryDatasource(ret);
            return ret;
        } catch (Exception e) {
        }
        try{
            Map<String, DataSource> ret = new HashMap<>();
            Map<String, DataSource> dataSources = applicationContext.getBeansOfType(DataSource.class);
            for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
                String name = entry.getKey();
                if(name.toLowerCase().endsWith("datasource")){
                    name=name.substring(0,name.length()-"datasource".length());
                }
                ret.put(name, entry.getValue());
            }
            detectPrimaryDatasource(ret);
            return ret;
        }catch(Exception e){

        }
        return new HashMap<>();
    }

    public static void detectPrimaryDatasource(Map<String, DataSource> ret) {
        if(ret.isEmpty()){
            return;
        }
        DataSource primary = ret.get(ParamsConsts.DEFAULT_DATASOURCE);
        if(primary==null){
            List<String> defaultNames=Arrays.asList("primary","master","main","default","leader");
            for (Map.Entry<String, DataSource> entry : ret.entrySet()) {
                String name = entry.getKey();
                if(defaultNames.contains(name)){
                    ret.put(ParamsConsts.DEFAULT_DATASOURCE,entry.getValue());
                    return;
                }
                name=name.toLowerCase();
                if(defaultNames.contains(name)){
                    ret.put(ParamsConsts.DEFAULT_DATASOURCE,entry.getValue());
                    return;
                }
            }
            if(ret.size()==1){
                ret.put(ParamsConsts.DEFAULT_DATASOURCE,ret.get(ret.keySet().iterator().next()));
                return;
            }
        }
    }


    @Override
    public void debugLog(Supplier<Object> supplier) {
        if (debug.get()) {
            log.debug(String.valueOf(supplier.get()));
        }
    }

    @Override
    public void infoLog(Supplier<Object> supplier, Throwable e) {
        if (e != null) {
            log.info(String.valueOf(supplier.get()), e);
        } else {
            log.info(String.valueOf(supplier.get()));
        }
    }

    @Override
    public void warnLog(Supplier<Object> supplier, Throwable e) {
        if (e != null) {
            log.warn(String.valueOf(supplier.get()), e);
        } else {
            log.warn(String.valueOf(supplier.get()));
        }
    }

    @Override
    public void errorLog(Supplier<Object> supplier, Throwable e) {
        if (e != null) {
            log.error(String.valueOf(supplier.get()), e);
        } else {
            log.error(String.valueOf(supplier.get()));
        }
    }


}
