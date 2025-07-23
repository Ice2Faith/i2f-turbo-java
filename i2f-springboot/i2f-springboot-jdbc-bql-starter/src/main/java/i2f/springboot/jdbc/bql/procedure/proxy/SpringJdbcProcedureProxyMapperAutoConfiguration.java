package i2f.springboot.jdbc.bql.procedure.proxy;

import i2f.springboot.jdbc.bql.procedure.SpringContextJdbcProcedureExecutorAutoConfiguration;
import i2f.springboot.jdbc.bql.procedure.SpringJdbcProcedureProperties;
import i2f.springboot.jdbc.bql.procedure.proxy.annotations.ProcedureMapper;
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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
@AutoConfigureAfter(SpringContextJdbcProcedureExecutorAutoConfiguration.class)
@ConditionalOnExpression("${xproc4j.proxy.enable:true}")
@Slf4j
@Data
@EnableConfigurationProperties({
        SpringJdbcProcedureProperties.class
})
public class SpringJdbcProcedureProxyMapperAutoConfiguration implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {
    private ApplicationContext applicationContext;

    @Autowired
    private SpringJdbcProcedureProperties jdbcProcedureProperties;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            List<String> packages = new ArrayList<>();
            List<String> mapperPackages = jdbcProcedureProperties.getMapperPackages();
            if (mapperPackages != null) {
                packages.addAll(mapperPackages);
            } else {
                packages.add("**.procedure.**");
                packages.add("**.proc.**");
                packages.add("**.xproc.**");
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
                ProcedureMapper ann = clazz.getDeclaredAnnotation(ProcedureMapper.class);
                if (ann == null) {
                    continue;
                }

                String simpleName = clazz.getSimpleName();
                String beanName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);


                GenericBeanDefinition definition = (GenericBeanDefinition) BeanDefinitionBuilder.genericBeanDefinition(clazz)
                        .getRawBeanDefinition();
                definition.getPropertyValues().add("mapperClass", clazz);
                definition.getPropertyValues().add("context", applicationContext);
                definition.setBeanClass(SpringJdbcProcedureProxyMapperFactoryBean.class);
                definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                definition.setLazyInit(true);
                beanDefinitionRegistry.registerBeanDefinition(beanName, definition);

                log.info("registry procedure mapper bean " + beanName + " of class " + className);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
