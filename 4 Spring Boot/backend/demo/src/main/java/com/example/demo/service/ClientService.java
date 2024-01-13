package com.example.demo.service;

import com.example.demo.DTO.ClientDTO;
import com.example.demo.model.Client;

import java.util.List;
import java.util.UUID;

public interface ClientService {

    ClientDTO createClient(ClientDTO clientRequest);

    List<ClientDTO> getClientList();

    ClientDTO getClientById(UUID id);

    ClientDTO updateClient(UUID clientID, ClientDTO clientDTO);

    void deleteClient(UUID clientId);

    void deleteAll();

    ClientDTO getClientsByDeviceId(UUID deviceId);
}
