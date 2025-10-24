package pe.edu.vallegrande.msvstudents.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import pe.edu.vallegrande.msvstudents.application.service.StudentService;
import pe.edu.vallegrande.msvstudents.application.service.StudentEnrollmentService;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.ApiResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.security.HeaderValidator;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final StudentService studentService;
    private final StudentEnrollmentService enrollmentService;

    @GetMapping("/secretary/dashboard")
    public Mono<ApiResponse<Map<String, Object>>> getDashboardStats(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return Mono.zip(
                studentService.getStudentStatistics(headers.getInstitutionId()),
                enrollmentService.getEnrollmentStatistics(headers.getInstitutionId())
            ).map(tuple -> {
                Map<String, Object> dashboard = new HashMap<>();
                dashboard.put("studentStats", tuple.getT1());
                dashboard.put("enrollmentStats", tuple.getT2());
                return ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully");
            });
        });
    }

    @GetMapping("/secretary/overview")
    public Mono<ApiResponse<Map<String, Object>>> getInstitutionOverview(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("SECRETARY")
            );
            
            return Mono.zip(
                studentService.getStudentsByInstitution(headers.getInstitutionId()).count(),
                studentService.getUnenrolledStudents(headers.getInstitutionId()).count(),
                enrollmentService.getEnrollmentsByInstitution(headers.getInstitutionId()).count(),
                enrollmentService.getEnrollmentsByStatus("ACTIVE", headers.getInstitutionId()).count()
            ).map(tuple -> {
                Map<String, Object> overview = new HashMap<>();
                overview.put("totalStudents", tuple.getT1());
                overview.put("unenrolledStudents", tuple.getT2());
                overview.put("totalEnrollments", tuple.getT3());
                overview.put("activeEnrollments", tuple.getT4());
                
                // Calcular porcentajes
                long totalStudents = tuple.getT1();
                if (totalStudents > 0) {
                    overview.put("enrollmentPercentage", (tuple.getT3() * 100.0) / totalStudents);
                    overview.put("unenrolledPercentage", (tuple.getT2() * 100.0) / totalStudents);
                }
                
                return ApiResponse.success(overview, "Institution overview retrieved successfully");
            });
        });
    }

    @GetMapping("/teacher/my-stats")
    public Mono<ApiResponse<Map<String, Object>>> getTeacherStats(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HeaderValidator.HeaderValidationResult headers = HeaderValidator.validateHeadersSimple(
                exchange, Arrays.asList("TEACHER")
            );
            
            return Mono.zip(
                studentService.getStudentsByTeacher(headers.getUserId(), headers.getInstitutionId()).count(),
                enrollmentService.getEnrollmentsByTeacher(headers.getUserId(), headers.getInstitutionId()).count()
            ).map(tuple -> {
                Map<String, Object> stats = new HashMap<>();
                stats.put("myStudents", tuple.getT1());
                stats.put("myEnrollments", tuple.getT2());
                return ApiResponse.success(stats, "Teacher statistics retrieved successfully");
            });
        });
    }
}