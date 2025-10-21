package pe.edu.vallegrande.msvstudents.infrastructure.repository;

import pe.edu.vallegrande.msvstudents.domain.model.StudentEnrollment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StudentEnrollmentRepository {

    Mono<StudentEnrollment> save(StudentEnrollment enrollment);

    Mono<StudentEnrollment> findById(String id);

    Flux<StudentEnrollment> findByInstitutionId(String institutionId);

    Mono<Boolean> existsById(String enrollmentId);

    Mono<Boolean> existsByQrCode(String qrCode);

    Flux<StudentEnrollment> findByClassroomIdIn(List<String> classroomIds);

}