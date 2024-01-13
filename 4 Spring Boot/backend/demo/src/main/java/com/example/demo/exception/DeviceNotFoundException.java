package com.example.demo.exception;

import java.util.UUID;


public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(UUID deviceID) {
        super("ERROR CODE 404 : " + "Device with ID " + deviceID + " not found.");
    }
}
