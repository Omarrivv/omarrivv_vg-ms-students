package pe.edu.vallegrande.msvstudents.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentStatus;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "enrollments")
public class StudentEnrollment {

    @Id
    private String id;

    @Field("student_id")
    @Indexed
    private String studentId;

    @Field("classroom_id")
    @Indexed
    private String classroomId;

    @Field("institution_id")
    @Indexed
    private String institutionId;

    @Field("enrollment_date")
    private LocalDate enrollmentDate;

    @Field("enrollment_type")
    private EnrollmentType enrollmentType;

    private EnrollmentStatus status;

    @Field("transfer_reason")
    private String transferReason;

    @Field("qr_code")
    @Indexed(unique = true)
    private String qrCode;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;
}