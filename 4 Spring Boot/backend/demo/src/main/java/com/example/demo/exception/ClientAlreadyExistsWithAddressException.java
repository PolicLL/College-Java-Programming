package com.example.demo.exception;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.model.Address;

public class ClientAlreadyExistsWithAddressException extends RuntimeException {

    public ClientAlreadyExistsWithAddressException(AddressDTO address) {
        super("Client already exists with address: '" + address + "'");
    }
}
