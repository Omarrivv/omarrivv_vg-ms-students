package pe.edu.vallegrande.msvstudents.infrastructure.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.server.ServerWebExchange;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.custom.InsufficientPermissionsException;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.custom.InvalidTokenException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HeaderValidator {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HeaderValidationResult {
        private String userId;
        private List<String> userRoles;
        private String institutionId;
    }

    /**
     * Validación simple de headers - Similar al flujo de Python
     * Solo valida los headers básicos sin complejidad adicional
     */
    public static HeaderValidationResult validateHeadersSimple(ServerWebExchange exchange, List<String> requiredRoles) {
        String userIdHeader = exchange.getRequest().getHeaders().getFirst("X-User-Id");
        String userRolesHeader = exchange.getRequest().getHeaders().getFirst("X-User-Roles");
        String institutionIdHeader = exchange.getRequest().getHeaders().getFirst("X-Institution-Id");

        // Validaciones básicas
        if (userIdHeader == null || userIdHeader.trim().isEmpty()) {
            throw new InvalidTokenException("X-User-Id", "Header is required");
        }

        if (userRolesHeader == null || userRolesHeader.trim().isEmpty()) {
            throw new InvalidTokenException("X-User-Roles", "Header is required");
        }

        // Parsear roles
        List<String> userRoles = Arrays.stream(userRolesHeader.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        if (userRoles.isEmpty()) {
            throw new InvalidTokenException("X-User-Roles", "No valid roles found in header");
        }

        // Validar que el usuario tenga al menos uno de los roles requeridos
        if (requiredRoles != null && !requiredRoles.isEmpty()) {
            boolean hasRequiredRole = requiredRoles.stream()
                    .anyMatch(requiredRole -> userRoles.contains(requiredRole.toUpperCase()));
            
            if (!hasRequiredRole) {
                throw new InsufficientPermissionsException(
                    String.join(",", requiredRoles), 
                    "access this endpoint"
                );
            }
        }

        // Para secretary y teacher, institutionId es obligatorio
        boolean requiresInstitutionId = userRoles.contains("SECRETARY") || userRoles.contains("TEACHER");
        if (requiresInstitutionId && (institutionIdHeader == null || institutionIdHeader.trim().isEmpty())) {
            throw new InvalidTokenException("X-Institution-Id", "Header is required for secretary or teacher roles");
        }

        return HeaderValidationResult.builder()
                .userId(userIdHeader.trim())
                .userRoles(userRoles)
                .institutionId(institutionIdHeader != null ? institutionIdHeader.trim() : null)
                .build();
    }

    /**
     * Validar acceso de secretaria - Similar a validate_secretary_access en Python
     */
    public static boolean validateSecretaryAccess(List<String> userRoles) {
        return userRoles.contains("SECRETARY");
    }

    /**
     * Validar acceso de profesor - Similar a validate_teacher_access en Python
     */
    public static boolean validateTeacherAccess(List<String> userRoles) {
        return userRoles.contains("TEACHER");
    }

    /**
     * Validar acceso de auxiliar
     */
    public static boolean validateAuxiliaryAccess(List<String> userRoles) {
        return userRoles.contains("AUXILIARY");
    }
}
