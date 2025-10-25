package pe.edu.vallegrande.msvstudents.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentStatus;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentWithStudentResponse {
    
    // Datos de la matrícula
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
    
    // Datos del estudiante
    private String firstName;
    private String lastName;
    private DocumentType documentType;
    private String documentNumber;
    private String parentName;
    private String parentPhone;
    private String parentEmail;
}