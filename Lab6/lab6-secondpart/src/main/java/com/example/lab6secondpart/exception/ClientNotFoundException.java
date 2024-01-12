package com.example.lab6secondpart.exception;

import java.util.UUID;

public class ClientNotFoundException extends RuntimeException {
	public ClientNotFoundException(UUID deviceID) {
		System.out.println("The client for device with id " + deviceID + " was not found.");
	}
}
