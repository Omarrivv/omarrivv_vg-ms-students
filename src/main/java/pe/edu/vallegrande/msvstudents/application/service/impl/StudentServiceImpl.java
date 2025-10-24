package pe.edu.vallegrande.msvstudents.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.msvstudents.application.service.StudentService;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentStatus;
import pe.edu.vallegrande.msvstudents.domain.model.Student;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.ResourceNotFoundException;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.custom.InsufficientPermissionsException;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentRepository;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentEnrollmentRepository;
import pe.edu.vallegrande.msvstudents.infrastructure.util.StudentMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;

    @Override
    public Mono<StudentResponse> createStudent(CreateStudentRequest request, String institutionId) {
        // Check if a student with the same document number already exists in the institution
        return studentRepository.findByDocumentNumberAndInstitutionId(request.getDocumentNumber(), institutionId)
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("Student with document number " + request.getDocumentNumber() + " already exists in this institution."));
                    }
                    // If not, create and save the new student
                    Student student = StudentMapper.toEntity(request, institutionId);
                    return studentRepository.save(student).map(StudentMapper::toResponse);
                });
    }

    @Override
    public Mono<StudentResponse> findById(String id) {
        return studentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student not found with id: " + id)))
                .map(StudentMapper::toResponse);
    }

    @Override
    public Flux<StudentResponse> findAll() {
        return studentRepository.findAll()
                .map(StudentMapper::toResponse);
    }

    @Override
    public Flux<StudentResponse> getStudentsByInstitution(String institutionId) {
        return studentRepository.findByInstitutionId(institutionId)
                .map(StudentMapper::toResponse);
    }

    @Override
    public Mono<StudentResponse> updateStudent(String studentId, UpdateStudentRequest request, String institutionId) {
        return studentRepository.findById(studentId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student not found with id: " + studentId)))
                .flatMap(student -> {
                    // Verify that the student belongs to the institution of the user making the request
                    if (!student.getInstitutionId().equals(institutionId)) {
                        return Mono.error(new InsufficientPermissionsException("SECRETARY", "update a student from another institution"));
                    }
                    Student updatedStudent = StudentMapper.updateEntity(student, request);
                    return studentRepository.save(updatedStudent);
                })
                .map(StudentMapper::toResponse);
    }

    @Override
    public Flux<StudentResponse> getStudentsByTeacher(String teacherId, String institutionId) {
        // This method requires a call to another microservice (e.g., academic-management)
        // to get the classrooms assigned to the teacher.
        // 1. Make WebClient call to get Flux<String> classroomIds for teacherId.
        // 2. Use classroomIds to find enrollments.
        // 3. Get studentIds from enrollments.
        // 4. Find students by studentIds.
        // For now, returning empty.
        return Flux.empty();
    }

    @Override
    public Mono<Boolean> existsById(String studentId) {
        return studentRepository.existsById(studentId);
    }

    @Override
    public Flux<StudentResponse> getUnenrolledStudents(String institutionId) {
        return studentRepository.findByInstitutionId(institutionId)
                .filterWhen(student -> 
                    studentEnrollmentRepository.findByStudentIdAndInstitutionId(student.getId(), institutionId)
                        .filter(enrollment -> enrollment.getStatus().equals(EnrollmentStatus.ACTIVE))
                        .hasElements()
                        .map(hasActiveEnrollments -> !hasActiveEnrollments)
                )
                .map(StudentMapper::toResponse);
    }

    @Override
    public Mono<Map<String, Object>> createStudentsBulk(List<CreateStudentRequest> requests, String institutionId) {
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        
        // Limitar a 1000 estudiantes
        if (requests.size() > 1000) {
            return Mono.error(new IllegalArgumentException("Cannot process more than 1000 students at once"));
        }
        
        return Flux.fromIterable(requests)
                .flatMap(request -> 
                    createStudent(request, institutionId)
                        .doOnNext(student -> successCount.incrementAndGet())
                        .onErrorResume(error -> {
                            errorCount.incrementAndGet();
                            return Mono.empty();
                        })
                )
                .collectList()
                .map(students -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("totalRequested", requests.size());
                    result.put("successfullyCreated", successCount.get());
                    result.put("errors", errorCount.get());
                    result.put("students", students);
                    return result;
                });
    }

    @Override
    public Flux<StudentResponse> searchStudents(String query, String institutionId) {
        return studentRepository.findByInstitutionId(institutionId)
                .filter(student -> 
                    student.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                    student.getLastName().toLowerCase().contains(query.toLowerCase()) ||
                    student.getDocumentNumber().contains(query) ||
                    (student.getParentEmail() != null && student.getParentEmail().toLowerCase().contains(query.toLowerCase()))
                )
                .map(StudentMapper::toResponse);
    }

    @Override
    public Flux<StudentResponse> getStudentsByGrade(String grade, String institutionId) {
        // Como el modelo StudentEnrollment no tiene campo grade, 
        // este método necesitaría ser implementado cuando se agregue ese campo
        // Por ahora retornamos estudiantes de la institución como placeholder
        return studentRepository.findByInstitutionId(institutionId)
                .map(StudentMapper::toResponse);
    }

    @Override
    public Flux<StudentResponse> getStudentsByStatus(String status, String institutionId) {
        Status statusEnum = Status.valueOf(status.toUpperCase());
        return studentRepository.findByInstitutionIdAndStatus(institutionId, statusEnum)
                .map(StudentMapper::toResponse);
    }

    @Override
    public Mono<Map<String, Object>> getStudentStatistics(String institutionId) {
        return Mono.zip(
            studentRepository.countByInstitutionId(institutionId),
            studentRepository.countByInstitutionIdAndStatus(institutionId, Status.ACTIVE),
            studentRepository.countByInstitutionIdAndStatus(institutionId, Status.INACTIVE),
            getUnenrolledStudents(institutionId).count()
        ).map(tuple -> {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalStudents", tuple.getT1());
            stats.put("activeStudents", tuple.getT2());
            stats.put("inactiveStudents", tuple.getT3());
            stats.put("unenrolledStudents", tuple.getT4());
            return stats;
        });
    }

    @Override
    public Mono<StudentResponse> deactivateStudent(String studentId, String institutionId) {
        return studentRepository.findById(studentId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student not found with id: " + studentId)))
                .flatMap(student -> {
                    if (!student.getInstitutionId().equals(institutionId)) {
                        return Mono.error(new InsufficientPermissionsException("SECRETARY", "deactivate a student from another institution"));
                    }
                    student.setStatus(Status.INACTIVE);
                    return studentRepository.save(student);
                })
                .map(StudentMapper::toResponse);
    }

    @Override
    public Mono<StudentResponse> activateStudent(String studentId, String institutionId) {
        return studentRepository.findById(studentId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student not found with id: " + studentId)))
                .flatMap(student -> {
                    if (!student.getInstitutionId().equals(institutionId)) {
                        return Mono.error(new InsufficientPermissionsException("SECRETARY", "activate a student from another institution"));
                    }
                    student.setStatus(Status.ACTIVE);
                    return studentRepository.save(student);
                })
                .map(StudentMapper::toResponse);
    }
}
