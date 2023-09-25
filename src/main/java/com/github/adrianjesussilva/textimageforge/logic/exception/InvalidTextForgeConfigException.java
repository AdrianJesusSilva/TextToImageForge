package com.github.adrianjesussilva.textimageforge.logic.exception;

/**
 * Exception for invalid text forge configuration
 * 
 * @author Adrian Jesus Simoes Silva
 *
 */
public class InvalidTextForgeConfigException extends Exception {

	private static final long serialVersionUID = 6211419468529508118L;

	public InvalidTextForgeConfigException() {
		
	}


	public InvalidTextForgeConfigException(String message) {
		super(message);
	}


	public InvalidTextForgeConfigException(Throwable cause) {
		super(cause);
	}


	public InvalidTextForgeConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidTextForgeConfigException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
