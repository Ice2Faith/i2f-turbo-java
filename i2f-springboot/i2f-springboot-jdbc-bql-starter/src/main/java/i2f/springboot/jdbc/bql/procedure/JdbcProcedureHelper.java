package i2f.springboot.jdbc.bql.procedure;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

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
        log.info("xproc4j config JdbcProcedureHelper ...");
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

    public static <T> T invoke(String procedureId,Map<String,Object> params){
        try {
            latch.await();
        } catch (Exception e) {

        }
        return executor.invoke(procedureId, params);
    }

    public static void call(String procedureId, Map<String, Object> params) {
        try {
            latch.await();
        } catch (Exception e) {

        }
        getExecutor().call(procedureId, params);
    }

    public static void call(String procedureId, Object ... args) {
        try {
            latch.await();
        } catch (Exception e) {

        }
        getExecutor().call(procedureId, args);
    }

    public static void call(String procedureId, List<Object> args) {
        try {
            latch.await();
        } catch (Exception e) {

        }
        getExecutor().call(procedureId, args);
    }
}
