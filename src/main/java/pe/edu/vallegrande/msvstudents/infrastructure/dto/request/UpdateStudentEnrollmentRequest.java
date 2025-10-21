package pe.edu.vallegrande.msvstudents.infrastructure.dto.request;

import lombok.Data;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentStatus;

@Data
public class UpdateStudentEnrollmentRequest {

    private EnrollmentStatus status;
    private String transferReason; // Used when status is 'TRANSFER'
}
