package pe.edu.vallegrande.msvstudents.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.msvstudents.application.service.StudentEnrollmentService;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentStatus;
import pe.edu.vallegrande.msvstudents.domain.model.StudentEnrollment;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentEnrollmentResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.EnrollmentWithStudentResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.InternalEnrollmentResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.ResourceNotFoundException;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.custom.InsufficientPermissionsException;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentEnrollmentRepository;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentRepository;
import pe.edu.vallegrande.msvstudents.infrastructure.util.StudentEnrollmentMapper;
import pe.edu.vallegrande.msvstudents.infrastructure.util.EnrollmentWithStudentMapper;
import pe.edu.vallegrande.msvstudents.infrastructure.util.InternalEnrollmentMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {

    private final StudentEnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

    @Override
    public Mono<StudentEnrollmentResponse> createEnrollment(CreateStudentEnrollmentRequest request, String institutionId) {
        return studentRepository.findById(request.getStudentId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student not found with id: " + request.getStudentId())))
                .flatMap(student -> {
                    if (!student.getInstitutionId().equals(institutionId)) {
                        return Mono.error(new InsufficientPermissionsException("SECRETARY", "enroll a student from another institution"));
                    }
                    StudentEnrollment enrollment = StudentEnrollmentMapper.toEntity(request);
                    // Denormalize institutionId into enrollment so we can query enrollments per institution
                    enrollment.setInstitutionId(institutionId);
                    return enrollmentRepository.save(enrollment);
                })
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Flux<StudentEnrollmentResponse> getEnrollmentsByInstitution(String institutionId) {
        return enrollmentRepository.findByInstitutionId(institutionId)
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Mono<StudentEnrollmentResponse> updateEnrollment(String enrollmentId, UpdateStudentEnrollmentRequest request, String institutionId) {
        return enrollmentRepository.findById(enrollmentId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId)))
                .flatMap(enrollment -> studentRepository.findById(enrollment.getStudentId())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Associated student not found with id: " + enrollment.getStudentId())))
                        .flatMap(student -> {
                            if (!student.getInstitutionId().equals(institutionId)) {
                                return Mono.error(new InsufficientPermissionsException("SECRETARY", "update an enrollment from another institution"));
                            }
                            StudentEnrollment updatedEnrollment = StudentEnrollmentMapper.updateEntity(enrollment, request);
                            return enrollmentRepository.save(updatedEnrollment);
                        }))
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Flux<StudentEnrollmentResponse> getEnrollmentsByTeacher(String teacherId, String institutionId) {
        // Requires call to another microservice to get teacher's classrooms.
        return Flux.empty();
    }

    @Override
    public Flux<StudentEnrollmentResponse> getEnrollmentsByClassroom(String classroomId, String institutionId) {
        // find by classroom and filter by institutionId
        return enrollmentRepository.findByClassroomIdIn(java.util.List.of(classroomId))
                .filter(enrollment -> enrollment.getInstitutionId() != null && enrollment.getInstitutionId().equals(institutionId))
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Mono<StudentEnrollmentResponse> updateEnrollmentObservations(String enrollmentId, String observations, String teacherId) {
        // Requires validation to ensure the teacher is assigned to this enrollment's classroom.
        return enrollmentRepository.findById(enrollmentId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId)))
                .flatMap(enrollment -> {
                    // The model doesn't have an 'observations' field. This is a conceptual implementation.
                    // In a real scenario, you would set the field and save.
                    return enrollmentRepository.save(enrollment);
                })
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Mono<Boolean> existsById(String enrollmentId) {
        return enrollmentRepository.existsById(enrollmentId);
    }

    @Override
    public Mono<Boolean> existsByQrCode(String qrCode) {
        return enrollmentRepository.existsByQrCode(qrCode);
    }

    @Override
    public Mono<StudentEnrollmentResponse> getEnrollmentById(String enrollmentId, String institutionId) {
        return enrollmentRepository.findById(enrollmentId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId)))
                .flatMap(enrollment -> {
                    if (enrollment.getInstitutionId() == null || !enrollment.getInstitutionId().equals(institutionId)) {
                        return Mono.error(new InsufficientPermissionsException("SECRETARY", "access enrollment from another institution"));
                    }
                    return Mono.just(enrollment);
                })
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Mono<Map<String, Object>> createBulkEnrollments(List<CreateStudentEnrollmentRequest> requests, String institutionId) {
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        
        return Flux.fromIterable(requests)
                .flatMap(request -> 
                    createEnrollment(request, institutionId)
                        .doOnNext(enrollment -> successCount.incrementAndGet())
                        .onErrorResume(error -> {
                            errorCount.incrementAndGet();
                            return Mono.empty();
                        })
                )
                .collectList()
                .map(enrollments -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("totalRequested", requests.size());
                    result.put("successfullyCreated", successCount.get());
                    result.put("errors", errorCount.get());
                    result.put("enrollments", enrollments);
                    return result;
                });
    }

    @Override
    public Flux<StudentEnrollmentResponse> getEnrollmentsByStatus(String status, String institutionId) {
        EnrollmentStatus enrollmentStatus = EnrollmentStatus.valueOf(status.toUpperCase());
        return enrollmentRepository.findByInstitutionId(institutionId)
                .filter(enrollment -> enrollment.getStatus() == enrollmentStatus)
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Mono<Map<String, Object>> getEnrollmentStatistics(String institutionId) {
        return enrollmentRepository.findByInstitutionId(institutionId)
                .collectList()
                .map(enrollments -> {
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("totalEnrollments", enrollments.size());
                    
                    long activeCount = enrollments.stream()
                        .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
                        .count();
                    stats.put("activeEnrollments", activeCount);
                    
                    long retiredCount = enrollments.stream()
                        .filter(e -> e.getStatus() == EnrollmentStatus.RETIRED)
                        .count();
                    stats.put("retiredEnrollments", retiredCount);
                    
                    long transferredCount = enrollments.stream()
                        .filter(e -> e.getStatus() == EnrollmentStatus.TRANSFER)
                        .count();
                    stats.put("transferredEnrollments", transferredCount);
                    
                    long completedCount = enrollments.stream()
                        .filter(e -> e.getStatus() == EnrollmentStatus.COMPLETED)
                        .count();
                    stats.put("completedEnrollments", completedCount);
                    
                    return stats;
                });
    }

    @Override
    public Mono<StudentEnrollmentResponse> transferStudent(String enrollmentId, String newClassroomId, String reason, String institutionId) {
        return enrollmentRepository.findById(enrollmentId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId)))
                .flatMap(enrollment -> {
                    if (!enrollment.getInstitutionId().equals(institutionId)) {
                        return Mono.error(new InsufficientPermissionsException("SECRETARY", "transfer an enrollment from another institution"));
                    }
                    enrollment.setClassroomId(newClassroomId);
                    enrollment.setStatus(EnrollmentStatus.TRANSFER);
                    enrollment.setTransferReason(reason);
                    return enrollmentRepository.save(enrollment);
                })
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Flux<StudentEnrollmentResponse> getEnrollmentsByStudent(String studentId, String institutionId) {
        return enrollmentRepository.findByStudentIdAndInstitutionId(studentId, institutionId)
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Mono<StudentEnrollmentResponse> cancelEnrollment(String enrollmentId, String reason, String institutionId) {
        return enrollmentRepository.findById(enrollmentId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId)))
                .flatMap(enrollment -> {
                    if (!enrollment.getInstitutionId().equals(institutionId)) {
                        return Mono.error(new InsufficientPermissionsException("SECRETARY", "cancel an enrollment from another institution"));
                    }
                    enrollment.setStatus(EnrollmentStatus.RETIRED);
                    enrollment.setTransferReason(reason);
                    return enrollmentRepository.save(enrollment);
                })
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Flux<EnrollmentWithStudentResponse> getEnrollmentsByClassroomWithStudentInfo(String classroomId, String institutionId) {
        return enrollmentRepository.findByClassroomId(classroomId)
                .filter(enrollment -> enrollment.getInstitutionId().equals(institutionId))
                .flatMap(enrollment -> 
                    studentRepository.findById(enrollment.getStudentId())
                        .map(student -> EnrollmentWithStudentMapper.toResponse(enrollment, student))
                        .switchIfEmpty(Mono.empty()) // Si no encuentra el estudiante, omite esta matrícula
                );
    }

    @Override
    public Flux<InternalEnrollmentResponse> getInternalEnrollmentsByClassroom(String classroomId, String institutionId) {
        return enrollmentRepository.findByClassroomId(classroomId)
                // No filtrar por institución en endpoints internos
                .flatMap(enrollment -> 
                    studentRepository.findById(enrollment.getStudentId())
                        .map(student -> InternalEnrollmentMapper.toResponse(enrollment, student))
                        .switchIfEmpty(Mono.empty()) // Si no encuentra el estudiante, omite esta matrícula
                );
    }
}
