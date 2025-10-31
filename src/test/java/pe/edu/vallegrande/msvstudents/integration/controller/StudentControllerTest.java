package pe.edu.vallegrande.msvstudents.integration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.edu.vallegrande.msvstudents.application.service.StudentService;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@DisplayName("StudentController Integration Tests")
class StudentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private StudentService studentService;

    private StudentResponse testStudentResponse;
    private CreateStudentRequest createRequest;
    private UpdateStudentRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba
        testStudentResponse = StudentResponse.builder()
                .id("test-id-123")
                .institutionId("institution-001")
                .firstName("Juan")
                .lastName("Pérez")
                .documentType(DocumentType.DNI)
                .documentNumber("12345678")
                .birthDate(LocalDate.of(2000, 1, 15))
                .gender(Gender.MALE)
                .address("Av. Test 123")
                .phone("987654321")
                .parentPhone("123456789")
                .parentEmail("parent@test.com")
                .status(Status.ACTIVE)
                .build();

        createRequest = new CreateStudentRequest();
        createRequest.setFirstName("Juan");
        createRequest.setLastName("Pérez");
        createRequest.setDocumentType(DocumentType.DNI);
        createRequest.setDocumentNumber("12345678");
        createRequest.setBirthDate(LocalDate.of(2000, 1, 15));
        createRequest.setGender(Gender.MALE);
        createRequest.setAddress("Av. Test 123");
        createRequest.setPhone("987654321");
        createRequest.setParentPhone("123456789");
        createRequest.setParentEmail("parent@test.com");

        updateRequest = new UpdateStudentRequest();
        updateRequest.setFirstName("Juan Carlos");
        updateRequest.setLastName("Pérez García");
        updateRequest.setAddress("Av. Updated 456");
        updateRequest.setPhone("999888777");
    }

    @Test
    @DisplayName("POST /api/students - Debe crear un estudiante exitosamente")
    void shouldCreateStudentSuccessfully() {
        // Arrange
        when(studentService.createStudent(any(CreateStudentRequest.class), anyString()))
                .thenReturn(Mono.just(testStudentResponse));

        // Act & Assert
        webTestClient.post()
                .uri("/api/students")
                .header("Institution-Id", "institution-001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.id").isEqualTo("test-id-123")
                .jsonPath("$.data.firstName").isEqualTo("Juan")
                .jsonPath("$.data.lastName").isEqualTo("Pérez")
                .jsonPath("$.data.documentNumber").isEqualTo("12345678")
                .jsonPath("$.data.institutionId").isEqualTo("institution-001");
    }

    @Test
    @DisplayName("POST /api/students - Debe fallar sin Institution-Id header")
    void shouldFailWithoutInstitutionIdHeader() {
        // Act & Assert
        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("POST /api/students - Debe fallar con datos inválidos")
    void shouldFailWithInvalidData() {
        // Arrange - Request con datos inválidos
        CreateStudentRequest invalidRequest = new CreateStudentRequest();
        invalidRequest.setFirstName(""); // Nombre vacío
        invalidRequest.setDocumentNumber("123"); // Documento muy corto

        // Act & Assert
        webTestClient.post()
                .uri("/api/students")
                .header("Institution-Id", "institution-001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("GET /api/students/{id} - Debe obtener estudiante por ID exitosamente")
    void shouldGetStudentByIdSuccessfully() {
        // Arrange
        when(studentService.findById(anyString()))
                .thenReturn(Mono.just(testStudentResponse));

        // Act & Assert
        webTestClient.get()
                .uri("/api/students/test-id-123")
                .header("Institution-Id", "institution-001")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.id").isEqualTo("test-id-123")
                .jsonPath("$.data.firstName").isEqualTo("Juan")
                .jsonPath("$.data.lastName").isEqualTo("Pérez");
    }

    @Test
    @DisplayName("GET /api/students/{id} - Debe retornar 404 cuando estudiante no existe")
    void shouldReturn404WhenStudentNotFound() {
        // Arrange
        when(studentService.findById(anyString()))
                .thenReturn(Mono.error(new RuntimeException("Estudiante no encontrado")));

        // Act & Assert
        webTestClient.get()
                .uri("/api/students/non-existent-id")
                .header("Institution-Id", "institution-001")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.error").exists();
    }

    @Test
    @DisplayName("GET /api/students - Debe obtener todos los estudiantes exitosamente")
    void shouldGetAllStudentsSuccessfully() {
        // Arrange
        StudentResponse student2 = StudentResponse.builder()
                .id("test-id-456")
                .firstName("María")
                .lastName("González")
                .institutionId("institution-001")
                .build();

        when(studentService.getStudentsByInstitution(anyString()))
                .thenReturn(Flux.just(testStudentResponse, student2));

        // Act & Assert
        webTestClient.get()
                .uri("/api/students")
                .header("Institution-Id", "institution-001")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data").isArray()
                .jsonPath("$.data.length()").isEqualTo(2)
                .jsonPath("$.data[0].id").isEqualTo("test-id-123")
                .jsonPath("$.data[1].id").isEqualTo("test-id-456");
    }

    @Test
    @DisplayName("GET /api/students - Debe retornar lista vacía cuando no hay estudiantes")
    void shouldReturnEmptyListWhenNoStudents() {
        // Arrange
        when(studentService.getStudentsByInstitution(anyString()))
                .thenReturn(Flux.empty());

        // Act & Assert
        webTestClient.get()
                .uri("/api/students")
                .header("Institution-Id", "institution-001")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data").isArray()
                .jsonPath("$.data.length()").isEqualTo(0);
    }

    @Test
    @DisplayName("PUT /api/students/{id} - Debe actualizar estudiante exitosamente")
    void shouldUpdateStudentSuccessfully() {
        // Arrange
        StudentResponse updatedResponse = StudentResponse.builder()
                .id("test-id-123")
                .firstName("Juan Carlos")
                .lastName("Pérez García")
                .address("Av. Updated 456")
                .build();

        when(studentService.updateStudent(anyString(), any(UpdateStudentRequest.class), anyString()))
                .thenReturn(Mono.just(updatedResponse));

        // Act & Assert
        webTestClient.put()
                .uri("/api/students/test-id-123")
                .header("Institution-Id", "institution-001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.id").isEqualTo("test-id-123")
                .jsonPath("$.data.firstName").isEqualTo("Juan Carlos")
                .jsonPath("$.data.lastName").isEqualTo("Pérez García");
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - Debe desactivar estudiante exitosamente")
    void shouldDeactivateStudentSuccessfully() {
        // Arrange
        StudentResponse deactivatedResponse = StudentResponse.builder()
                .id("test-id-123")
                .status(Status.INACTIVE)
                .build();

        when(studentService.deactivateStudent(anyString(), anyString()))
                .thenReturn(Mono.just(deactivatedResponse));

        // Act & Assert
        webTestClient.delete()
                .uri("/api/students/test-id-123")
                .header("Institution-Id", "institution-001")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.id").isEqualTo("test-id-123")
                .jsonPath("$.data.status").isEqualTo("INACTIVE");
    }

    @Test
    @DisplayName("GET /api/students/search - Debe buscar estudiantes por query")
    void shouldSearchStudentsSuccessfully() {
        // Arrange
        when(studentService.searchStudents(anyString(), anyString()))
                .thenReturn(Flux.just(testStudentResponse));

        // Act & Assert
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/students/search")
                        .queryParam("q", "Juan")
                        .build())
                .header("Institution-Id", "institution-001")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data").isArray()
                .jsonPath("$.data[0].firstName").isEqualTo("Juan");
    }

    @Test
    @DisplayName("GET /api/students/statistics - Debe obtener estadísticas de estudiantes")
    void shouldGetStudentStatisticsSuccessfully() {
        // Arrange
        Map<String, Object> statistics = Map.of(
                "totalStudents", 100,
                "activeStudents", 95,
                "inactiveStudents", 5,
                "byGender", Map.of("MALE", 50, "FEMALE", 50),
                "averageAge", 15.5
        );

        when(studentService.getStudentStatistics(anyString()))
                .thenReturn(Mono.just(statistics));

        // Act & Assert
        webTestClient.get()
                .uri("/api/students/statistics")
                .header("Institution-Id", "institution-001")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.totalStudents").isEqualTo(100)
                .jsonPath("$.data.activeStudents").isEqualTo(95)
                .jsonPath("$.data.averageAge").isEqualTo(15.5);
    }

    @Test
    @DisplayName("Debe validar header Institution-Id en todos los endpoints")
    void shouldValidateInstitutionIdHeaderInAllEndpoints() {
        // Test GET /api/students sin header
        webTestClient.get()
                .uri("/api/students")
                .exchange()
                .expectStatus().isBadRequest();

        // Test GET /api/students/{id} sin header
        webTestClient.get()
                .uri("/api/students/test-id-123")
                .exchange()
                .expectStatus().isBadRequest();

        // Test PUT /api/students/{id} sin header
        webTestClient.put()
                .uri("/api/students/test-id-123")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isBadRequest();

        // Test DELETE /api/students/{id} sin header
        webTestClient.delete()
                .uri("/api/students/test-id-123")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe manejar errores de validación correctamente")
    void shouldHandleValidationErrorsCorrectly() {
        // Arrange - Request con múltiples campos inválidos
        CreateStudentRequest invalidRequest = new CreateStudentRequest();
        invalidRequest.setFirstName(""); // Vacío
        invalidRequest.setLastName("A"); // Muy corto
        invalidRequest.setDocumentNumber("123"); // Formato inválido
        invalidRequest.setParentEmail("invalid-email"); // Email inválido

        // Act & Assert
        webTestClient.post()
                .uri("/api/students")
                .header("Institution-Id", "institution-001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.error").exists();
    }

    @Test
    @DisplayName("Debe manejar errores internos del servidor correctamente")
    void shouldHandleInternalServerErrorsCorrectly() {
        // Arrange
        when(studentService.createStudent(any(CreateStudentRequest.class), anyString()))
                .thenReturn(Mono.error(new RuntimeException("Error interno del servidor")));

        // Act & Assert
        webTestClient.post()
                .uri("/api/students")
                .header("Institution-Id", "institution-001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createRequest)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.error").exists();
    }
}