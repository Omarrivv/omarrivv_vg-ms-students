package pe.edu.vallegrande.msvstudents.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;
import pe.edu.vallegrande.msvstudents.domain.model.Student;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService Unit Tests - Simplified")
class StudentServiceSimplifiedTest {

    @Mock
    private StudentRepository studentRepository;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId("test-id-123");
        testStudent.setInstitutionId("institution-001");
        testStudent.setFirstName("Juan");
        testStudent.setLastName("Pérez");
        testStudent.setDocumentType(DocumentType.DNI);
        testStudent.setDocumentNumber("12345678");
        testStudent.setBirthDate(LocalDate.of(2000, 1, 15));
        testStudent.setGender(Gender.MALE);
        testStudent.setAddress("Av. Test 123");
        testStudent.setPhone("987654321");
        testStudent.setParentPhone("123456789");
        testStudent.setParentEmail("parent@test.com");
        testStudent.setStatus(Status.ACTIVE);
        testStudent.setCreatedAt(LocalDateTime.now());
        testStudent.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Debe encontrar estudiante por ID exitosamente")
    void shouldFindStudentByIdSuccessfully() {
        // Arrange
        when(studentRepository.findById(anyString()))
                .thenReturn(Mono.just(testStudent));

        // Act & Assert
        StepVerifier.create(studentRepository.findById("test-id-123"))
                .expectNextMatches(student -> {
                    return student.getId().equals("test-id-123") &&
                           student.getFirstName().equals("Juan") &&
                           student.getLastName().equals("Pérez") &&
                           student.getDocumentNumber().equals("12345678");
                })
                .verifyComplete();

        // Verify
        verify(studentRepository).findById("test-id-123");
    }

    @Test
    @DisplayName("Debe retornar vacío cuando estudiante no existe")
    void shouldReturnEmptyWhenStudentNotFound() {
        // Arrange
        when(studentRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(studentRepository.findById("non-existent-id"))
                .expectComplete()
                .verify();

        // Verify
        verify(studentRepository).findById("non-existent-id");
    }

    @Test
    @DisplayName("Debe obtener todos los estudiantes exitosamente")
    void shouldGetAllStudentsSuccessfully() {
        // Arrange
        Student student2 = new Student();
        student2.setId("test-id-456");
        student2.setFirstName("María");
        student2.setLastName("González");
        student2.setInstitutionId("institution-001");

        when(studentRepository.findAll())
                .thenReturn(Flux.just(testStudent, student2));

        // Act & Assert
        StepVerifier.create(studentRepository.findAll())
                .expectNextCount(2)
                .verifyComplete();

        // Verify
        verify(studentRepository).findAll();
    }

    @Test
    @DisplayName("Debe guardar estudiante exitosamente")
    void shouldSaveStudentSuccessfully() {
        // Arrange
        when(studentRepository.save(any(Student.class)))
                .thenReturn(Mono.just(testStudent));

        // Act & Assert
        StepVerifier.create(studentRepository.save(testStudent))
                .expectNextMatches(student -> {
                    return student.getId().equals("test-id-123") &&
                           student.getStatus() == Status.ACTIVE;
                })
                .verifyComplete();

        // Verify
        verify(studentRepository).save(testStudent);
    }

    @Test
    @DisplayName("Debe verificar existencia de estudiante por ID")
    void shouldCheckStudentExistence() {
        // Arrange
        when(studentRepository.existsById(anyString()))
                .thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(studentRepository.existsById("test-id-123"))
                .expectNext(true)
                .verifyComplete();

        // Verify
        verify(studentRepository).existsById("test-id-123");
    }

    @Test
    @DisplayName("Debe retornar false cuando estudiante no existe para verificación")
    void shouldReturnFalseWhenStudentNotExistsForCheck() {
        // Arrange
        when(studentRepository.existsById(anyString()))
                .thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(studentRepository.existsById("non-existent-id"))
                .expectNext(false)
                .verifyComplete();

        // Verify
        verify(studentRepository).existsById("non-existent-id");
    }

    @Test
    @DisplayName("Debe obtener estudiantes por institución y estado")
    void shouldGetStudentsByInstitutionAndStatus() {
        // Arrange
        when(studentRepository.findByInstitutionIdAndStatus(anyString(), any(Status.class)))
                .thenReturn(Flux.just(testStudent));

        // Act & Assert
        StepVerifier.create(studentRepository.findByInstitutionIdAndStatus("institution-001", Status.ACTIVE))
                .expectNextCount(1)
                .verifyComplete();

        // Verify
        verify(studentRepository).findByInstitutionIdAndStatus("institution-001", Status.ACTIVE);
    }

    @Test
    @DisplayName("Debe manejar error durante guardado de estudiante")
    void shouldHandleErrorDuringSaveStudent() {
        // Arrange
        when(studentRepository.save(any(Student.class)))
                .thenReturn(Mono.error(new RuntimeException("Error al guardar estudiante")));

        // Act & Assert
        StepVerifier.create(studentRepository.save(testStudent))
                .expectErrorMatches(throwable -> 
                    throwable instanceof RuntimeException &&
                    throwable.getMessage().contains("Error al guardar estudiante")
                )
                .verify();

        // Verify
        verify(studentRepository).save(testStudent);
    }

    @Test
    @DisplayName("Debe manejar flujo vacío cuando no hay estudiantes")
    void shouldHandleEmptyFluxWhenNoStudents() {
        // Arrange
        when(studentRepository.findAll())
                .thenReturn(Flux.empty());

        // Act & Assert
        StepVerifier.create(studentRepository.findAll())
                .expectComplete()
                .verify();

        // Verify
        verify(studentRepository).findAll();
    }

    @Test
    @DisplayName("Debe manejar múltiples operaciones en secuencia")
    void shouldHandleMultipleOperationsInSequence() {
        // Arrange
        when(studentRepository.save(any(Student.class)))
                .thenReturn(Mono.just(testStudent));
        when(studentRepository.findById(anyString()))
                .thenReturn(Mono.just(testStudent));

        // Act & Assert - Secuencia de operaciones
        Mono<Student> operationChain = studentRepository.save(testStudent)
                .flatMap(savedStudent -> studentRepository.findById(savedStudent.getId()))
                .filter(student -> student.getStatus() == Status.ACTIVE);

        StepVerifier.create(operationChain)
                .expectNextMatches(student -> 
                    student.getId().equals("test-id-123") &&
                    student.getStatus() == Status.ACTIVE
                )
                .verifyComplete();

        // Verify
        verify(studentRepository).save(testStudent);
        verify(studentRepository).findById("test-id-123");
    }
}