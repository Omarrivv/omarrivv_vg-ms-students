package pe.edu.vallegrande.msvstudents.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import pe.edu.vallegrande.msvstudents.application.service.StudentService;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.BulkStudentsRequest;
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

    // NUEVOS ENDPOINTS PARA LÓGICA DE NEGOCIO

    @GetMapping("/secretary/unenrolled")
    public Mono<ApiResponse<List<StudentResponse>>> getUnenrolledStudents(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return studentService.getUnenrolledStudents(headers.getInstitutionId())
                    .collectList()
                    .map(studentResponses -> ApiResponse.success(
                        studentResponses, 
                        "Unenrolled students retrieved successfully"
                    ));
        });
    }

    @PostMapping("/secretary/bulk-create")
    public Mono<ApiResponse<Map<String, Object>>> createStudentsBulk(
            @Valid @RequestBody BulkStudentsRequest request, 
            ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return studentService.createStudentsBulk(request.getStudents(), headers.getInstitutionId())
                    .map(result -> ApiResponse.success(
                        result, 
                        "Bulk student creation completed"
                    ));
        });
    }

    @GetMapping("/secretary/search")
    public Mono<ApiResponse<List<StudentResponse>>> searchStudents(
            @RequestParam String query,
            ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return studentService.searchStudents(query, headers.getInstitutionId())
                    .collectList()
                    .map(studentResponses -> ApiResponse.success(
                        studentResponses, 
                        "Students search completed successfully"
                    ));
        });
    }

    @GetMapping("/secretary/by-grade/{grade}")
    public Mono<ApiResponse<List<StudentResponse>>> getStudentsByGrade(
            @PathVariable String grade,
            ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return studentService.getStudentsByGrade(grade, headers.getInstitutionId())
                    .collectList()
                    .map(studentResponses -> ApiResponse.success(
                        studentResponses, 
                        "Students by grade retrieved successfully"
                    ));
        });
    }

    @GetMapping("/secretary/by-status/{status}")
    public Mono<ApiResponse<List<StudentResponse>>> getStudentsByStatus(
            @PathVariable String status,
            ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return studentService.getStudentsByStatus(status, headers.getInstitutionId())
                    .collectList()
                    .map(studentResponses -> ApiResponse.success(
                        studentResponses, 
                        "Students by status retrieved successfully"
                    ));
        });
    }

    @GetMapping("/secretary/statistics")
    public Mono<ApiResponse<Map<String, Object>>> getStudentStatistics(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return studentService.getStudentStatistics(headers.getInstitutionId())
                    .map(statistics -> ApiResponse.success(
                        statistics, 
                        "Student statistics retrieved successfully"
                    ));
        });
    }

    @PutMapping("/secretary/deactivate/{studentId}")
    public Mono<ApiResponse<Map<String, Object>>> deactivateStudent(
            @PathVariable String studentId,
            ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return studentService.deactivateStudent(studentId, headers.getInstitutionId())
                    .map(studentResponse -> ApiResponse.success(
                        Map.of("student", studentResponse), 
                        "Student deactivated successfully"
                    ));
        });
    }

    @PutMapping("/secretary/activate/{studentId}")
    public Mono<ApiResponse<Map<String, Object>>> activateStudent(
            @PathVariable String studentId,
            ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return studentService.activateStudent(studentId, headers.getInstitutionId())
                    .map(studentResponse -> ApiResponse.success(
                        Map.of("student", studentResponse), 
                        "Student activated successfully"
                    ));
        });
    }

}
