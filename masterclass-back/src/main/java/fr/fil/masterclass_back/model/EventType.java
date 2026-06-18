package fr.fil.masterclass_back.model;

public enum EventType {
    DEVOIR("devoir"),
    EXAMEN("examen"),
    ACTIVITE("activite"),
    SPORT("sport");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
