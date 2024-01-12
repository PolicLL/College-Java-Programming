package com.example.demo.exception;

import java.util.UUID;


public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
		super(message);
	}
}

