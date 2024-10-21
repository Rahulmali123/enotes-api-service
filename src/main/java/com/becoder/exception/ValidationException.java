package com.becoder.exception;

import java.util.Map;

public class ValidationException extends RuntimeException 
{


	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> error;

	public ValidationException(Map<String, Object> error) {
		super("Validation failed");
		this.error = error;
	}

	public Map<String, Object> getErrors() {
		return error;
	}

}