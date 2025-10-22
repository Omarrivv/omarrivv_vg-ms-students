package pe.edu.vallegrande.msvstudents.infrastructure.repository;

import pe.edu.vallegrande.msvstudents.domain.model.Student;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentRepository {

    Mono<Student> save(Student student);

    Mono<Student> findById(String id);

    Flux<Student> findAll();

    Flux<Student> findByInstitutionId(String institutionId);

    Mono<Boolean> existsById(String studentId);

    // Assuming this might be needed for validation
    Mono<Student> findByDocumentNumberAndInstitutionId(String documentNumber, String institutionId);

}