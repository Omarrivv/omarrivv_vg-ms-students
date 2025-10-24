package pe.edu.vallegrande.msvstudents.infrastructure.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class BulkEnrollmentsRequest {

    @NotNull(message = "Enrollments list is required")
    @Size(max = 500, message = "Cannot process more than 500 enrollments at once")
    @Valid
    private List<CreateStudentEnrollmentRequest> enrollments;
}