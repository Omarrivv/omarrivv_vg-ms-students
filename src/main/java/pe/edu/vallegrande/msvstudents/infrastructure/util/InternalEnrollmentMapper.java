package pe.edu.vallegrande.msvstudents.infrastructure.util;

import pe.edu.vallegrande.msvstudents.domain.model.Student;
import pe.edu.vallegrande.msvstudents.domain.model.StudentEnrollment;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.InternalEnrollmentResponse;

public class InternalEnrollmentMapper {

    public static InternalEnrollmentResponse toResponse(StudentEnrollment enrollment, Student student) {
        return InternalEnrollmentResponse.builder()
                // Datos básicos de la matrícula
                .id(enrollment.getId())
                .studentId(enrollment.getStudentId())
                .classroomId(enrollment.getClassroomId())
                .status(enrollment.getStatus())
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