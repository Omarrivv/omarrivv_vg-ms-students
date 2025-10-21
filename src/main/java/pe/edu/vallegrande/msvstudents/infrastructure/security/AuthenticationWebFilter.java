package pe.edu.vallegrande.msvstudents.infrastructure.security;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationWebFilter implements WebFilter {

    // Define public paths that don't require authentication
    private static final List<String> PUBLIC_PATHS = List.of(
            "/validate-students/",
            "/validate-enrollments/",
            "/validate-qr/"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Skip validation for public paths
        boolean isPublicPath = PUBLIC_PATHS.stream().anyMatch(path::startsWith);
        if (isPublicPath) {
            return chain.filter(exchange);
        }

        try {
            HeaderValidator.HeaderValidationResult validationResult = HeaderValidator.validateHeaders(exchange.getRequest());
            // Guardar el resultado de validación como atributo en el ServerWebExchange
            // Los controladores usan exchange.getAttributeOrDefault("validationResult", null)
            exchange.getAttributes().put("validationResult", validationResult);
            return chain.filter(exchange);
        } catch (Exception e) {
            // Dejar que el GlobalExceptionHandler maneje la respuesta
            return Mono.error(e);
        }
    }
}
