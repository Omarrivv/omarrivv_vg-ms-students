package pe.edu.vallegrande.msvstudents.infrastructure.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import pe.edu.vallegrande.msvstudents.domain.model.Student;
import pe.edu.vallegrande.msvstudents.domain.model.StudentEnrollment;
import pe.edu.vallegrande.msvstudents.domain.enums.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;
import org.springframework.data.mongodb.core.query.Query;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "pe.edu.vallegrande.msvstudents.infrastructure.repository")
public class MongoConfig {

    @Bean
    public CommandLineRunner initData(ReactiveMongoTemplate mongoTemplate) {
        return args -> {
            String institutionId = "96960392-1e5f-4e66-afc9-4b5bcd771d9f"; // Example institutionId

            mongoTemplate.count(new Query(), Student.class)
                .flatMap(count -> {
                    if (count == 0) {
                        LocalDateTime now = LocalDateTime.now();

                        Student student1 = new Student();
                        student1.setId(UUID.randomUUID().toString());
                        student1.setInstitutionId(institutionId);
                        student1.setFirstName("Juan Carlos");
                        student1.setLastName("González Pérez");
                        student1.setDocumentType(DocumentType.DNI);
                        student1.setDocumentNumber("78901234");
                        student1.setGender(Gender.MALE);
                        student1.setBirthDate(LocalDate.of(2015, 3, 15));
                        student1.setAddress("Jr. Los Pinos 123, Lima");
                        student1.setPhone("912345678");
                        student1.setParentName("Carlos González");
                        student1.setParentPhone("987654321");
                        student1.setParentEmail("carlos.gonzalez@mail.com");
                        student1.setStatus(Status.ACTIVE);
                        student1.setCreatedAt(now);
                        student1.setUpdatedAt(now);

                        Student student2 = new Student();
                        student2.setId(UUID.randomUUID().toString());
                        student2.setInstitutionId(institutionId);
                        student2.setFirstName("María Lucía");
                        student2.setLastName("Martínez López");
                        student2.setDocumentType(DocumentType.DNI);
                        student2.setDocumentNumber("78901235");
                        student2.setGender(Gender.FEMALE);
                        student2.setBirthDate(LocalDate.of(2014, 5, 20));
                        student2.setAddress("Av. Los Jardines 456, Miraflores");
                        student2.setPhone("912345679");
                        student2.setParentName("Rosa López");
                        student2.setParentPhone("987654322");
                        student2.setParentEmail("rosa.lopez@mail.com");
                        student2.setStatus(Status.ACTIVE);
                        student2.setCreatedAt(now);
                        student2.setUpdatedAt(now);

                        return mongoTemplate.insertAll(List.of(student1, student2))
                            .collectList()
                            .flatMapMany(students -> {
                                StudentEnrollment enrollment1 = new StudentEnrollment();
                                enrollment1.setId(UUID.randomUUID().toString());
                                enrollment1.setStudentId(students.get(0).getId());
                                enrollment1.setClassroomId("classroom-2024-001");
                                enrollment1.setEnrollmentDate(LocalDate.of(2024, 3, 1));
                                enrollment1.setEnrollmentType(EnrollmentType.REGULAR);
                                enrollment1.setStatus(EnrollmentStatus.ACTIVE);
                                enrollment1.setQrCode(String.format("{\"student_id\":\"%s\",\"classroom_id\":\"%s\"}", students.get(0).getId(), "classroom-2024-001"));
                                enrollment1.setCreatedAt(now);
                                enrollment1.setUpdatedAt(now);

                                StudentEnrollment enrollment2 = new StudentEnrollment();
                                enrollment2.setId(UUID.randomUUID().toString());
                                enrollment2.setStudentId(students.get(1).getId());
                                enrollment2.setClassroomId("classroom-2024-001");
                                enrollment2.setEnrollmentDate(LocalDate.of(2024, 3, 1));
                                enrollment2.setEnrollmentType(EnrollmentType.REGULAR);
                                enrollment2.setStatus(EnrollmentStatus.ACTIVE);
                                enrollment2.setQrCode(String.format("{\"student_id\":\"%s\",\"classroom_id\":\"%s\"}", students.get(1).getId(), "classroom-2024-001"));
                                enrollment2.setCreatedAt(now);
                                enrollment2.setUpdatedAt(now);

                                return mongoTemplate.insertAll(List.of(enrollment1, enrollment2));
                            })
                            .then();
                    } else {
                        return Mono.empty();
                    }
                })
                .block(); // Using block() here for simplicity in a CommandLineRunner
        };
    }
}