package i2f.springboot.spring.application;

import i2f.extension.slf4j.Slf4jPrintStream;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

/**
 * @author Ice2Faith
 * @date 2024/7/12 22:14
 * @desc
 */
@AutoConfigureOrder(-1)
@Data
@NoArgsConstructor
public class SpringBootPrintBannerAutoConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        Slf4jPrintStream.redirectSysoutSyserr();
        System.out.println("starting...");
    }

    @Bean
    public ApplicationListener<ApplicationStartedEvent> applicationStartedEventApplicationListener() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        StackTraceElement elem = trace[trace.length - 1];
        String className = elem.getClassName();
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (Throwable e) {

        }
        if (clazz == null) {
            try {
                clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            } catch (Throwable e) {
            }
        }
        return BaseBootApplication.getStartedListener(null, clazz);
    }
}
