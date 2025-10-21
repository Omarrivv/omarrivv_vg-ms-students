package pe.edu.vallegrande.msvstudents.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import pe.edu.vallegrande.msvstudents.application.service.StudentService;
import pe.edu.vallegrande.msvstudents.domain.enums.UserRole;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.ApiResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.security.HeaderValidator;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/secretary/create")
    public Mono<ApiResponse<Map<String, Object>>> createStudent(@Valid @RequestBody CreateStudentRequest request, ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = exchange.getAttributeOrDefault("validationResult", null);
            if (headers == null) {
                return Mono.error(new RuntimeException("Missing authentication headers (validationResult)."));
            }
            HeaderValidator.validateRole(headers, UserRole.SECRETARY);
            return studentService.createStudent(request, headers.getInstitutionId())
                    .map(studentResponse -> ApiResponse.success(Map.of("student", studentResponse), "Student created successfully"));
        });
    }

    @GetMapping("/secretary")
    public Mono<ApiResponse<List<StudentResponse>>> getStudentsByInstitution(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = exchange.getAttributeOrDefault("validationResult", null);
            if (headers == null) {
                return Mono.error(new RuntimeException("Missing authentication headers (validationResult)."));
            }
            HeaderValidator.validateRole(headers, UserRole.SECRETARY);
            return studentService.getStudentsByInstitution(headers.getInstitutionId())
                    .collectList()
                    .map(studentResponses -> ApiResponse.success(studentResponses, "Students retrieved successfully"));
        });
    }

    @PutMapping("/secretary/update/{studentId}")
    public Mono<ApiResponse<Map<String, Object>>> updateStudent(@PathVariable String studentId, @Valid @RequestBody UpdateStudentRequest request, ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = exchange.getAttributeOrDefault("validationResult", null);
            if (headers == null) {
                return Mono.error(new RuntimeException("Missing authentication headers (validationResult)."));
            }
            HeaderValidator.validateRole(headers, UserRole.SECRETARY);
            return studentService.updateStudent(studentId, request, headers.getInstitutionId())
                    .map(studentResponse -> ApiResponse.success(Map.of("student", studentResponse), "Student updated successfully"));
        });
    }

    @GetMapping("/teacher/my-students")
    public Mono<ApiResponse<List<StudentResponse>>> getMyStudents(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = exchange.getAttributeOrDefault("validationResult", null);
            if (headers == null) {
                return Mono.error(new RuntimeException("Missing authentication headers (validationResult)."));
            }
            HeaderValidator.validateRole(headers, UserRole.TEACHER);
            return studentService.getStudentsByTeacher(headers.getUserId(), headers.getInstitutionId())
                    .collectList()
                    .map(studentResponses -> ApiResponse.success(studentResponses, "My students retrieved successfully"));
        });
    }

    @GetMapping("/auxiliary")
    public Mono<ApiResponse<List<StudentResponse>>> getStudentsAuxiliary(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = exchange.getAttributeOrDefault("validationResult", null);
            if (headers == null) {
                return Mono.error(new RuntimeException("Missing authentication headers (validationResult)."));
            }
            HeaderValidator.validateRole(headers, UserRole.AUXILIARY);
            return studentService.getStudentsByInstitution(headers.getInstitutionId())
                    .collectList()
                    .map(list -> ApiResponse.success(list, "Auxiliary students retrieved successfully"));
        });
    }
}