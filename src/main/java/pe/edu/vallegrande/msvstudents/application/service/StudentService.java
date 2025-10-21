package pe.edu.vallegrande.msvstudents.application.service;

import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentService {

    Mono<StudentResponse> createStudent(CreateStudentRequest request, String institutionId);

    Flux<StudentResponse> getStudentsByInstitution(String institutionId);

    Mono<StudentResponse> updateStudent(String studentId, UpdateStudentRequest request, String institutionId);

    Flux<StudentResponse> getStudentsByTeacher(String teacherId, String institutionId);

    Mono<Boolean> existsById(String studentId);

}