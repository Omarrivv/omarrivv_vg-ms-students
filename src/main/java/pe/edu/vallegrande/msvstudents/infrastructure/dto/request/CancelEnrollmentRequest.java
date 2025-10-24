package pe.edu.vallegrande.msvstudents.infrastructure.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelEnrollmentRequest {

    @NotBlank(message = "Cancellation reason is required")
    private String reason;
}