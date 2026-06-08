package org.example.vistanhr.model;

public enum EducationLevel {
    NOT_SPECIFIED("Не указано"),
    PTO("Профессионально-техническое (ПТО)"),
    SSO("Среднее специальное (ССО)"),
    HE("Высшее образование (ВО)"),
    POSTGRADUATE("Магистратура / Аспирантура");

    private final String displayName;

    EducationLevel(String displayName) {
        this.displayName = displayName;
    }

    // Этот метод позволит нам забирать красивую строку напрямую
    public String getDisplayName() {
        return displayName;
    }
}