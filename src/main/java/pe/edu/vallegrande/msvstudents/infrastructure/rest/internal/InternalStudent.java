package pe.edu.vallegrande.msvstudents.infrastructure.rest.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.msvstudents.application.service.StudentEnrollmentService;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.ApiResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.InternalEnrollmentResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/internal")
@RequiredArgsConstructor
public class InternalStudent {

    private final StudentEnrollmentService enrollmentService;

    @GetMapping("/by-classroom/{classroomId}")
    public Mono<ApiResponse<List<InternalEnrollmentResponse>>> getEnrollmentsByClassroomInternal(
            @PathVariable String classroomId) {
        return enrollmentService.getInternalEnrollmentsByClassroom(classroomId, null)
                .collectList()
                .map(list -> ApiResponse.success(list, "Internal enrollments by classroom retrieved successfully"));
    }
}
