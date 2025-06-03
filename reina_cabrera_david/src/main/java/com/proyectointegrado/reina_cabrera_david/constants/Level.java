package com.proyectointegrado.reina_cabrera_david.constants;

/**
 * The Enum LEVEL.
 */
public enum Level {

    /**
     * The Constant INICIAL.
     */
    INICIAL("Inicial"),

    /**
     * The Constant INTERMEDIO.
     */
    INTERMEDIO("Intermedio"),

    /**
     * The Constant AVANZADO.
     */
    AVANZADO("Avanzado");

    private final String value;
    
    /**
     * Constructor for LEVEL enum.
     *
     * @param value the string representation of the level
     */
    Level(String value) {
        this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return the string value of the level
     */
    public String getValue() {
        return value;
    }
}
