package pe.edu.vallegrande.msvstudents.infrastructure.util;

import pe.edu.vallegrande.msvstudents.domain.model.Student;
import pe.edu.vallegrande.msvstudents.domain.model.StudentEnrollment;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.EnrollmentWithStudentResponse;

public class EnrollmentWithStudentMapper {

    public static EnrollmentWithStudentResponse toResponse(StudentEnrollment enrollment, Student student) {
        return EnrollmentWithStudentResponse.builder()
                // Datos de la matrícula
                .id(enrollment.getId())
                .studentId(enrollment.getStudentId())
                .classroomId(enrollment.getClassroomId())
                .institutionId(enrollment.getInstitutionId())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .enrollmentType(enrollment.getEnrollmentType())
                .status(enrollment.getStatus())
                .transferReason(enrollment.getTransferReason())
                .qrCode(enrollment.getQrCode())
                .createdAt(enrollment.getCreatedAt())
                .updatedAt(enrollment.getUpdatedAt())
                // Datos del estudiante
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .documentType(student.getDocumentType())
                .documentNumber(student.getDocumentNumber())
                .parentName(student.getParentName())
                .parentPhone(student.getParentPhone())
                .parentEmail(student.getParentEmail())
                .build();
    }
}