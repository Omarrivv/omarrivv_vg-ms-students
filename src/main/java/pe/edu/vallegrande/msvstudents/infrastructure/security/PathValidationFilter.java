package pe.edu.vallegrande.msvstudents.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class PathValidationFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(PathValidationFilter.class);

    private static final Set<String> ROLE_TOKENS = new HashSet<>(Arrays.asList(
            "SECRETARY", "TEACHER", "AUXILIARY"
    ));

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Normalize and split
        String[] segments = Arrays.stream(path.split("/"))
                .filter(s -> !s.isBlank())
                .toArray(String[]::new);

        if (segments.length > 0) {
            // Inspect last segment: if it's a role name, likely gateway misrouted and put role where id should be
            String last = segments[segments.length - 1];
            if (ROLE_TOKENS.contains(last.toUpperCase())) {
                String msg = String.format("Invalid request path: found role '%s' in path end. This likely means the gateway forwarded the role as an identifier or omitted the resource id. Check gateway path mapping.", last);
                String method = exchange.getRequest().getMethod() != null ? exchange.getRequest().getMethod().name() : "UNKNOWN";
                log.warn("{} -> method={} path={}", msg, method, path);

                byte[] bytes = ("{\"error\": \"" + msg + "\"}").getBytes(StandardCharsets.UTF_8);
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
            }

            // Validate if the last segment matches the expected ID format (e.g., UUID or numeric)
            if (!last.matches("^[a-zA-Z0-9-]+$")) {
                String msg = String.format("Invalid request path: '%s' is not a valid identifier. Expected alphanumeric or UUID format.", last);
                String method = exchange.getRequest().getMethod() != null ? exchange.getRequest().getMethod().name() : "UNKNOWN";
                log.warn("{} -> method={} path={}", msg, method, path);

                byte[] bytes = ("{\"error\": \"" + msg + "\"}").getBytes(StandardCharsets.UTF_8);
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
            }
        }

        return chain.filter(exchange);
    }
}
