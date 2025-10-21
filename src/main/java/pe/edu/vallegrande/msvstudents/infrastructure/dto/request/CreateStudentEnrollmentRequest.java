package pe.edu.vallegrande.msvstudents.infrastructure.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentType;

import java.time.LocalDate;

@Data
public class CreateStudentEnrollmentRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Classroom ID is required")
    private String classroomId;

    @NotNull(message = "Enrollment date is required")
    private LocalDate enrollmentDate;

    @NotNull(message = "Enrollment type is required")
    private EnrollmentType enrollmentType;
}