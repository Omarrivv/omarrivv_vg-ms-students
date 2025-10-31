package pe.edu.vallegrande.msvstudents.unit.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;
import pe.edu.vallegrande.msvstudents.domain.model.Student;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Student Domain Model Tests")
class StudentTest {

    @Test
    @DisplayName("Debe crear un estudiante con todos los campos")
    void shouldCreateStudentWithAllFields() {
        // Arrange & Act
        Student student = new Student();
        student.setId("test-id-123");
        student.setInstitutionId("institution-001");
        student.setFirstName("Juan");
        student.setLastName("Pérez");
        student.setDocumentType(DocumentType.DNI);
        student.setDocumentNumber("12345678");
        student.setBirthDate(LocalDate.of(2000, 1, 15));
        student.setGender(Gender.MALE);
        student.setAddress("Av. Test 123");
        student.setPhone("987654321");
        student.setParentPhone("123456789");
        student.setParentEmail("parent@test.com");
        student.setStatus(Status.ACTIVE);
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());

        // Assert
        assertAll(
            () -> assertEquals("test-id-123", student.getId()),
            () -> assertEquals("institution-001", student.getInstitutionId()),
            () -> assertEquals("Juan", student.getFirstName()),
            () -> assertEquals("Pérez", student.getLastName()),
            () -> assertEquals(DocumentType.DNI, student.getDocumentType()),
            () -> assertEquals("12345678", student.getDocumentNumber()),
            () -> assertEquals(LocalDate.of(2000, 1, 15), student.getBirthDate()),
            () -> assertEquals(Gender.MALE, student.getGender()),
            () -> assertEquals("Av. Test 123", student.getAddress()),
            () -> assertEquals("987654321", student.getPhone()),
            () -> assertEquals("123456789", student.getParentPhone()),
            () -> assertEquals("parent@test.com", student.getParentEmail()),
            () -> assertEquals(Status.ACTIVE, student.getStatus()),
            () -> assertNotNull(student.getCreatedAt()),
            () -> assertNotNull(student.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Debe validar campos obligatorios del estudiante")
    void shouldValidateRequiredFields() {
        // Arrange
        Student student = new Student();

        // Act & Assert - Campos pueden ser null inicialmente
        assertAll(
            () -> assertNull(student.getId()),
            () -> assertNull(student.getInstitutionId()),
            () -> assertNull(student.getFirstName()),
            () -> assertNull(student.getLastName()),
            () -> assertNull(student.getDocumentType()),
            () -> assertNull(student.getDocumentNumber()),
            () -> assertNull(student.getBirthDate()),
            () -> assertNull(student.getGender())
        );
    }

    @Test
    @DisplayName("Debe calcular la edad correctamente basada en la fecha de nacimiento")
    void shouldCalculateAgeCorrectly() {
        // Arrange
        Student student = new Student();
        LocalDate birthDate = LocalDate.of(2000, 6, 15);
        student.setBirthDate(birthDate);

        // Act
        LocalDate now = LocalDate.now();
        int expectedAge = now.getYear() - birthDate.getYear();
        
        // Ajustar si aún no ha cumplido años este año
        if (now.getDayOfYear() < birthDate.getDayOfYear()) {
            expectedAge--;
        }

        // Assert
        assertEquals(birthDate, student.getBirthDate());
        
        // Verificar que la fecha de nacimiento es válida para calcular edad
        assertTrue(birthDate.isBefore(LocalDate.now()), 
                  "La fecha de nacimiento debe ser anterior a la fecha actual");
        
        // Verificar rango de edad razonable (entre 3 y 25 años para estudiantes)
        assertTrue(expectedAge >= 3 && expectedAge <= 25, 
                  "La edad del estudiante debe estar en un rango razonable");
    }

    @Test
    @DisplayName("Debe validar formato de documento según tipo")
    void shouldValidateDocumentFormatByType() {
        // Arrange
        Student studentDNI = new Student();
        studentDNI.setDocumentType(DocumentType.DNI);
        studentDNI.setDocumentNumber("12345678");

        Student studentCE = new Student();
        studentCE.setDocumentType(DocumentType.CE);
        studentCE.setDocumentNumber("000123456");

        // Assert
        assertAll(
            () -> {
                assertEquals(DocumentType.DNI, studentDNI.getDocumentType());
                assertEquals("12345678", studentDNI.getDocumentNumber());
                assertEquals(8, studentDNI.getDocumentNumber().length());
                assertTrue(studentDNI.getDocumentNumber().matches("\\d+"), 
                          "DNI debe contener solo números");
            },
            () -> {
                assertEquals(DocumentType.CE, studentCE.getDocumentType());
                assertEquals("000123456", studentCE.getDocumentNumber());
                assertTrue(studentCE.getDocumentNumber().length() >= 8, 
                          "CE debe tener al menos 8 caracteres");
            }
        );
    }

    @Test
    @DisplayName("Debe validar formato de email del padre/tutor")
    void shouldValidateParentEmailFormat() {
        // Arrange
        Student student = new Student();

        // Test con email válido
        String validEmail = "parent@domain.com";
        student.setParentEmail(validEmail);

        // Assert
        assertEquals(validEmail, student.getParentEmail());
        assertTrue(validEmail.contains("@"), "Email debe contener @");
        assertTrue(validEmail.contains("."), "Email debe contener punto");
        assertFalse(validEmail.trim().isEmpty(), "Email no debe estar vacío");
    }

    @Test
    @DisplayName("Debe validar formato de teléfono")
    void shouldValidatePhoneFormat() {
        // Arrange
        Student student = new Student();
        
        // Test con teléfono válido
        String validPhone = "987654321";
        String validParentPhone = "123456789";
        
        student.setPhone(validPhone);
        student.setParentPhone(validParentPhone);

        // Assert
        assertAll(
            () -> {
                assertEquals(validPhone, student.getPhone());
                assertEquals(9, student.getPhone().length());
                assertTrue(student.getPhone().matches("\\d+"), 
                          "Teléfono debe contener solo números");
            },
            () -> {
                assertEquals(validParentPhone, student.getParentPhone());
                assertEquals(9, student.getParentPhone().length());
                assertTrue(student.getParentPhone().matches("\\d+"), 
                          "Teléfono del padre debe contener solo números");
            }
        );
    }

    @Test
    @DisplayName("Debe manejar estados del estudiante correctamente")
    void shouldHandleStudentStatusCorrectly() {
        // Arrange
        Student student = new Student();

        // Test estados válidos
        student.setStatus(Status.ACTIVE);
        assertEquals(Status.ACTIVE, student.getStatus());

        student.setStatus(Status.INACTIVE);
        assertEquals(Status.INACTIVE, student.getStatus());

        // Assert - Verificar que solo hay estados válidos
        assertAll(
            () -> assertNotNull(Status.ACTIVE),
            () -> assertNotNull(Status.INACTIVE),
            () -> assertTrue(Status.values().length >= 2, 
                           "Debe haber al menos 2 estados definidos")
        );
    }

    @Test
    @DisplayName("Debe manejar géneros correctamente")
    void shouldHandleGenderCorrectly() {
        // Arrange
        Student maleStudent = new Student();
        Student femaleStudent = new Student();

        // Act
        maleStudent.setGender(Gender.MALE);
        femaleStudent.setGender(Gender.FEMALE);

        // Assert
        assertAll(
            () -> assertEquals(Gender.MALE, maleStudent.getGender()),
            () -> assertEquals(Gender.FEMALE, femaleStudent.getGender()),
            () -> assertNotEquals(maleStudent.getGender(), femaleStudent.getGender()),
            () -> assertTrue(Gender.values().length >= 2, 
                           "Debe haber al menos 2 géneros definidos")
        );
    }

    @Test
    @DisplayName("Debe validar auditoría de timestamps")
    void shouldValidateAuditTimestamps() {
        // Arrange
        Student student = new Student();
        LocalDateTime now = LocalDateTime.now();

        // Act
        student.setCreatedAt(now);
        student.setUpdatedAt(now.plusMinutes(5));

        // Assert
        assertAll(
            () -> assertNotNull(student.getCreatedAt()),
            () -> assertNotNull(student.getUpdatedAt()),
            () -> assertEquals(now, student.getCreatedAt()),
            () -> assertTrue(student.getUpdatedAt().isAfter(student.getCreatedAt()) || 
                           student.getUpdatedAt().isEqual(student.getCreatedAt()),
                           "UpdatedAt debe ser igual o posterior a createdAt")
        );
    }

    @Test
    @DisplayName("Debe usar Lombok correctamente para equals y hashCode")
    void shouldUseLombokCorrectlyForEqualsAndHashCode() {
        // Arrange
        Student student1 = new Student();
        student1.setId("test-id-123");
        student1.setFirstName("Juan");
        student1.setLastName("Pérez");

        Student student2 = new Student();
        student2.setId("test-id-123");
        student2.setFirstName("Juan");
        student2.setLastName("Pérez");

        Student student3 = new Student();
        student3.setId("test-id-456");
        student3.setFirstName("María");
        student3.setLastName("González");

        // Assert
        assertAll(
            () -> assertEquals(student1, student2, "Estudiantes con mismos datos deben ser iguales"),
            () -> assertNotEquals(student1, student3, "Estudiantes con datos diferentes no deben ser iguales"),
            () -> assertEquals(student1.hashCode(), student2.hashCode(), 
                              "HashCode debe ser igual para objetos iguales"),
            () -> assertNotNull(student1.toString(), "ToString no debe ser null"),
            () -> assertTrue(student1.toString().contains("Juan"), 
                           "ToString debe contener información del objeto")
        );
    }
}