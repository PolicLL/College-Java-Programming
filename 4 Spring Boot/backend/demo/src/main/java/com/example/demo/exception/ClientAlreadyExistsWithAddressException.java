package com.example.demo.exception;

import com.example.demo.model.Address;

public class ClientAlreadyExistsWithAddressException extends RuntimeException {

    public ClientAlreadyExistsWithAddressException(Address address) {
        super("Client already exists with address: '" + address + "'");
    }
}
