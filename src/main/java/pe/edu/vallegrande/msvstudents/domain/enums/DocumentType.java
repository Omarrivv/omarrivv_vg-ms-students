package pe.edu.vallegrande.msvstudents.domain.enums;

public enum DocumentType {
    DNI("DNI"),
    CE("CE"),
    PASSPORT("PASSPORT");

    private final String value;

    DocumentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
} 