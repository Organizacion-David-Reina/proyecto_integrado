package com.proyectointegrado.reina_cabrera_david.constants;

public enum Level {
	INICIAL("Inicial"),
    INTERMEDIO("Intermedio"),
    AVANZADO("Avanzado");

    private final String value;
    
    Level(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
