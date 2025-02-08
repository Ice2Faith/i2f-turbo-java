package i2f.springboot.zookeeper;

import i2f.extension.zookeeper.ZookeeperManager;
import i2f.extension.zookeeper.cache.ZookeeperCache;
import i2f.extension.zookeeper.cluster.ClusterProvider;
import i2f.extension.zookeeper.cluster.impl.ZookeeperClusterProvider;
import i2f.extension.zookeeper.lock.ZookeeperLockProvider;
import i2f.extension.zookeeper.lock.ZookeeperLockUtil;
import i2f.springboot.zookeeper.properties.ZookeeperProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * @author Ice2Faith
 * @date 2023/4/11 19:13
 * @desc
 */
@ConditionalOnExpression("${i2f.zookeeper.enable:true}")
@EnableConfigurationProperties(ZookeeperProperties.class)
@Data
@NoArgsConstructor
public class ZookeeperAutoConfiguration implements EnvironmentAware {
    private Environment environment;

    @Autowired
    private ZookeeperProperties zkConfig;


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public ZookeeperManager zookeeperManager() {
        return new ZookeeperManager(zkConfig);
    }

    @Bean
    public ZookeeperCache zookeeperCache(@Autowired ZookeeperManager zookeeperManager) {
        return new ZookeeperCache(zookeeperManager);
    }

    @Bean
    public ClusterProvider clusterProvider(@Autowired ZookeeperManager zookeeperManager) throws Exception {
        String appName = environment.getProperty("spring.application.name");
        if (StringUtils.isEmpty(appName)) {
            appName = "noappname";
        }
        String listenPath = "/apps/" + appName + "/cluster";
        ZookeeperClusterProvider clusterProvider = new ZookeeperClusterProvider(listenPath, zookeeperManager);
        return clusterProvider;
    }

    @ConditionalOnClass(CuratorFramework.class)
    @Bean
    public CuratorFramework curatorFramework() {
        return ZookeeperLockUtil.getClient(zkConfig.getConnectString(), zkConfig.getSessionTimeout());
    }

    @ConditionalOnClass(CuratorFramework.class)
    @Bean
    public ZookeeperLockProvider zookeeperLockProvider(@Autowired CuratorFramework curator) {
        return new ZookeeperLockProvider(curator);
    }

}
