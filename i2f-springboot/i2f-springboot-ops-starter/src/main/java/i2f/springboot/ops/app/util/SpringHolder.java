package i2f.springboot.ops.app.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Ice2Faith
 * @date 2026/9/13 16:33
 * @desc
 */
@Slf4j
@Component
@Data
@NoArgsConstructor
public class SpringHolder implements ApplicationContextAware, EnvironmentAware {
    protected static ApplicationContext context;
    protected static Environment env;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringHolder.context = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        SpringHolder.env = environment;
    }

    public static ApplicationContext context() {
        return context;
    }

    public static Environment env() {
        return env;
    }
}
