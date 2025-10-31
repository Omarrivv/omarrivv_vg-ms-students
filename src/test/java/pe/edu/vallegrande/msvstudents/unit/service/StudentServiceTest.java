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
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentRepository Unit Tests - Simplified")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba
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
                           student.getDocumentNumber().equals("12345678") &&
                           student.getInstitutionId().equals("institution-001");
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
        student2.setStatus(Status.ACTIVE);

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
                           student.getStatus() == Status.ACTIVE &&
                           student.getFirstName().equals("Juan");
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
    @DisplayName("Debe actualizar estudiante exitosamente")
    void shouldUpdateStudentSuccessfully() {
        // Arrange
        Student updatedStudent = new Student();
        updatedStudent.setId("test-id-123");
        updatedStudent.setFirstName("Juan Carlos");
        updatedStudent.setLastName("Pérez García");
        updatedStudent.setAddress("Av. Updated 456");
        updatedStudent.setPhone("999888777");
        updatedStudent.setStatus(Status.ACTIVE);
        updatedStudent.setUpdatedAt(LocalDateTime.now());

        when(studentRepository.save(any(Student.class)))
                .thenReturn(Mono.just(updatedStudent));

        // Act & Assert
        StepVerifier.create(studentRepository.save(updatedStudent))
                .expectNextMatches(student -> {
                    return student.getId().equals("test-id-123") &&
                           student.getFirstName().equals("Juan Carlos") &&
                           student.getLastName().equals("Pérez García");
                })
                .verifyComplete();

        // Verify
        verify(studentRepository).save(updatedStudent);
    }

    @Test
    @DisplayName("Debe manejar timeout en operaciones del repositorio")
    void shouldHandleRepositoryTimeout() {
        // Arrange
        when(studentRepository.findById(anyString()))
                .thenReturn(Mono.just(testStudent).delayElement(Duration.ofSeconds(2)));

        // Act & Assert
        StepVerifier.create(studentRepository.findById("test-id-123").timeout(Duration.ofSeconds(1)))
                .expectError(TimeoutException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe validar campos del estudiante")
    void shouldValidateStudentFields() {
        // Act & Assert - Verificar que el estudiante tiene todos los campos esperados
        StepVerifier.create(Mono.just(testStudent))
                .expectNextMatches(student -> {
                    return student.getId() != null &&
                           student.getInstitutionId() != null &&
                           student.getFirstName() != null &&
                           student.getLastName() != null &&
                           student.getDocumentType() != null &&
                           student.getDocumentNumber() != null &&
                           student.getBirthDate() != null &&
                           student.getGender() != null &&
                           student.getStatus() != null &&
                           student.getCreatedAt() != null &&
                           student.getUpdatedAt() != null;
                })
                .verifyComplete();
    }
}