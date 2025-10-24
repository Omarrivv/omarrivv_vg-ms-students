package pe.edu.vallegrande.msvstudents.infrastructure.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class BulkStudentsRequest {

    @NotNull(message = "Students list is required")
    @Size(max = 1000, message = "Cannot process more than 1000 students at once")
    @Valid
    private List<CreateStudentRequest> students;
}