package pe.edu.vallegrande.msvstudents.infrastructure.dto.request;

import lombok.Data;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class UpdateStudentRequest {

    private String firstName;

    private String lastName;

    private DocumentType documentType;

    private String documentNumber;

    private LocalDate birthDate;

    private Gender gender;

    private String address;

    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "Invalid phone format")
    private String phone;

    private String parentName;

    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "Invalid parent phone format")
    private String parentPhone;

    @Email(message = "Invalid parent email format")
    private String parentEmail;

    private Status status;
}