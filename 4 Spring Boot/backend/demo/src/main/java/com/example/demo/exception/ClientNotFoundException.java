package com.example.demo.exception;

import java.util.UUID;


public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(UUID clientID) {
        super("ERROR CODE 404 : " + "Client with ID " + clientID + " not found.");
    }
}
