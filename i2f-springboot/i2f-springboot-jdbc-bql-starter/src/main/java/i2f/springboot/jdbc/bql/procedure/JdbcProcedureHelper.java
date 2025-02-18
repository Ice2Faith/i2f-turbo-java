package i2f.springboot.jdbc.bql.procedure;

import i2f.jdbc.procedure.caller.impl.DefaultJdbcProcedureExecutorCaller;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2025/2/8 11:09
 */
public class JdbcProcedureHelper implements ApplicationContextAware {
    public static ApplicationContext applicationContext;
    private static volatile DefaultJdbcProcedureExecutorCaller caller;
    private static final CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JdbcProcedureHelper.applicationContext = applicationContext;
        JdbcProcedureHelper.caller = applicationContext.getBean(DefaultJdbcProcedureExecutorCaller.class);
        latch.countDown();
    }

    public static <T> T invoke(String procedureId,Map<String,Object> params){
        try {
            latch.await();
        } catch (Exception e) {

        }
        return caller.invoke(procedureId, params);
    }

    public static void call(String procedureId, Map<String, Object> params) {
        try {
            latch.await();
        } catch (Exception e) {

        }
        caller.call(procedureId, params);
    }
}
