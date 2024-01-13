package com.example.demo.mapper;

import com.example.demo.DTO.ClientDTO;
import com.example.demo.model.Client;

public class ClientMapper {

    public static Client toClient(ClientDTO clientRequest) {
        return new Client(clientRequest.getName(), clientRequest.getAddress(),clientRequest.getDevice());
    }
}
