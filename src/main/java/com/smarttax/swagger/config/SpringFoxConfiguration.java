package com.smarttax.swagger.config;

import com.google.common.collect.Lists;
import com.smarttax.swagger.annotations.MsGalaxyController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * 项目名称:
 * 模块名称:
 * 说明:
 * JDK 版本:      1.8
 * 作者(@author): Justin
 * 创建日期:      2019/10/11 10:03
 */
@Profile({"dev", "test", "local", "fat"})
@EnableSwagger2
@Configuration
public class SpringFoxConfiguration {

    @Bean
    public Docket apiV2Docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(apiKeys())
                .securityContexts(newArrayList(securityContext()))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(MsGalaxyController.class))
                .build()
                .groupName("api-v2");
    }

    private List<ApiKey> apiKeys() {
        return Lists.newArrayList(
                new ApiKey("X-Access-Token", "X-Access-Token", "header"),
                new ApiKey("X-Operation-Token", "X-Operation-Token", "header")
        );
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference("X-Access-Token", authorizationScopes),
                new SecurityReference("X-Operation-Token", authorizationScopes));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(path -> !PathSelectors.regex("/security/.*").apply(path))
                .build();
    }

}
