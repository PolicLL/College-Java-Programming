package com.example.demo.exception;

import java.util.UUID;


public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(UUID addressID) {
        super("ERROR CODE 404 : " + "Address with ID " + addressID + " not found.");
    }
}
