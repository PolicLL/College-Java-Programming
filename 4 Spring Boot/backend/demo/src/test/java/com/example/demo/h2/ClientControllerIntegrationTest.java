package com.example.demo.h2;

import com.example.demo.Lab6Application;
import com.example.demo.controller.ClientController;
import com.example.demo.exception.AddressNotFoundException;
import com.example.demo.exception.ClientAlreadyExistsWithAddressException;
import com.example.demo.exception.ClientNotFoundException;
import com.example.demo.model.Client;
import com.example.demo.DTO.ClientDTO;
import com.example.demo.service.ClientService;
import com.example.demo.utils.DTOUtils;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = Lab6Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class
})
public class ClientControllerIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientController clientController;

    @Autowired
    private DTOUtils dtoUtils;

    private int numberOfClientsBeforeTest = 0;

    @BeforeEach
    public void setUp(){
        numberOfClientsBeforeTest = clientService.getClientList().size();
    }

    @Test
    public void TestCreateClient() {
        ClientDTO testClientDTO = dtoUtils.getClientDTO();

        testClientDTO.setName("Test Client");

        ResponseEntity<ClientDTO> responseEntity = (ResponseEntity<ClientDTO>) clientController.createClient(testClientDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Test Client", responseEntity.getBody().getName());

        List<ClientDTO> clients = clientService.getClientList();
        assertEquals(numberOfClientsBeforeTest + 1, clients.size());
    }

    @Test
    public void TestCreateClientWithOccupyLocation() {
        ResponseEntity<ClientDTO> createdClient = (ResponseEntity<ClientDTO>) clientController.createClient(dtoUtils.getClientDTO());

        ClientDTO testClientDTO2 = dtoUtils.getClientDTO();
        testClientDTO2.setAddress(createdClient.getBody().getAddress());

        assertThrows(ClientAlreadyExistsWithAddressException.class, () -> {
            clientController.createClient(testClientDTO2);
        });
    }

    @Test
    public void TestGetClientList() {
        clientController.createClient(dtoUtils.getClientDTO());
        clientController.createClient(dtoUtils.getClientDTO());

        ResponseEntity<List<ClientDTO>> responseEntity = clientController.getClientList();

        assertEquals(numberOfClientsBeforeTest + 2, responseEntity.getBody().size());
    }
    @Test
    public void TestGetClientById() {
        ClientDTO testClientDTO = dtoUtils.getClientDTO();

        ResponseEntity<ClientDTO> responseEntity = (ResponseEntity<ClientDTO>) clientController.createClient(testClientDTO);

        ClientDTO retrievedClient = clientController.getClient(Objects.requireNonNull(responseEntity.getBody()).getId()).getBody();

	    assert retrievedClient != null;
	    assertEquals(testClientDTO.getName(), retrievedClient.getName());
    }

    @Test
    public void testGetClientByIdException() {

        UUID fakeID = UUID.randomUUID();
        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            clientController.getClient(fakeID).getBody();
        });
    }

    @Test
    public void TestUpdateClient() {
        ClientDTO testClientDTO = dtoUtils.getClientDTO();

        ResponseEntity<ClientDTO> createdClient = (ResponseEntity<ClientDTO>) clientController.createClient(testClientDTO);

        ClientDTO updatedClientDTO = dtoUtils.getClientDTO();
        ResponseEntity<ClientDTO> updatedClient =
                (ResponseEntity<ClientDTO>) clientController.updateClient(createdClient.getBody().getId(), updatedClientDTO);

        assertEquals(updatedClientDTO.getName(), updatedClient.getBody().getName());
    }
//
    @Test
    public void TestDeleteClient() {
        ClientDTO testClientDTO = dtoUtils.getClientDTO();

        ResponseEntity<ClientDTO> createdClient = (ResponseEntity<ClientDTO>) clientController.createClient(testClientDTO);

        HttpStatus status = (HttpStatus) clientController.deleteClient(Objects.requireNonNull(createdClient.getBody()).getId()).getStatusCode();

        assertEquals(HttpStatus.NO_CONTENT, status);
    }

}
