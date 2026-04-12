package com.nexttrip.backend.exception;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(String resource, String id) {
		super("%s not found: %s".formatted(resource, id));
	}
}
