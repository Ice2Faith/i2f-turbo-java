package i2f.spring.enviroment;

import i2f.environment.IEnvironment;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/8/9 10:19
 * @desc
 */
@Data
@NoArgsConstructor
public class SpringEnvironment implements IEnvironment, EnvironmentAware {
    protected Environment environment;

    public SpringEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getProperty(String name) {
        return environment.getProperty(name);
    }

    @Override
    public Map<String, String> getAllProperties() {
        Map<String, String> ret = new LinkedHashMap<>();
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        MutablePropertySources sources = env.getPropertySources();
        for (PropertySource<?> source : sources) {
            String name = source.getName();
            Object target = source.getSource();
            if (target instanceof Map) {
                Map<?, ?> map = (Map<String, String>) target;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    Object value = entry.getValue();
                    ret.put(String.valueOf(entry.getKey()), (value == null ? null : String.valueOf(value)));
                }
            } else if (target instanceof Properties) {
                Properties properties = (Properties) target;
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    Object value = entry.getValue();
                    ret.put(String.valueOf(entry.getKey()), (value == null ? null : String.valueOf(value)));
                }
            }
        }
        return ret;
    }

    public List<String> getActiveProfiles() {
        String[] activeProfiles = environment.getActiveProfiles();
        return new ArrayList<>(Arrays.asList(activeProfiles));
    }

    public List<String> getSourceNames() {
        List<String> ret = new ArrayList<>();
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        MutablePropertySources sources = env.getPropertySources();
        for (PropertySource<?> source : sources) {
            String name = source.getName();
            ret.add(name);
        }
        return ret;
    }
}
