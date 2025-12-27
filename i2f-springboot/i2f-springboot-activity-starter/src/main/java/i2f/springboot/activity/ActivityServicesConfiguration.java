package i2f.springboot.activity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2022/5/3 13:35
 * @desc
 */
@ConditionalOnBean(ActivityAutoConfiguration.class)
@ConditionalOnExpression("${i2f.springboot.config.activity.enable-ioc-services:true}")
@Slf4j
@Data
@NoArgsConstructor
@Configuration
public class ActivityServicesConfiguration {

    @Autowired
    private ProcessEngine processEngine;

    @Bean
    public RepositoryService repositoryService() {
        log.info("RepositoryService config done.");
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService() {
        log.info("RuntimeService config done.");
        return processEngine.getRuntimeService();
    }

    @Bean
    public HistoryService historyService() {
        log.info("HistoryService config done.");
        return processEngine.getHistoryService();
    }

    @Bean
    public ManagementService managementService() {
        log.info("ManagementService config done.");
        return processEngine.getManagementService();
    }

    @Bean
    public TaskService taskService() {
        log.info("TaskService config done.");
        return processEngine.getTaskService();
    }
}
