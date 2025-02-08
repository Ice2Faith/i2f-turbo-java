package i2f.springboot.zookeeper.properties;

import i2f.extension.zookeeper.ZookeeperConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2024/6/30 18:07
 * @desc
 */
@ConfigurationProperties(prefix = "i2f.zookeeper")
public class ZookeeperProperties extends ZookeeperConfig {
}
