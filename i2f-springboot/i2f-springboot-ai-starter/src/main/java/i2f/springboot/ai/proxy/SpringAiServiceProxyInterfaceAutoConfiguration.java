package i2f.springboot.ai.proxy;

import i2f.ai.std.service.annotations.AiService;
import i2f.ai.std.service.proxy.AiServiceDynamicProxyHandler;
import i2f.spring.core.SpringContext;
import i2f.springboot.ai.autoconfiguration.SpringAiServiceAutoConfiguration;
import i2f.springboot.ai.properties.SpringAiProxyServiceProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/7/23 14:58
 */
@AutoConfigureAfter(SpringAiServiceAutoConfiguration.class)
@ConditionalOnExpression("${i2f.springboot.ai.proxy.enable:true}")
@Configuration
@Slf4j
@Data
@EnableConfigurationProperties({
        SpringAiProxyServiceProperties.class
})
public class SpringAiServiceProxyInterfaceAutoConfiguration implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {
    private ApplicationContext applicationContext;

    @Autowired
    private SpringAiProxyServiceProperties springAiProxyServiceProperties;


    @ConditionalOnExpression("${i2f.springboot.ai.proxy.handler.enable:true}")
    @ConditionalOnMissingBean(AiServiceDynamicProxyHandler.class)
    @Bean
    public AiServiceDynamicProxyHandler aiServiceDynamicProxyHandler() {
        return new AiServiceDynamicProxyHandler(new SpringContext(applicationContext));
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            List<String> packages = new ArrayList<>();
            List<String> mapperPackages = springAiProxyServiceProperties.getServicePackages();
            if (mapperPackages != null) {
                packages.addAll(mapperPackages);
            } else {
                packages.add("**.ai.**");
                packages.add("**.service.**");
            }
            Set<Resource> resources = new LinkedHashSet<>();
            for (String item : packages) {
                String path = item.trim();
                if (path.isEmpty()) {
                    continue;
                }
                path = item.replaceAll("\\.", "/");
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                if (path.endsWith("/")) {
                    path = path.substring(0, path.length() - 1);
                }
                if (path.isEmpty()) {
                    continue;
                }
                path = "classpath*:/" + path + "/*.class";
                Resource[] arr = resolver.getResources(path);
                if (arr != null) {
                    resources.addAll(Arrays.asList(arr));
                }
            }
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resolver);
            for (Resource resource : resources) {
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                ClassMetadata classMetadata = reader.getClassMetadata();
                String className = classMetadata.getClassName();
                Class<?> clazz = Class.forName(className);
                if (!clazz.isInterface()) {
                    continue;
                }
                AiService ann = clazz.getDeclaredAnnotation(AiService.class);
                if (ann == null) {
                    continue;
                }
                if (!ann.enable()) {
                    continue;
                }

                String simpleName = clazz.getSimpleName();
                String beanName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);


                GenericBeanDefinition definition = (GenericBeanDefinition) BeanDefinitionBuilder.genericBeanDefinition(clazz)
                        .getRawBeanDefinition();
                definition.getPropertyValues().add("serviceClass", clazz);
                definition.getPropertyValues().add("context", applicationContext);
                definition.setBeanClass(SpringAiServiceProxyInterfaceFactoryBean.class);
                definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                definition.setLazyInit(true);
                beanDefinitionRegistry.registerBeanDefinition(beanName, definition);

                log.info("registry ai service bean " + beanName + " of class " + className);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
