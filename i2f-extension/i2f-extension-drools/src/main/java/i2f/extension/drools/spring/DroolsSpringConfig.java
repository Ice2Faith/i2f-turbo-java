package i2f.extension.drools.spring;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * @author Ice2Faith
 * @date 2026/11/7 17:43
 * @desc 不再需要 kmodule.xml
 * 而是通过配置类配置
 */
@Data
@NoArgsConstructor
@Configuration
public class DroolsSpringConfig {
    @Value("${drools.rules.path:rules/}")
    protected String rulesPath = "rules/";

    @Bean
    @ConditionalOnMissingBean
    public KieServices kieServices() {
        return KieServices.Factory.get();
    }

    @Bean
    @ConditionalOnMissingBean
    public KieFileSystem kieFileSystem(@Autowired KieServices kieServices) throws Exception {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:" + rulesPath + "*.*");
        for (Resource resource : resources) {
            String path = rulesPath + resource.getFilename();
            kieFileSystem.write(ResourceFactory.newClassPathResource(path, "UTF-8"));
        }
        return kieFileSystem;
    }

    @Bean
    @ConditionalOnMissingBean
    public KieContainer kieContainer(@Autowired KieServices kieServices,
                                     @Autowired KieFileSystem kieFileSystem) {
        KieRepository repository = kieServices.getRepository();
        repository.addKieModule(repository::getDefaultReleaseId);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        return kieServices.newKieContainer(repository.getDefaultReleaseId());
    }

    @Bean
    @ConditionalOnMissingBean
    public KieBase kieBase(@Autowired KieContainer kieContainer) {
        return kieContainer.getKieBase();
    }

    @Bean
    @ConditionalOnMissingBean
    public KModuleBeanFactoryPostProcessor kiePostProcessor() {
        return new KModuleBeanFactoryPostProcessor();
    }
}
