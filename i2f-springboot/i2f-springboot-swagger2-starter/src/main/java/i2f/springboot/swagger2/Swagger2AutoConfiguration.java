package i2f.springboot.swagger2;

import i2f.springboot.swagger2.properties.Swagger2ApiInfoProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Ice2Faith
 * @date 2024/6/26 20:25
 * @desc
 */
@ConditionalOnExpression("${i2f.swagger2.enable:true}")
@Slf4j
@Data
@EnableConfigurationProperties({
        Swagger2ApiInfoProperties.class
})
@Import({
        Swagger2RestfulConfiguration.class,
        DynamicSwaggerApisConfiguration.class
})
@ConfigurationProperties(prefix = "i2f.swagger2.apis")
public class Swagger2AutoConfiguration {

    @ConditionalOnMissingBean(ApiInfo.class)
    @Bean
    public ApiInfo apiInfo(Swagger2ApiInfoProperties properties) {
        log.info("SwaggerConfig api info config.");
        return new ApiInfoBuilder().title(properties.getTitle())
                .description(properties.getDescription())
                .contact(new Contact(null, null, null))
                .license(properties.getLicense())
                .licenseUrl(properties.getLicenseUrl())
                .version(properties.getVersion())
                .build();
    }

    @ConditionalOnExpression("${i2f.swagger2.apis.all.enable:true}")
    @Bean
    public Docket allApi(ApiInfo apiInfo) {
        log.info("SwaggerConfig all api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("all")
                .select()
                .apis(RequestHandlerSelectors
                        .any())
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }


}
