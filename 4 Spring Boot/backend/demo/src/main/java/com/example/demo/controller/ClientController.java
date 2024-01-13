package com.example.demo.controller;

import com.example.demo.DTO.ClientDTO;
import com.example.demo.exception.ClientNotFoundException;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.response.ErrorResponse;
import com.example.demo.model.Client;
import com.example.demo.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;


@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

	private final ClientService clientService;

	private static final Logger logger = Logger.getLogger(ClientController.class.getName());

	@PostMapping
	public ResponseEntity<?> createClient(@RequestBody ClientDTO clientDTO) {

		ClientDTO createdClient = clientService.createClient(clientDTO);
		logger.info("Client created: " + createdClient.toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
	}

	@GetMapping
	public ResponseEntity<List<ClientDTO>> getClientList() {

		List<ClientDTO> clients = clientService.getClientList();
		logger.info("Retrieved client list: " + clients.toString());

		return clients.stream()
				.findFirst()
				.map(client -> ResponseEntity.ok(clients))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(clients));
	}

	@GetMapping("/{clientId}")
	public ResponseEntity<ClientDTO> getClient(@PathVariable UUID clientId) {

		return Optional.ofNullable(clientService.getClientById(clientId))
				.map(client -> {
					logger.info("Retrieved client by ID: " + clientId.toString());
					return ResponseEntity.ok(client);
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PutMapping("/{clientId}")
	public ResponseEntity<?> updateClient(@PathVariable UUID clientId, @RequestBody ClientDTO clientDTO) {

		ClientDTO updatedClient = clientService.updateClient(clientId, clientDTO);
		logger.info("Client updated: " + updatedClient.toString());
		return ResponseEntity.ok(updatedClient);
	}

	@DeleteMapping("/{clientId}")
	public ResponseEntity<?> deleteClient(@PathVariable UUID clientId) {

		clientService.deleteClient(clientId);
		logger.info("Client deleted: " + clientId.toString());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/byDevice/{deviceId}")
	public ResponseEntity<ClientDTO> getClientsByDeviceId(@PathVariable UUID deviceId) {

		ClientDTO client = clientService.getClientsByDeviceId(deviceId);
		logger.info("Retrieved client by device ID: " + deviceId.toString());

		return Optional.ofNullable(clientService.getClientsByDeviceId(deviceId))
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(client));
	}

}
