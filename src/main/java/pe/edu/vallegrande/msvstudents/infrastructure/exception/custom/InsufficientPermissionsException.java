package pe.edu.vallegrande.msvstudents.infrastructure.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InsufficientPermissionsException extends RuntimeException {
    public InsufficientPermissionsException(String requiredRole, String action) {
        super(String.format("Insufficient permissions. Role '%s' is required to %s.", requiredRole, action));
    }
}
