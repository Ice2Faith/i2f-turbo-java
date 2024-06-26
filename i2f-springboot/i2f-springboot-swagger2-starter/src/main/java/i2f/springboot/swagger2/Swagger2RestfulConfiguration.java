package i2f.springboot.swagger2;

import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Ice2Faith
 * @date 2024/6/26 20:43
 * @desc
 */
@Slf4j
@ConditionalOnExpression("${i2f.swagger2.apis.rest.enable:true}")
public class Swagger2RestfulConfiguration {

    @ConditionalOnExpression("${i2f.swagger2.apis.rest.all.enable:true}")
    @Bean
    public Docket restfulApi(ApiInfo apiInfo) {
        log.info("SwaggerConfig restful api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("20-rest-all")
                .select()
                .apis(Predicates.or(RequestHandlerSelectors
                                        .withClassAnnotation(RestController.class),
                                RequestHandlerSelectors
                                        .withClassAnnotation(ResponseBody.class),
                                RequestHandlerSelectors
                                        .withMethodAnnotation(ResponseBody.class),
                                RequestHandlerSelectors
                                        .withMethodAnnotation(GetMapping.class),
                                RequestHandlerSelectors
                                        .withMethodAnnotation(PostMapping.class),
                                RequestHandlerSelectors
                                        .withMethodAnnotation(PutMapping.class),
                                RequestHandlerSelectors
                                        .withMethodAnnotation(DeleteMapping.class)
                        )
                )
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }

    @ConditionalOnExpression("${i2f.swagger2.apis.rest.web.enable:true}")
    @Bean
    public Docket webApi(ApiInfo apiInfo) {
        log.info("SwaggerConfig web api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("30-web")
                .select()
                .apis(RequestHandlerSelectors
                        .withClassAnnotation(Controller.class))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }

    @ConditionalOnExpression("${i2f.swagger2.apis.rest.get.enable:true}")
    @Bean
    public Docket getApi(ApiInfo apiInfo) {
        log.info("SwaggerConfig get api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("40-rest-get")
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors
                                .withMethodAnnotation(GetMapping.class)
                ))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }

    @ConditionalOnExpression("${i2f.swagger2.apis.rest.post.enable:true}")
    @Bean
    public Docket postApi(ApiInfo apiInfo) {
        log.info("SwaggerConfig post api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("50-rest-post")
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors
                                .withMethodAnnotation(PostMapping.class)
                ))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }

    @ConditionalOnExpression("${i2f.swagger2.apis.rest.put.enable:true}")
    @Bean
    public Docket putApi(ApiInfo apiInfo) {
        log.info("SwaggerConfig put api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("60-rest-put")
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors
                                .withMethodAnnotation(PutMapping.class)
                ))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }

    @ConditionalOnExpression("${i2f.swagger2.apis.rest.delete.enable:true}")
    @Bean
    public Docket deleteApi(ApiInfo apiInfo) {
        log.info("SwaggerConfig delete api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("70-rest-delete")
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors
                                .withMethodAnnotation(DeleteMapping.class)
                ))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }
}
