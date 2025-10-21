package pe.edu.vallegrande.msvstudents.domain.enums;

public enum Gender {
    MALE("M"),
    FEMALE("F"),
    OTHER("O");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}