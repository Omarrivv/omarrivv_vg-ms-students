package pe.edu.vallegrande.msvstudents.domain.enums;

public enum GuardianRelationship {
    FATHER("FATHER"),
    MOTHER("MOTHER"),
    GUARDIAN("GUARDIAN"),
    GRANDPARENT("GRANDPARENT"),
    OTHER("OTHER");

    private final String value;

    GuardianRelationship(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
