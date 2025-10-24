package pe.edu.vallegrande.msvstudents.infrastructure.dto.request;

import lombok.Data;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentStatus;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentType;

@Data
public class UpdateStudentEnrollmentRequest {

    private String classroomId;
    private EnrollmentType enrollmentType;
    private EnrollmentStatus status;
    private String transferReason; // Used when status is 'TRANSFER'
}
