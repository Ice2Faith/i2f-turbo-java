package i2f.springboot.jdbc.bql.autoconfiguration;

import i2f.extension.ognl.OgnlUtil;
import i2f.extension.velocity.VelocityGenerator;
import i2f.extension.velocity.bindsql.VelocityResourceSqlTemplateResolver;
import i2f.extension.velocity.bindsql.VelocitySqlGenerator;
import i2f.jdbc.proxy.provider.ProxyRenderSqlProvider;
import i2f.jdbc.proxy.provider.impl.SimpleProxyRenderSqlProvider;
import i2f.jdbc.proxy.xml.mybatis.MybatisMapperContext;
import i2f.jdbc.proxy.xml.mybatis.inflater.MybatisMapperInflater;
import i2f.jdbc.proxy.xml.mybatis.parser.MybatisMapperParser;
import i2f.jdbc.proxy.xml.mybatis.provider.MybatisMapperProxyRenderSqlProvider;
import i2f.springboot.jdbc.bql.components.SpringJdbcProxyMapperFactoryBean;
import i2f.springboot.jdbc.bql.components.VelocityProxyRenderSqlProvider;
import i2f.springboot.jdbc.bql.mybatis.OgnlMybatisMapperInflater;
import i2f.springboot.jdbc.bql.properties.JdbcProxyProperties;
import lombok.Data;
import ognl.Ognl;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.net.URL;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/4/24 16:19
 * @desc
 */
@Data
@EnableConfigurationProperties({
        JdbcProxyProperties.class
})
@ConditionalOnExpression("${i2f.jdbc.proxy.enable:true}")
public class JdbcProxyAutoConfiguration implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

    private Logger log = LoggerFactory.getLogger(this.getClass());


    private ApplicationContext context;

    @ConditionalOnMissingBean(MybatisMapperInflater.class)
    @ConditionalOnClass({OgnlMybatisMapperInflater.class, OgnlUtil.class, Ognl.class})
    @Bean
    public MybatisMapperInflater ognlMybatisMapperInflater() {
        return new OgnlMybatisMapperInflater();
    }

    @ConditionalOnMissingBean(MybatisMapperInflater.class)
    @ConditionalOnClass({MybatisMapperInflater.class})
    @Bean
    public MybatisMapperInflater mybatisMapperInflater() {
        return new MybatisMapperInflater();
    }

    @ConditionalOnMissingBean(MybatisMapperContext.class)
    @ConditionalOnClass({MybatisMapperContext.class})
    @Bean
    public MybatisMapperContext mybatisMapperContext(MybatisMapperInflater inflater) {
        JdbcProxyProperties proxyProperties = context.getBean(JdbcProxyProperties.class);
        List<String> scriptLocations = new ArrayList<>();
        List<String> locations = proxyProperties.getScriptLocations();
        if (locations != null) {
            scriptLocations.addAll(locations);
        } else {
            scriptLocations.add("classpath*:/**/mapper/**/*.xml");
        }
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<URL> resources = new ArrayList<>();
        for (String scriptLocation : scriptLocations) {
            String path = scriptLocation.trim();
            if (path.isEmpty()) {
                continue;
            }
            try {
                Resource[] arr = resolver.getResources(path);
                for (Resource resource : arr) {
                    resources.add(resource.getURL());
                }
            } catch (Exception e) {

            }
        }
        MybatisMapperContext context = new MybatisMapperContext(resources);
        context.setInflater(inflater);
        return context;
    }

    @ConditionalOnMissingBean(VelocityResourceSqlTemplateResolver.class)
    @ConditionalOnClass({VelocitySqlGenerator.class, VelocityGenerator.class, VelocityEngine.class})
    @Bean
    public VelocityResourceSqlTemplateResolver velocityResourceSqlTemplateResolver() {
        JdbcProxyProperties proxyProperties = context.getBean(JdbcProxyProperties.class);
        List<String> scriptLocations = new ArrayList<>();
        List<String> locations = proxyProperties.getScriptLocations();
        if (locations != null) {
            scriptLocations.addAll(locations);
        } else {
            scriptLocations.add("classpath*:/**/mapper/**/*.xml.vm");
        }
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<URL> resources = new ArrayList<>();
        for (String scriptLocation : scriptLocations) {
            String path = scriptLocation.trim();
            if (path.isEmpty()) {
                continue;
            }
            try {
                Resource[] arr = resolver.getResources(path);
                for (Resource resource : arr) {
                    resources.add(resource.getURL());
                }
            } catch (Exception e) {

            }
        }
        VelocityResourceSqlTemplateResolver ret = new VelocityResourceSqlTemplateResolver(resources);
        ret.refreshResources();
        return ret;
    }

    @ConditionalOnMissingBean(ProxyRenderSqlProvider.class)
    @ConditionalOnClass({MybatisMapperContext.class, MybatisMapperInflater.class, MybatisMapperParser.class})
    @Bean
    public MybatisMapperProxyRenderSqlProvider mybatisMapperProxyRenderSqlProvider(MybatisMapperContext context) {
        return new MybatisMapperProxyRenderSqlProvider(context);
    }

    @ConditionalOnMissingBean(ProxyRenderSqlProvider.class)
    @ConditionalOnClass({VelocitySqlGenerator.class, VelocityGenerator.class, VelocityEngine.class})
    @Bean
    public VelocityProxyRenderSqlProvider velocityProxyRenderSqlProvider(VelocityResourceSqlTemplateResolver templateResolver) {
        return new VelocityProxyRenderSqlProvider(templateResolver);
    }

    @ConditionalOnMissingBean(ProxyRenderSqlProvider.class)
    @Bean
    public SimpleProxyRenderSqlProvider simpleProxyRenderSqlProvider() {
        return new SimpleProxyRenderSqlProvider();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        JdbcProxyProperties proxyProperties = context.getBean(JdbcProxyProperties.class);
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            List<String> packages = new ArrayList<>();
            List<String> mapperPackages = proxyProperties.getMapperPackages();
            if (mapperPackages != null) {
                packages.addAll(mapperPackages);
            } else {
                packages.add("**.mapper.**");
                packages.add("**.dao.**");
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

                String simpleName = clazz.getSimpleName();
                String beanName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);


                GenericBeanDefinition definition = (GenericBeanDefinition) BeanDefinitionBuilder.genericBeanDefinition(clazz)
                        .getRawBeanDefinition();
                definition.getPropertyValues().add("mapperClass", clazz);
                definition.getPropertyValues().add("context", context);
                definition.setBeanClass(SpringJdbcProxyMapperFactoryBean.class);
                definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                definition.setLazyInit(true);
                beanDefinitionRegistry.registerBeanDefinition(beanName, definition);

                log.info("registry mapper bean " + beanName + " of class " + className);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }


}
