package com.zti.kha;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by up on 2/8/17.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig implements EnvironmentAware {
    private Environment environment;
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                 .enable(Boolean.parseBoolean(environment.getProperty("use-swagger")));

    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;

    }
}
