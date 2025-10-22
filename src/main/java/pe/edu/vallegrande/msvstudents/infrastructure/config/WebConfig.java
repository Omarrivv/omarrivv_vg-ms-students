package pe.edu.vallegrande.msvstudents.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.WebFilter;
import pe.edu.vallegrande.msvstudents.infrastructure.security.PathValidationFilter;

@Configuration
@EnableWebFlux
public class WebConfig {

    @Bean
    public WebFilter pathValidationFilter() {
        return new PathValidationFilter();
    }
}