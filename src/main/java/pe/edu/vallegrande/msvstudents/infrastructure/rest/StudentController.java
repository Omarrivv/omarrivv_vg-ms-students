package pe.edu.vallegrande.msvstudents.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import pe.edu.vallegrande.msvstudents.application.service.StudentService;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.ApiResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.security.HeaderValidator;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // ENDPOINTS ESPECÍFICOS PRIMERO para evitar conflictos de mapeo

    @PostMapping("/secretary/create")
    public Mono<ApiResponse<Map<String, Object>>> createStudent(@Valid @RequestBody CreateStudentRequest request, ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return studentService.createStudent(request, headers.getInstitutionId())
                    .map(studentResponse -> ApiResponse.success(
                        Map.of("student", studentResponse), 
                        "Student created successfully"
                    ));
        });
    }

    @GetMapping("/secretary")
    public Mono<ApiResponse<List<StudentResponse>>> getStudentsByInstitution(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return studentService.getStudentsByInstitution(headers.getInstitutionId())
                    .collectList()
                    .map(studentResponses -> ApiResponse.success(
                        studentResponses, 
                        "Students retrieved successfully"
                    ));
        });
    }

    @PutMapping("/secretary/update/{studentId}")
    public Mono<ApiResponse<Map<String, Object>>> updateStudent(
            @PathVariable String studentId, 
            @Valid @RequestBody UpdateStudentRequest request, 
            ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return studentService.updateStudent(studentId, request, headers.getInstitutionId())
                    .map(studentResponse -> ApiResponse.success(
                        Map.of("student", studentResponse), 
                        "Student updated successfully"
                    ));
        });
    }

    @GetMapping("/teacher/my-students")
    public Mono<ApiResponse<List<StudentResponse>>> getMyStudents(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("TEACHER")
            );
            
            return studentService.getStudentsByTeacher(headers.getUserId(), headers.getInstitutionId())
                    .collectList()
                    .map(studentResponses -> ApiResponse.success(
                        studentResponses, 
                        "My students retrieved successfully"
                    ));
        });
    }

    @GetMapping("/auxiliary")
    public Mono<ApiResponse<List<StudentResponse>>> getStudentsAuxiliary(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("AUXILIARY")
            );
            
            return studentService.getStudentsByInstitution(headers.getInstitutionId())
                    .collectList()
                    .map(studentResponses -> ApiResponse.success(
                        studentResponses, 
                        "Students retrieved successfully"
                    ));
        });
    }

    // ENDPOINTS GENÉRICOS AL FINAL para evitar conflictos de mapping
    @GetMapping
    public Mono<ApiResponse<List<StudentResponse>>> findAll() {
        return studentService.findAll()
                .collectList()
                .map(studentResponses -> ApiResponse.success(
                    studentResponses, 
                    "Students retrieved successfully"
                ));
    }

    @GetMapping("/{id}")
    public Mono<ApiResponse<StudentResponse>> findById(@PathVariable String id) {
        return studentService.findById(id)
                .map(studentResponse -> ApiResponse.success(
                    studentResponse, 
                    "Student retrieved successfully"
                ));
    }

}
