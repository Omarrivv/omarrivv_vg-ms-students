package pe.edu.vallegrande.msvstudents.infrastructure.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import pe.edu.vallegrande.msvstudents.domain.enums.UserRole;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.custom.InsufficientPermissionsException;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.custom.InvalidTokenException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HeaderValidator {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HeaderValidationResult {
        private String userId;
        private List<UserRole> userRoles;
        private String institutionId;
    }

    public static HeaderValidationResult validateHeaders(ServerHttpRequest request) {
        String userIdHeader = request.getHeaders().getFirst("X-User-Id");
        String userRolesHeader = request.getHeaders().getFirst("X-User-Roles");
        String institutionIdHeader = request.getHeaders().getFirst("X-Institution-Id");

        if (userIdHeader == null || userIdHeader.trim().isEmpty()) {
            throw new InvalidTokenException("X-User-Id", "Header is required");
        }

        if (userRolesHeader == null || userRolesHeader.trim().isEmpty()) {
            throw new InvalidTokenException("X-User-Roles", "Header is required");
        }

        List<UserRole> userRoles;
        try {
            userRoles = Arrays.stream(userRolesHeader.split(","))
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .map(UserRole::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new InvalidTokenException("X-User-Roles", "Invalid value format or contains invalid roles");
        }

        if (userRoles.isEmpty()) {
            throw new InvalidTokenException("X-User-Roles", "No valid roles found in header");
        }

        // For SECRETARY and TEACHER, institutionId is mandatory
        boolean requiresInstitutionId = userRoles.contains(UserRole.SECRETARY) || userRoles.contains(UserRole.TEACHER);
        if (requiresInstitutionId && (institutionIdHeader == null || institutionIdHeader.trim().isEmpty())) {
            throw new InvalidTokenException("X-Institution-Id", "Header is required for SECRETARY or TEACHER roles");
        }

        return HeaderValidationResult.builder()
                .userId(userIdHeader.trim())
                .userRoles(userRoles)
                .institutionId(institutionIdHeader != null ? institutionIdHeader.trim() : null)
                .build();
    }

    public static void validateRole(HeaderValidationResult headers, UserRole requiredRole) {
        if (!headers.getUserRoles().contains(requiredRole)) {
            throw new InsufficientPermissionsException(requiredRole.name(), "access this endpoint");
        }
    }
}
