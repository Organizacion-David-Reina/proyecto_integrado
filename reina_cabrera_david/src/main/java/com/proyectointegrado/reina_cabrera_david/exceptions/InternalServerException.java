package com.proyectointegrado.reina_cabrera_david.exceptions;

/**
 * The Class InternalServerException.
 * Custom runtime exception representing internal server errors.
 */
public class InternalServerException extends RuntimeException {

	/** The serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new InternalServerException with the specified message.
	 *
	 * @param message the detail message
	 */
	public InternalServerException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new InternalServerException with the specified message and cause.
	 *
	 * @param message the detail message
	 * @param cause the cause of the exception
	 */
	public InternalServerException(String message, Throwable cause) {
		super(message, cause);
	}

}
