package i2f.springboot.jdbc.bql.procedure.ext;

import org.springframework.core.env.Environment;

/**
 * @author Ice2Faith
 * @date 2025/9/17 9:46
 */
public class SpringEnvironmentFunctions {
    public static volatile Environment environment;

    public static String spring_env(Object key) {
        return environment.getProperty(String.valueOf(key));
    }
}
