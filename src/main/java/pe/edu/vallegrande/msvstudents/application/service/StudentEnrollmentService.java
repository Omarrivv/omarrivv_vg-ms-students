package pe.edu.vallegrande.msvstudents.application.service;

import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentEnrollmentResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

public interface StudentEnrollmentService {

    Mono<StudentEnrollmentResponse> createEnrollment(CreateStudentEnrollmentRequest request, String institutionId);

    Flux<StudentEnrollmentResponse> getEnrollmentsByInstitution(String institutionId);

    Mono<StudentEnrollmentResponse> updateEnrollment(String enrollmentId, UpdateStudentEnrollmentRequest request, String institutionId);

    Flux<StudentEnrollmentResponse> getEnrollmentsByTeacher(String teacherId, String institutionId);

    Flux<StudentEnrollmentResponse> getEnrollmentsByClassroom(String classroomId, String institutionId);

    Mono<StudentEnrollmentResponse> getEnrollmentById(String enrollmentId, String institutionId);

    Mono<StudentEnrollmentResponse> updateEnrollmentObservations(String enrollmentId, String observations, String teacherId);

    Mono<Boolean> existsById(String enrollmentId);

    Mono<Boolean> existsByQrCode(String qrCode);

    // Nuevos métodos para lógica de negocio
    Mono<Map<String, Object>> createBulkEnrollments(List<CreateStudentEnrollmentRequest> requests, String institutionId);
    
    Flux<StudentEnrollmentResponse> getEnrollmentsByStatus(String status, String institutionId);
    
    Mono<Map<String, Object>> getEnrollmentStatistics(String institutionId);
    
    Mono<StudentEnrollmentResponse> transferStudent(String enrollmentId, String newClassroomId, String reason, String institutionId);
    
    Flux<StudentEnrollmentResponse> getEnrollmentsByStudent(String studentId, String institutionId);
    
    Mono<StudentEnrollmentResponse> cancelEnrollment(String enrollmentId, String reason, String institutionId);

}