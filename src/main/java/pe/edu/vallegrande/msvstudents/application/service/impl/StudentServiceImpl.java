package pe.edu.vallegrande.msvstudents.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.msvstudents.application.service.StudentService;
import pe.edu.vallegrande.msvstudents.domain.model.Student;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.ResourceNotFoundException;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.custom.InsufficientPermissionsException;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentRepository;
import pe.edu.vallegrande.msvstudents.infrastructure.util.StudentMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

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
}
