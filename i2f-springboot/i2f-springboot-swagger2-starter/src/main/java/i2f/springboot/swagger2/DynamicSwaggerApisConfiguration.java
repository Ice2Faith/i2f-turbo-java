package i2f.springboot.swagger2;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2022/4/9 15:18
 * @desc
 */
@Slf4j
@ConfigurationProperties(prefix = "i2f.swagger2.apis.dynamic")
@Configuration
@ConditionalOnExpression("${i2f.swagger2.apis.dynamic.enable:true}")
public class DynamicSwaggerApisConfiguration implements InitializingBean, ApplicationContextAware {

    @Autowired
    private ApiInfo apiInfo;

    private ApplicationContext applicationContext;

    private Map<String,GroupItemProperties> group=new LinkedHashMap<>();

    @Data
    @NoArgsConstructor
    public static class GroupItemProperties{
        private String group;
        private String basePackage;
        private String antPath;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        for(Map.Entry<String,GroupItemProperties> entry : group.entrySet()){
            String name=entry.getKey();
            name=name+"Docket";
            GroupItemProperties item=entry.getValue();
            if(applicationContext.containsBean(name)){
                log.warn("context has contains bean with name:"+name);
                continue;
            }

            String groupName=item.getGroup();
            String basePackage=item.getBasePackage();
            String antPath=item.getAntPath();

            if(groupName==null || "".equals(groupName)){
                log.warn("swagger registry docket bad group.");
                continue;
            }

            if(basePackage==null || "".equals(basePackage)){
                log.warn("swagger registrt docket bad base-package.");
                continue;
            }

            if(antPath==null || "".equals(antPath)){
                log.warn("swagger registry bad ant-path be replace to /**");
                antPath="/**";
            }

            //获取BeanFactory
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            //创建bean信息.
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(DocketFactoryBean.class);
            beanDefinitionBuilder.addPropertyValue("apiInfo",apiInfo)
                    .addPropertyValue("groupName",groupName)
                    .addPropertyValue("basePackage",basePackage)
                    .addPropertyValue("antPath",antPath);
            //动态注册bean.
            defaultListableBeanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());
            log.info("swagger registry docket name is:"+name);

            Docket dock=(Docket)applicationContext.getBean(name);
            log.info("swagger dock is:"+dock);
        }
    }

}
