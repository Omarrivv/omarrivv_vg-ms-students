package pe.edu.vallegrande.msvstudents.application.service;

import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

public interface StudentService {

    Mono<StudentResponse> createStudent(CreateStudentRequest request, String institutionId);

    Mono<StudentResponse> findById(String id);

    Flux<StudentResponse> findAll();

    Flux<StudentResponse> getStudentsByInstitution(String institutionId);

    Mono<StudentResponse> updateStudent(String studentId, UpdateStudentRequest request, String institutionId);

    Flux<StudentResponse> getStudentsByTeacher(String teacherId, String institutionId);

    Mono<Boolean> existsById(String studentId);

    // Nuevos métodos para lógica de negocio
    Flux<StudentResponse> getUnenrolledStudents(String institutionId);
    
    Mono<Map<String, Object>> createStudentsBulk(List<CreateStudentRequest> requests, String institutionId);
    
    Flux<StudentResponse> searchStudents(String query, String institutionId);
    
    Flux<StudentResponse> getStudentsByGrade(String grade, String institutionId);
    
    Flux<StudentResponse> getStudentsByStatus(String status, String institutionId);
    
    Mono<Map<String, Object>> getStudentStatistics(String institutionId);
    
    Mono<StudentResponse> deactivateStudent(String studentId, String institutionId);
    
    Mono<StudentResponse> activateStudent(String studentId, String institutionId);

}