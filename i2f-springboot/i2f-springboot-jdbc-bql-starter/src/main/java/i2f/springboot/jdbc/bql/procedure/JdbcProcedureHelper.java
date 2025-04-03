package i2f.springboot.jdbc.bql.procedure;

import i2f.container.builder.map.MapBuilder;
import i2f.jdbc.procedure.consts.XProc4jConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/2/8 11:09
 */
@Slf4j
public class JdbcProcedureHelper implements ApplicationContextAware {
    public static volatile ApplicationContext applicationContext;
    private static volatile JdbcProcedureExecutor executor;
    private static final CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info(XProc4jConsts.NAME+" config "+this.getClass().getSimpleName()+" ...");
        JdbcProcedureHelper.applicationContext = applicationContext;
        latch.countDown();
    }

    public static JdbcProcedureExecutor getExecutor(){
        if(executor==null){
            synchronized (latch){
                if(executor==null) {
                    executor = applicationContext.getBean(JdbcProcedureExecutor.class);
                }
            }
        }
        return executor;
    }

    public static <T> T invoke(String procedureId, Consumer<MapBuilder<String, Object, ? extends Map<String, Object>>> consumer) {
        MapBuilder<String, Object, HashMap<String, Object>> builder = new MapBuilder<>(new HashMap<String, Object>());
        consumer.accept(builder);
        return invoke(procedureId, builder.get());
    }

    public static <T> T invoke(String procedureId,Map<String,Object> params){
        try {
            latch.await();
        } catch (Exception e) {

        }
        return getExecutor().invoke(procedureId, params);
    }

    public static <T> T invoke(String procedureId,Object ... args){
        try {
            latch.await();
        } catch (Exception e) {

        }
        return getExecutor().invoke(procedureId, args);
    }

    public static <T> T invoke(String procedureId,List<Object> args){
        try {
            latch.await();
        } catch (Exception e) {

        }
        return getExecutor().invoke(procedureId, args);
    }

    public static Map<String,Object> call(String procedureId, Consumer<MapBuilder<String, Object, ? extends Map<String, Object>>> consumer) {
        MapBuilder<String, Object, HashMap<String, Object>> builder = new MapBuilder<>(new HashMap<String, Object>());
        consumer.accept(builder);
        return call(procedureId, builder.get());
    }

    public static Map<String,Object> call(String procedureId, Map<String, Object> params) {
        try {
            latch.await();
        } catch (Exception e) {

        }
        return getExecutor().call(procedureId, params);
    }

    public static Map<String,Object> call(String procedureId, Object ... args) {
        try {
            latch.await();
        } catch (Exception e) {

        }
        return getExecutor().call(procedureId, args);
    }

    public static Map<String,Object> call(String procedureId, List<Object> args) {
        try {
            latch.await();
        } catch (Exception e) {

        }
        return getExecutor().call(procedureId, args);
    }
}
