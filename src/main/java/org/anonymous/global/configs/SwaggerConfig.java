package org.anonymous.global.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info=@Info(title="은행 서비스", description = "계좌, 은행, 잔액 등 관리하는 서비스"))
public class SwaggerConfig {
    
    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("은행 서비스 v1")
                .pathsToMatch("/**")
                .build();
    }
}
