package pe.edu.vallegrande.msvstudents.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "students")
public class Student {

    @Id
    private String id;

    @Field("institution_id")
    private String institutionId;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("document_type")
    private DocumentType documentType;

    @Field("document_number")
    @Indexed(unique = true)
    private String documentNumber;

    @Field("birth_date")
    private LocalDate birthDate;

    private Gender gender;

    private String address;

    private String phone;

    @Field("parent_phone")
    private String parentPhone;

    @Field("parent_email")
    private String parentEmail;

    @Field("parent_name")
    private String parentName;

    private Status status;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;
}