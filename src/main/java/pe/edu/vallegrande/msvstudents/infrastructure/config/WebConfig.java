package pe.edu.vallegrande.msvstudents.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {
    // Configuración básica de WebFlux sin restricciones CSP
} 