package uk.co.sancode.greet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorAttributesConfiguration {
    @Bean
    public ApplicationErrorAttributes applicationErrorAttributes() {
        return new ApplicationErrorAttributes();
    }
}
