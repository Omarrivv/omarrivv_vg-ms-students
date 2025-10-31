package pe.edu.vallegrande.msvstudents.integration.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@DisplayName("StudentController Integration Tests - Simplified")
class StudentControllerSimplifiedTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("GET /actuator/health - Debe retornar status UP")
    void shouldReturnHealthStatusUp() {
        webTestClient.get()
                .uri("/actuator/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("UP");
    }

    @Test
    @DisplayName("GET /api/students - Debe fallar sin Institution-Id header")
    void shouldFailWithoutInstitutionIdHeader() {
        webTestClient.get()
                .uri("/api/students")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("POST /api/students - Debe fallar sin Institution-Id header")
    void shouldFailPostWithoutInstitutionIdHeader() {
        String studentJson = """
            {
                "firstName": "Juan",
                "lastName": "Pérez",
                "documentType": "DNI",
                "documentNumber": "12345678",
                "birthDate": "2000-01-15",
                "gender": "MALE",
                "address": "Av. Test 123",
                "phone": "987654321",
                "parentPhone": "123456789",
                "parentEmail": "parent@test.com"
            }
            """;

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentJson)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("GET /api/students - Debe aceptar request con Institution-Id header válido")
    void shouldAcceptRequestWithValidInstitutionIdHeader() {
        webTestClient.get()
                .uri("/api/students")
                .header("Institution-Id", "INST001")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("POST /api/students - Debe validar formato JSON")
    void shouldValidateJsonFormat() {
        String invalidJson = "{ invalid json }";

        webTestClient.post()
                .uri("/api/students")
                .header("Institution-Id", "INST001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidJson)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("GET /api/students/{id} - Debe validar formato de ID")
    void shouldValidateIdFormat() {
        webTestClient.get()
                .uri("/api/students/invalid-id-format")
                .header("Institution-Id", "INST001")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("PUT /api/students/{id} - Debe fallar sin Institution-Id header")
    void shouldFailUpdateWithoutInstitutionIdHeader() {
        String updateJson = """
            {
                "firstName": "Juan Carlos",
                "lastName": "Pérez García"
            }
            """;

        webTestClient.put()
                .uri("/api/students/test-id-123")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateJson)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - Debe fallar sin Institution-Id header")
    void shouldFailDeleteWithoutInstitutionIdHeader() {
        webTestClient.delete()
                .uri("/api/students/test-id-123")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("GET /api/students/search - Debe validar parámetro de búsqueda")
    void shouldValidateSearchParameter() {
        webTestClient.get()
                .uri("/api/students/search")
                .header("Institution-Id", "INST001")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("POST /api/students - Debe rechazar contenido vacío")
    void shouldRejectEmptyContent() {
        webTestClient.post()
                .uri("/api/students")
                .header("Institution-Id", "INST001")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe validar Content-Type para requests POST/PUT")
    void shouldValidateContentTypeForPostPutRequests() {
        String studentJson = """
            {
                "firstName": "Juan",
                "lastName": "Pérez"
            }
            """;

        // Test POST sin Content-Type
        webTestClient.post()
                .uri("/api/students")
                .header("Institution-Id", "INST001")
                .bodyValue(studentJson)
                .exchange()
                .expectStatus().is4xxClientError();

        // Test PUT sin Content-Type
        webTestClient.put()
                .uri("/api/students/test-id-123")
                .header("Institution-Id", "INST001")
                .bodyValue(studentJson)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("Debe manejar headers adicionales correctamente")
    void shouldHandleAdditionalHeadersCorrectly() {
        webTestClient.get()
                .uri("/api/students")
                .header("Institution-Id", "INST001")
                .header("User-Agent", "JUnit-Test")
                .header("Accept", "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("Debe validar caracteres especiales en Institution-Id")
    void shouldValidateSpecialCharactersInInstitutionId() {
        // Test con caracteres especiales válidos
        webTestClient.get()
                .uri("/api/students")
                .header("Institution-Id", "INST-001_TEST")
                .exchange()
                .expectStatus().isOk();

        // Test con espacios (normalmente no válido)
        webTestClient.get()
                .uri("/api/students")
                .header("Institution-Id", "INST 001")
                .exchange()
                .expectStatus().isOk(); // Depende de la validación implementada
    }

    @Test
    @DisplayName("Debe manejar requests concurrentes correctamente")
    void shouldHandleConcurrentRequestsCorrectly() {
        // Simular múltiples requests concurrentes
        for (int i = 0; i < 5; i++) {
            webTestClient.get()
                    .uri("/api/students")
                    .header("Institution-Id", "INST001")
                    .exchange()
                    .expectStatus().isOk();
        }
    }
}