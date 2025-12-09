package com.skillLink.skillLink.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwagerConfig {
    @Bean
  public  OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("my SkillLink ApI")
                        .description("API documentation for SkillLink application")
                        .version("10.0")

                );
    }
}
