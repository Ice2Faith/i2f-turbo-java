package i2f.springcloud.config.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.refresh.ConfigDataContextRefresher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2026/4/1 14:49
 * @desc
 */
@ConditionalOnExpression("${i2f.springcloud.config-client.refresh.pull.enable:false}")
@ConditionalOnClass(ConfigDataContextRefresher.class)
@Slf4j
@Data
@NoArgsConstructor
@EnableConfigurationProperties(EnvironmentPullBaseWithVersionProperties.class)
@Component
public class EnvironmentPullBaseWithVersionCompareIntervalRefresher implements ApplicationRunner {
    public static final String VERSION_MISSING = "missing";
    @Autowired
    protected ConfigDataContextRefresher configDataContextRefresher;
    @Autowired
    protected Environment environment;
    @Autowired
    protected EnvironmentPullBaseWithVersionProperties properties;

    protected RestTemplate restTemplate = new RestTemplate();

    protected ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();

    protected AtomicBoolean initConfigFileVersion = new AtomicBoolean(false);
    protected CopyOnWriteArraySet<String> configNameSet = new CopyOnWriteArraySet<>();
    protected ConcurrentHashMap<String, String> configFileVersionMap = new ConcurrentHashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(() -> {
            initConfig();
        }).start();
        pool.scheduleWithFixedDelay(this::refreshTask,
                properties.getInitDelaySeconds(),
                properties.getIntervalDelaySeconds(),
                TimeUnit.SECONDS);
    }

    public synchronized void initConfig() {
        if (initConfigFileVersion.getAndSet(true)) {
            return;
        }
        // 初始化名称列表
        String prop = environment.getProperty("spring.application.name");
        if (prop != null && !prop.isEmpty()) {
            configNameSet.add(prop);
        }
        prop = environment.getProperty("spring.cloud.config.name");
        if (prop != null && !prop.isEmpty()) {
            String[] arr = prop.split(",");
            for (String item : arr) {
                item = item.trim();
                if (!item.isEmpty()) {
                    configNameSet.add(item);
                }
            }
        }

        log.info("pull config names: " + configNameSet);

        /**
         * config-client 配置
         *
         * spring.profiles.active=dev
         * spring.cloud.config.name=eureka-client,charset,log4j
         * spring.cloud.config.uri=http://localhost:7777
         * spring.application.name=config-server
         */

        // 初始化启动时的版本信息
        String configUrl = environment.getProperty("spring.cloud.config.uri");
        if (configUrl == null) {
            log.warn("pull config ignored, not config uri!");
            return;
        }

        if (configUrl.endsWith("/")) {
            configUrl = configUrl.substring(0, configUrl.length() - 1);
        }

        log.info("pull config base url: " + configUrl);


        String[] activeProfiles = environment.getActiveProfiles();
        Set<String> profiles = new LinkedHashSet<>(Arrays.asList(activeProfiles));
        profiles.add("default");
        log.info("pull config profiles: " + configNameSet);

        for (String configName : configNameSet) {
            for (String profile : profiles) {
                try {
                    String mapKey = "/" + configName + "/" + profile;
                    String requestUrl = configUrl + mapKey;
                    log.debug("pull config url [" + requestUrl + "] ... ");
                    org.springframework.cloud.config.environment.Environment configEnv = restTemplate.getForObject(requestUrl, org.springframework.cloud.config.environment.Environment.class);
                    String remoteVersion = null;
                    if (configEnv != null) {
                        remoteVersion = configEnv.getVersion();
                    }
                    if (remoteVersion == null) {
                        remoteVersion = VERSION_MISSING;
                    }
                    configFileVersionMap.put(mapKey, remoteVersion);
                    log.debug("pull config url [" + requestUrl + "] version [" + remoteVersion + "]");
                } catch (Exception e) {
                    log.debug(e.getMessage(), e);
                }

            }
        }
    }

    public void refreshTask() {
        try {

            initConfig();

            log.debug("begin interval check config version ...");

            String configUrl = environment.getProperty("spring.cloud.config.uri");
            if (configUrl == null) {
                log.warn("pull config ignored, not config uri!");
                return;
            }

            if (configUrl.endsWith("/")) {
                configUrl = configUrl.substring(0, configUrl.length() - 1);
            }

            String[] activeProfiles = environment.getActiveProfiles();
            Set<String> profiles = new LinkedHashSet<>(Arrays.asList(activeProfiles));
            profiles.add("default");

            boolean needRefresh = false;
            for (String configName : configNameSet) {
                for (String profile : profiles) {
                    String mapKey = "/" + configName + "/" + profile;
                    String requestUrl = configUrl + mapKey;
                    org.springframework.cloud.config.environment.Environment configEnv = restTemplate.getForObject(requestUrl, org.springframework.cloud.config.environment.Environment.class);
                    String remoteVersion = null;
                    if (configEnv != null) {
                        remoteVersion = configEnv.getVersion();
                    }
                    if (remoteVersion == null) {
                        remoteVersion = VERSION_MISSING;
                    }
                    String selfVersion = configFileVersionMap.get(mapKey);
                    configFileVersionMap.put(mapKey, remoteVersion);
                    if (!VERSION_MISSING.equals(remoteVersion)
                            && !remoteVersion.equalsIgnoreCase(selfVersion)) {
                        needRefresh = true;
                        log.info("checked config url [" + requestUrl + "] version has changed!");
                        break;
                    }
                }
                if (needRefresh) {
                    break;
                }
            }

            if (needRefresh) {
                log.info("pull config begin refresh context ...");
                configDataContextRefresher.refresh();
            }

        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }
    }
}
