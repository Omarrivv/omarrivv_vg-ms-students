package pe.edu.vallegrande.msvstudents.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import pe.edu.vallegrande.msvstudents.domain.models.ClassroomStudent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ClassroomStudentRepository extends ReactiveMongoRepository<ClassroomStudent, String> {
    Flux<ClassroomStudent> findByStudentId(String studentId);
    Flux<ClassroomStudent> findByClassroomId(String classroomId);
    Flux<ClassroomStudent> findByStatus(String status);
    Flux<ClassroomStudent> findByStudentIdAndStatus(String studentId, String status);
    Flux<ClassroomStudent> findByClassroomIdAndStatus(String classroomId, String status);
    
    // Método para verificar si un estudiante ya tiene una matrícula activa
    Mono<Boolean> existsByStudentIdAndStatus(String studentId, String status);

    // Métodos para búsqueda por año y periodo
    Flux<ClassroomStudent> findByEnrollmentYear(String year);
    Flux<ClassroomStudent> findByEnrollmentPeriod(String period);
    Flux<ClassroomStudent> findByEnrollmentYearAndEnrollmentPeriod(String year, String period);
} 