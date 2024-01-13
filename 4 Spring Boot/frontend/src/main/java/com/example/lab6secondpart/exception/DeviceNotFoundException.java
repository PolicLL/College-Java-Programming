package com.example.lab6secondpart.exception;

import java.util.UUID;

public class DeviceNotFoundException extends RuntimeException{
	public DeviceNotFoundException(UUID deviceId) {
		System.out.println("Device with id " + deviceId + " not found.");
	}
}
