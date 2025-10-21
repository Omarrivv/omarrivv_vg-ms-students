package pe.edu.vallegrande.msvstudents.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.msvstudents.application.service.StudentEnrollmentService;
import pe.edu.vallegrande.msvstudents.domain.model.StudentEnrollment;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentEnrollmentResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.ResourceNotFoundException;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.custom.InsufficientPermissionsException;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentEnrollmentRepository;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentRepository;
import pe.edu.vallegrande.msvstudents.infrastructure.util.StudentEnrollmentMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
}
