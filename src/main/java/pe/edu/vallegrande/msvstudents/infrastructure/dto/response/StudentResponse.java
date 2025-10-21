package pe.edu.vallegrande.msvstudents.infrastructure.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentResponse {
    private String id;
    private String institutionId;
    private String firstName;
    private String lastName;
    private DocumentType documentType;
    private String documentNumber;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String phone;
    private String parentPhone;
    private String parentEmail;
    private String parentName;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}