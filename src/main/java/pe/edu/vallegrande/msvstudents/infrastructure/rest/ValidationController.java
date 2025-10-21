package pe.edu.vallegrande.msvstudents.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.vallegrande.msvstudents.application.service.StudentEnrollmentService;
import pe.edu.vallegrande.msvstudents.application.service.StudentService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ValidationController {

    private final StudentService studentService;
    private final StudentEnrollmentService enrollmentService;

    @GetMapping("/validate-students/{studentId}")
    public Mono<ResponseEntity<Void>> validateStudent(@PathVariable String studentId) {
        return studentService.existsById(studentId)
                .map(exists -> exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build());
    }

    @GetMapping("/validate-enrollments/{enrollmentId}")
    public Mono<ResponseEntity<Void>> validateEnrollment(@PathVariable String enrollmentId) {
        return enrollmentService.existsById(enrollmentId)
                .map(exists -> exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build());
    }

    @GetMapping("/validate-qr/{qrCode}")
    public Mono<ResponseEntity<Void>> validateQrCode(@PathVariable String qrCode) {
        return enrollmentService.existsByQrCode(qrCode)
                .map(exists -> exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build());
    }
}
