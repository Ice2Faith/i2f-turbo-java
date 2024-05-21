package i2f.springboot.dynamic.datasource.autoconfiguration;

import i2f.springboot.dynamic.datasource.aop.DataSourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/3/21 11:00
 * @desc
 */
@Slf4j
@ConditionalOnExpression("${" + DynamicDataSourceConfig.CONFIG_PREFIX + ".enable:true}")
public class DynamicDataSourceProperty implements InitializingBean {

    public static final String MULTIPLY_DATASOURCE_PREFIX = DynamicDataSourceConfig.CONFIG_PREFIX + ".multiply.";
    @Autowired
    private Environment environment;

    private Map<String, Map<String, Object>> properties = new HashMap<>();


    public static Map<String, Map<String, Object>> getDatasourceConfigs(Environment env) {
        return getGroupMapConfigs(env, MULTIPLY_DATASOURCE_PREFIX);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        properties = getDatasourceConfigs(environment);
        for (Map.Entry<String, Map<String, Object>> item : properties.entrySet()) {
            log.info("multiply datasource find: " + item.getKey());
            log.info("datasource is:" + item.getValue());
        }

        if (!properties.containsKey(DataSourceType.MASTER)) {
            resolveDefaultSpringDatasourceAsMaster(properties);
        }

        if (!properties.containsKey(DataSourceType.MASTER)) {
            throw new BeanInitializationException("datasource [" + DataSourceType.MASTER + "] not found config.please config properties like as:\n" +
                    "\t" + MULTIPLY_DATASOURCE_PREFIX + "master.url=jdbc:xxx\n" +
                    "\t" + MULTIPLY_DATASOURCE_PREFIX + "master.username=xxx\n" +
                    "\t" + MULTIPLY_DATASOURCE_PREFIX + "master.password=xxx\n" +
                    "\t" + MULTIPLY_DATASOURCE_PREFIX + "master.driver=xxx\n" +
                    "\t\tcomment: .master is required other datasource is option,format like:\n" +
                    "\t\t" + MULTIPLY_DATASOURCE_PREFIX + "${datasourceId}.[url|username|password|driver]");
        }
    }

    public Map<String, Map<String, Object>> getDatasource() {
        return properties;
    }

    private void resolveDefaultSpringDatasourceAsMaster(Map<String, Map<String, Object>> properties) {
        Map<String, Object> map = getPropertiesWithPrefix(environment, false, "spring.datasource.");
        Map<String, Object> res = new HashMap<>();
        if (!map.containsKey("url")) {
            return;
        }
        res.put("url", map.get("url"));
        res.put("username", map.get("username"));
        res.put("password", map.get("password"));
        res.put("driver", map.get("driver-class-name"));
        properties.put(DataSourceType.MASTER, res);
    }


    /**
     * 将环境处理为一个方便理解的数据结构
     * key表示每一个环境
     * value表示每一个环境中的配置的map
     *
     * @return
     */
    public static Map<String, Map<String, Object>> getEnvironmentProperties(Environment environment) {
        Map<String, Map<String, Object>> map = new HashMap<>(8);
        ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) environment;
        Iterator<PropertySource<?>> iterator = configurableEnvironment.getPropertySources().iterator();

        while (iterator.hasNext()) {
            Map<String, Object> props = new HashMap<>(128);
            PropertySource<?> source = iterator.next();
            String name = source.getName();
            Object src = source.getSource();
            if (src instanceof Map) {
                for (Map.Entry<String, Object> entry : ((Map<String, Object>) src).entrySet()) {
                    try {
                        String key = entry.getKey();
                        props.put(key, configurableEnvironment.getProperty(key));
                    } catch (Exception e) {

                    }
                }
            }
            map.put(name, props);
        }
        return map;
    }

    /**
     * 直接从环境中获取指定前缀的所有配置
     *
     * @param keepPrefix 是否保留前缀
     * @param prefix     前缀
     * @return
     */
    public static Map<String, Object> getPropertiesWithPrefix(Environment environment, boolean keepPrefix, String prefix) {
        return getPropertiesWithPrefix(getEnvironmentProperties(environment), keepPrefix, prefix);
    }

    /**
     * 从配置对象中获取某些指定前缀的配置信息
     *
     * @param envProperties 配置对象
     * @param keepPrefix    是否保留前缀
     * @param prefix        前缀
     * @return
     */
    public static Map<String, Object> getPropertiesWithPrefix(Map<String, Map<String, Object>> envProperties, boolean keepPrefix, String prefix) {
        Map<String, Object> ret = new HashMap<>();
        Map<String, Map<String, Object>> map = envProperties;
        for (Map.Entry<String, Map<String, Object>> item : map.entrySet()) {
            Map<String, Object> entire = item.getValue();
            for (Map.Entry<String, Object> cfg : entire.entrySet()) {
                String key = cfg.getKey();
                if (!key.startsWith(prefix)) {
                    continue;
                }
                Object val = cfg.getValue();

                if (!keepPrefix) {
                    key = key.substring(prefix.length());
                }
                ret.put(key, val);
            }
        }
        return ret;
    }


    /**
     * 从环境中获取以groupPrefix开头的每一个分组，分别将分组信息包装为一个map,
     * 返回以分组id(string):分组对象(map)形式返回
     * 分组概念：
     * 例如：
     * spring.datasource.master.url=
     * spring.datasource.master.driver=
     * <p>
     * spring.datasource.slave.url=
     * spring.datasource.slave.driver=
     * 这样的配置数据，则可以得到分组的前缀groupPrefix=spring.datasource.
     * 则取出分组数据：
     * master:
     * url:
     * driver:
     * slave:
     * url:
     * driver:
     * 其中的master和slave被称为ID，作为返回值的key,
     * 每一组数据中的数据被url和driver被包装为value
     *
     * @param groupPrefix
     * @return
     */
    public static Map<String, Map<String, Object>> getGroupMapConfigs(Environment environment, String groupPrefix) {
        Map<String, Map<String, Object>> ret = new HashMap<>();
        Map<String, Map<String, Object>> map = getEnvironmentProperties(environment);
        for (Map.Entry<String, Map<String, Object>> item : map.entrySet()) {
            Map<String, Object> entire = item.getValue();
            for (Map.Entry<String, Object> cfg : entire.entrySet()) {
                String key = cfg.getKey();
                if (!key.startsWith(groupPrefix)) {
                    continue;
                }
                String type = key.substring(groupPrefix.length());
                int idx = type.indexOf(".");
                String typeKey = type.substring(0, idx);
                if (!ret.containsKey(typeKey)) {
                    ret.put(typeKey, new HashMap<>());
                }
                String propKey = type.substring(idx + 1);
                ret.get(typeKey).put(propKey, cfg.getValue());
            }
        }
        return ret;
    }
}
