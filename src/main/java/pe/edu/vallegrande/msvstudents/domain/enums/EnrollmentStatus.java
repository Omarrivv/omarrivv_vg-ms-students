package pe.edu.vallegrande.msvstudents.domain.enums;

public enum EnrollmentStatus {
    ACTIVE("A"),
    COMPLETED("C"),
    TRANSFER("T"),
    RETIRED("R");

    private final String value;

    EnrollmentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}