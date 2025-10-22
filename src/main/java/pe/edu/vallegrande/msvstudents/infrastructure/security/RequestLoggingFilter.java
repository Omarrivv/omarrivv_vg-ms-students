package pe.edu.vallegrande.msvstudents.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class RequestLoggingFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethodValue();
        String path = request.getURI().getPath();

        String userId = request.getHeaders().getFirst("X-User-Id");
        String roles = request.getHeaders().getFirst("X-User-Roles");
        String institution = request.getHeaders().getFirst("X-Institution-Id");

        if (log.isDebugEnabled()) {
            log.debug("Incoming request -> method={}, path={}, X-User-Id={}, X-User-Roles={}, X-Institution-Id={}",
                    method, path, userId, roles, institution);
        }

        return chain.filter(exchange);
    }
}
