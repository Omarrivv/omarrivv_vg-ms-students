package pe.edu.vallegrande.msvstudents.infrastructure.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String headerName, String reason) {
        super(String.format("Invalid header '%s': %s", headerName, reason));
    }
}
