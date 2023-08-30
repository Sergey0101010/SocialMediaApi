package com.sergey.socialmediaapi.config;

import com.sergey.socialmediaapi.controllers.UserController;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                        .title("Social Media API")
                        .version("1.0")
                        .description("""
                                This is basic social media service API which allows users to make friends, post something and follow each other.
                                - [Project repository](https://github.com/Sergey0101010/SocialMediaApi)"""));
    }
}
