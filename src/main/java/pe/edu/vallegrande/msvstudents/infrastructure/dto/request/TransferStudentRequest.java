package pe.edu.vallegrande.msvstudents.infrastructure.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransferStudentRequest {

    @NotBlank(message = "New classroom ID is required")
    private String newClassroomId;
    
    @NotBlank(message = "Transfer reason is required")
    private String reason;
}