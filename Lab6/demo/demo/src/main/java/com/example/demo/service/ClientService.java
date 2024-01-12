package com.example.demo.service;

import com.example.demo.DTO.ClientDTO;
import com.example.demo.exception.ClientAlreadyExistsWithAddressException;
import com.example.demo.exception.ClientNotFoundException;
import com.example.demo.mapper.ClientMapper;
import com.example.demo.model.Address;
import com.example.demo.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.model.Client;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(ClientDTO clientRequest) {
        Client client = ClientMapper.toClient(clientRequest);

        if(isSomeOtherClientOnThisAddress(clientRequest.getAddress()))
            throw new ClientAlreadyExistsWithAddressException(clientRequest.getAddress());

        this.clientRepository.save(client);
        return client;
    }

    private boolean isSomeOtherClientOnThisAddress(Address address){
        List<Client> clients = clientRepository.findAll();

        for(Client client : clients)
            if(client.getAddress().equals(address)) return true;

        return false;
    }

    public List<Client> getClientList() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(UUID id) {
        return clientRepository.findById(id);
    }

    public Client updateClient(UUID clientID, ClientDTO clientDTO) {
        Optional<Client> optionalClient = clientRepository.findById(clientID);

        if (optionalClient.isPresent()) {
            Client clientToUpdate = optionalClient.get();

            clientToUpdate.updateUsingDTO(clientDTO);

            return clientRepository.save(clientToUpdate);
        } else {
            throw new ClientNotFoundException("There is no client with this id.");
        }
    }

    public void deleteClient(UUID clientId) {
        clientRepository.deleteById(clientId);
    }

    public void deleteAll(){
        this.clientRepository.deleteAll();
    }

    public Client getClientsByDeviceId(UUID deviceId) {
        return clientRepository.findByDeviceId(deviceId);
    }

}