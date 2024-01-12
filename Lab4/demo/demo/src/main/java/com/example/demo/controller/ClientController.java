package com.example.demo.controller;

import com.example.demo.DTO.ClientDTO;
import com.example.demo.exception.ClientNotFoundException;
import com.example.demo.exception.ErrorResponse;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.model.Client;
import com.example.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody ClientDTO clientDTO) {
        try{
            Client createdClient = clientService.createClient(clientDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
        }
        catch (InvalidInputException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<Client>> getClientList() {
        List<Client> clients = clientService.getClientList();
        if (clients.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(clients);
        else
            return ResponseEntity.ok(clients);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClient(@PathVariable UUID clientId) {
        Optional<Client> client = clientService.getClientById(clientId);

        return client.map(tempClient -> ResponseEntity.ok().body(tempClient))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<?> updateClient(@PathVariable UUID clientId, @RequestBody ClientDTO clientDTO) {
        try {
            Client updatedClient = clientService.updateClient(clientId, clientDTO);
            return ResponseEntity.ok(updatedClient);
        } catch (ClientNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deleteClient(@PathVariable UUID clientId) {

        try{
            clientService.deleteClient(clientId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch (ClientNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
    // Additional endpoints as per your requirements...
}