package pe.edu.vallegrande.msvstudents.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import pe.edu.vallegrande.msvstudents.application.service.StudentEnrollmentService;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.BulkEnrollmentsRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CancelEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.TransferStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.ApiResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentEnrollmentResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.security.HeaderValidator;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class StudentEnrollmentController {

    private final StudentEnrollmentService enrollmentService;

    @PostMapping("/secretary/create")
    public Mono<ApiResponse<Map<String, Object>>> createEnrollment(@Valid @RequestBody CreateStudentEnrollmentRequest request, ServerWebExchange exchange) {
        return Mono.defer(() -> {
            // Validación simple de headers - Similar al flujo Python
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return enrollmentService.createEnrollment(request, headers.getInstitutionId())
                    .map(response -> ApiResponse.success(Map.of("enrollment", response), "Enrollment created successfully"));
        });
    }

    @GetMapping("/secretary")
    public Mono<ApiResponse<List<StudentEnrollmentResponse>>> getEnrollmentsByInstitution(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            // Validación simple de headers para secretaria
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return enrollmentService.getEnrollmentsByInstitution(headers.getInstitutionId())
                    .collectList()
                    .map(responses -> ApiResponse.success(responses, "Enrollments retrieved successfully"));
        });
    }

    @PutMapping("/secretary/update/{enrollmentId}")
    public Mono<ApiResponse<Map<String, Object>>> updateEnrollment(@PathVariable String enrollmentId, @Valid @RequestBody UpdateStudentEnrollmentRequest request, ServerWebExchange exchange) {
        return Mono.defer(() -> {
            // Validación simple de headers para secretaria
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return enrollmentService.updateEnrollment(enrollmentId, request, headers.getInstitutionId())
                    .map(response -> ApiResponse.success(Map.of("enrollment", response), "Enrollment updated successfully"));
        });
    }

    @GetMapping("/teacher/my-enrollments")
    public Mono<ApiResponse<List<StudentEnrollmentResponse>>> getMyEnrollments(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            // Validación simple de headers para profesor
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("TEACHER")
            );
            
            return enrollmentService.getEnrollmentsByTeacher(headers.getUserId(), headers.getInstitutionId())
                    .collectList()
                    .map(responses -> ApiResponse.success(responses, "My enrollments retrieved successfully"));
        });
    }
    
    // The DTO for this was not specified, so a simple Map is used for the request body for now.
    @PutMapping("/teacher/observations/{enrollmentId}")
    public Mono<ApiResponse<Map<String, Object>>> updateEnrollmentObservations(@PathVariable String enrollmentId, @RequestBody Map<String, String> requestBody, ServerWebExchange exchange) {
        return Mono.defer(() -> {
            // Validación simple de headers para profesor
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("TEACHER")
            );
            
            String observations = requestBody.get("observations");
            return enrollmentService.updateEnrollmentObservations(enrollmentId, observations, headers.getUserId())
                    .map(response -> ApiResponse.success(Map.of("enrollment", response), "Observations updated successfully"));
        });
    }

    @GetMapping("/secretary/by-classroom/{classroomId}")
    public Mono<ApiResponse<List<StudentEnrollmentResponse>>> getEnrollmentsByClassroom(@PathVariable String classroomId, ServerWebExchange exchange) {
        return Mono.defer(() -> {
            // Validación simple de headers para secretaria
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return enrollmentService.getEnrollmentsByClassroom(classroomId, headers.getInstitutionId())
                    .collectList()
                    .map(responses -> ApiResponse.success(responses, "Enrollments by classroom retrieved successfully"));
        });
    }

    @GetMapping("/secretary/qr/{enrollmentId}")
    public Mono<ApiResponse<Map<String, Object>>> getEnrollmentQr(@PathVariable String enrollmentId, ServerWebExchange exchange) {
        return Mono.defer(() -> {
            // Validación simple de headers para secretaria
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return enrollmentService.getEnrollmentById(enrollmentId, headers.getInstitutionId())
                    .map(enrollment -> ApiResponse.success(Map.of("qrCode", enrollment.getQrCode()), "QR retrieved successfully"));
        });
    }

    @GetMapping("/auxiliary/by-classroom/{classroomId}")
    public Mono<ApiResponse<List<StudentEnrollmentResponse>>> getEnrollmentsByClassroomAuxiliary(@PathVariable String classroomId, ServerWebExchange exchange) {
        return Mono.defer(() -> {
            // Validación simple de headers para auxiliar
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("AUXILIARY")
            );
            
            return enrollmentService.getEnrollmentsByClassroom(classroomId, headers.getInstitutionId())
                    .collectList()
                    .map(list -> ApiResponse.success(list, "Auxiliary enrollments by classroom retrieved successfully"));
        });
    }

    // NUEVOS ENDPOINTS PARA LÓGICA DE NEGOCIO

    @PostMapping("/secretary/bulk-create")
    public Mono<ApiResponse<Map<String, Object>>> createBulkEnrollments(
            @Valid @RequestBody BulkEnrollmentsRequest request, 
            ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return enrollmentService.createBulkEnrollments(request.getEnrollments(), headers.getInstitutionId())
                    .map(result -> ApiResponse.success(
                        result, 
                        "Bulk enrollment creation completed"
                    ));
        });
    }

    @GetMapping("/secretary/by-status/{status}")
    public Mono<ApiResponse<List<StudentEnrollmentResponse>>> getEnrollmentsByStatus(
            @PathVariable String status,
            ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return enrollmentService.getEnrollmentsByStatus(status, headers.getInstitutionId())
                    .collectList()
                    .map(enrollments -> ApiResponse.success(
                        enrollments, 
                        "Enrollments by status retrieved successfully"
                    ));
        });
    }

    @GetMapping("/secretary/statistics")
    public Mono<ApiResponse<Map<String, Object>>> getEnrollmentStatistics(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return enrollmentService.getEnrollmentStatistics(headers.getInstitutionId())
                    .map(statistics -> ApiResponse.success(
                        statistics, 
                        "Enrollment statistics retrieved successfully"
                    ));
        });
    }

    @PutMapping("/secretary/transfer/{enrollmentId}")
    public Mono<ApiResponse<Map<String, Object>>> transferStudent(
            @PathVariable String enrollmentId,
            @Valid @RequestBody TransferStudentRequest request,
            ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return enrollmentService.transferStudent(
                    enrollmentId, 
                    request.getNewClassroomId(), 
                    request.getReason(), 
                    headers.getInstitutionId())
                    .map(enrollment -> ApiResponse.success(
                        Map.of("enrollment", enrollment), 
                        "Student transferred successfully"
                    ));
        });
    }

    @GetMapping("/secretary/by-student/{studentId}")
    public Mono<ApiResponse<List<StudentEnrollmentResponse>>> getEnrollmentsByStudent(
            @PathVariable String studentId,
            ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return enrollmentService.getEnrollmentsByStudent(studentId, headers.getInstitutionId())
                    .collectList()
                    .map(enrollments -> ApiResponse.success(
                        enrollments, 
                        "Enrollments by student retrieved successfully"
                    ));
        });
    }

    @PutMapping("/secretary/cancel/{enrollmentId}")
    public Mono<ApiResponse<Map<String, Object>>> cancelEnrollment(
            @PathVariable String enrollmentId,
            @Valid @RequestBody CancelEnrollmentRequest request,
            ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return enrollmentService.cancelEnrollment(
                    enrollmentId, 
                    request.getReason(), 
                    headers.getInstitutionId())
                    .map(enrollment -> ApiResponse.success(
                        Map.of("enrollment", enrollment), 
                        "Enrollment cancelled successfully"
                    ));
        });
    }

    // ENDPOINTS PARA PROFESORES
    @GetMapping("/teacher/by-student/{studentId}")
    public Mono<ApiResponse<List<StudentEnrollmentResponse>>> getEnrollmentsByStudentForTeacher(
            @PathVariable String studentId,
            ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("TEACHER")
            );
            
            return enrollmentService.getEnrollmentsByStudent(studentId, headers.getInstitutionId())
                    .collectList()
                    .map(enrollments -> ApiResponse.success(
                        enrollments, 
                        "Student enrollments retrieved successfully"
                    ));
        });
    }

    // ENDPOINTS PARA AUXILIARES  
    @GetMapping("/auxiliary/by-student/{studentId}")
    public Mono<ApiResponse<List<StudentEnrollmentResponse>>> getEnrollmentsByStudentForAuxiliary(
            @PathVariable String studentId,
            ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("AUXILIARY")
            );
            
            return enrollmentService.getEnrollmentsByStudent(studentId, headers.getInstitutionId())
                    .collectList()
                    .map(enrollments -> ApiResponse.success(
                        enrollments, 
                        "Student enrollments retrieved successfully"
                    ));
        });
    }
}