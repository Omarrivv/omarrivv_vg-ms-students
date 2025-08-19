package pe.edu.vallegrande.msvstudents.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.msvstudents.application.service.ClassroomStudentService;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.ClassroomStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.ClassroomStudentResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.util.CsvUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/classroom-students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClassroomStudentController {

    private final ClassroomStudentService classroomStudentService;

    @GetMapping
    public Flux<ClassroomStudentResponse> findAll() {
        return classroomStudentService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ClassroomStudentResponse> findById(@PathVariable String id) {
        return classroomStudentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ClassroomStudentResponse> save(@RequestBody ClassroomStudentRequest request) {
        return classroomStudentService.save(request);
    }

    @PutMapping("/{id}")
    public Mono<ClassroomStudentResponse> update(@PathVariable String id, @RequestBody ClassroomStudentRequest request) {
        return classroomStudentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return classroomStudentService.delete(id);
    }

    @GetMapping("/student/{studentId}")
    public Flux<ClassroomStudentResponse> findByStudentId(@PathVariable String studentId) {
        return classroomStudentService.findByStudentId(studentId);
    }

    @GetMapping("/classroom/{classroomId}")
    public Flux<ClassroomStudentResponse> findByClassroomId(@PathVariable String classroomId) {
        return classroomStudentService.findByClassroomId(classroomId);
    }

    @GetMapping("/status/{status}")
    public Flux<ClassroomStudentResponse> findByStatus(@PathVariable String status) {
        return classroomStudentService.findByStatus(status);
    }

    @GetMapping("/year/{year}")
    public Flux<ClassroomStudentResponse> findByYear(@PathVariable String year) {
        return classroomStudentService.findByYear(year);
    }

    @GetMapping("/period/{period}")
    public Flux<ClassroomStudentResponse> findByPeriod(@PathVariable String period) {
        return classroomStudentService.findByPeriod(period);
    }

    @GetMapping("/year/{year}/period/{period}")
    public Flux<ClassroomStudentResponse> findByYearAndPeriod(
            @PathVariable String year,
            @PathVariable String period) {
        return classroomStudentService.findByYearAndPeriod(year, period);
    }

    @PutMapping("/{id}/restore")
    public Mono<ClassroomStudentResponse> restore(@PathVariable String id) {
        return classroomStudentService.restore(id);
    }

    @GetMapping(value = "/export", produces = "text/csv")
    public Mono<ResponseEntity<Flux<String>>> exportCsv() {
        String header = CsvUtils.joinCsv(
                "id", "classroomId", "studentId", "enrollmentDate",
                "enrollmentYear", "enrollmentPeriod", "status"
        );

        Flux<String> lines = classroomStudentService.findAll()
                .map(e -> CsvUtils.joinCsv(
                        e.getId(),
                        e.getClassroomId(),
                        e.getStudentId(),
                        e.getEnrollmentDate(),
                        e.getEnrollmentYear(),
                        e.getEnrollmentPeriod(),
                        e.getStatus()
                ) + "\n");

        Flux<String> body = Flux.concat(Flux.just(header + "\n"), lines);

        return Mono.just(ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=classroom-students.csv")
                .contentType(MediaType.valueOf("text/csv"))
                .body(body));
    }
} 