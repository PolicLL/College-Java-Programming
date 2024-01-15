package com.example.demo.service.implementation;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.DTO.ClientDTO;
import com.example.demo.exception.ClientAlreadyExistsWithAddressException;
import com.example.demo.exception.ClientNotFoundException;
import com.example.demo.mapper.AddressMapper;
import com.example.demo.mapper.ClientMapper;
import com.example.demo.model.Address;
import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;
import com.example.demo.service.ClientService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;

	private final ClientMapper clientMapper;

	private final AddressMapper addressMapper;

	private static final Logger logger = LoggerFactory.getLogger(ClientService.class);


	@Override
	public ClientDTO createClient(ClientDTO clientRequest) {

		logger.info("Creating new client: {}", clientRequest);
		Client client = clientMapper.toClient(clientRequest);

		if (isSomeOtherClientOnThisAddress(clientRequest.getAddress())) {
			logger.error("Client already exists with the given address: {}", clientRequest.getAddress());
			throw new ClientAlreadyExistsWithAddressException(clientRequest.getAddress());
		}

		this.clientRepository.save(client);
		logger.info("Client created successfully: {}", client);
		return this.clientMapper.toClientDTO(client);
	}

	@Override
	public List<ClientDTO> getClientList() {

		logger.info("Fetching all clients");

		List<Client> clients = clientRepository.findAll();
		List<ClientDTO> clientDTOs = clients.stream()
				.map(clientMapper::toClientDTO)
				.collect(Collectors.toList());

		logger.info("Fetched {} clients successfully", clientDTOs.size());
		return clientDTOs;
	}

	@Override
	public ClientDTO getClientById(UUID id) {

		logger.info("Fetching client by ID: {}", id);
		Client client = retrieveClient(id);
		logger.info("Fetched client successfully: {}", client);
		return this.clientMapper.toClientDTO(client);
	}

	@Override
	public ClientDTO updateClient(UUID clientID, ClientDTO clientDTO) {

		logger.info("Updating client with ID {}: {}", clientID, clientDTO);
		Client existingClient = retrieveClient(clientID);
		Client updatedClient = clientMapper.toClient(clientDTO);
		updatedClient.setId(existingClient.getId());
		logger.info("Client updated successfully: {}", updatedClient);
		return this.clientMapper.toClientDTO(updatedClient);
	}

	@Override
	public void deleteClient(UUID clientId) {

		logger.info("Deleting client with ID: {}", clientId);
		clientRepository.deleteById(clientId);
		logger.info("Client deleted successfully");
	}

	@Override
	public void deleteAll() {

		logger.info("Deleting all clients");
		clientRepository.deleteAll();
		logger.info("All clients deleted successfully");
	}

	@Override
	public ClientDTO getClientsByDeviceId(UUID deviceId) {

		logger.info("Fetching client by device ID: {}", deviceId);
		Client client = clientRepository.findByDeviceId(deviceId);
		logger.info("Fetched client successfully: {}", client);
		return this.clientMapper.toClientDTO(client);
	}

	private boolean isSomeOtherClientOnThisAddress(AddressDTO address) {

		logger.info("Checking if there is another client with the given address: {}", address);
		List<Client> clients = clientRepository.findAll();

		Address mappedAddress = addressMapper.toAddress(address);

		for (Client client : clients)
			if (client.getAddress().equals(mappedAddress)) {
				logger.warn("Another client already exists with the given address: {}", address);
				return true;
			}

		logger.info("No other client found with the given address: {}", address);
		return false;
	}

	private Client retrieveClient(UUID clientID) {

		logger.info("Fetching client with ID: {}", clientID);
		Client client = clientRepository.findById(clientID)
				.orElseThrow(() -> {
					logger.error("Client with ID {} not found", clientID);
					return new ClientNotFoundException(clientID);
				});
		logger.info("Fetched client successfully: {}", client);
		return client;
	}

}
