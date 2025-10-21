package pe.edu.vallegrande.msvstudents.infrastructure.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentStatus;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentEnrollmentResponse {
    private String id;
    private String studentId;
    private String classroomId;
    private String institutionId;
    private LocalDate enrollmentDate;
    private EnrollmentType enrollmentType;
    private EnrollmentStatus status;
    private String transferReason;
    private String qrCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}