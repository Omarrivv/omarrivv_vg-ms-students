package pe.edu.vallegrande.msvstudents.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternalEnrollmentResponse {
    
    // Datos básicos de la matrícula
    private String id;
    private String studentId;
    private String classroomId;
    private EnrollmentStatus status;
    
    // Datos del estudiante
    private String firstName;
    private String lastName;
    private DocumentType documentType;
    private String documentNumber;
    private String parentName;
    private String parentPhone;
    private String parentEmail;
}