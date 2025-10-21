package pe.edu.vallegrande.msvstudents.domain.enums;

public enum Status {
    ACTIVE("A"),
    INACTIVE("I"),
    TRANSFER("T"),
    GRADUATED("G");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}