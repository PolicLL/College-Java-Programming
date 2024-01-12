package com.example.demo;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.DTO.DeviceDTO;
import com.example.demo.controller.AddressController;
import com.example.demo.controller.ClientController;
import com.example.demo.controller.DeviceController;
import com.example.demo.exception.ClientAlreadyExistsWithAddressException;
import com.example.demo.model.Address;
import com.example.demo.model.Client;
import com.example.demo.model.Device;
import com.example.demo.DTO.ClientDTO;
import com.example.demo.service.AddressService;
import com.example.demo.service.ClientService;
import com.example.demo.service.DeviceService;
import com.example.demo.utils.DefaultData;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = Lab4Application.class)
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
    private AddressController addressController;

    @Autowired
    private DeviceController deviceController;

    private Address tempAddress;

    private Device tempDevice;

    @BeforeEach
    public void setUp(){
        clientService.deleteAll();

        tempAddress = addressController.getAddressList().getBody().get(0);
        tempDevice = deviceController.getDeviceList().getBody().get(0);
    }

    @Test
    public void TestCreateClientEndpoint() {
        ClientDTO testClientDTO = new ClientDTO("Test Client", tempAddress, tempDevice);

        ResponseEntity<Client> responseEntity = (ResponseEntity<Client>) clientController.createClient(testClientDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Test Client", responseEntity.getBody().getName());

        List<Client> clients = clientService.getClientList();
        assertEquals(1, clients.size());
        assertEquals("Test Client", clients.get(0).getName());
    }

    @Test
    public void TestCreateClientWithOccupyLocation() {
        ClientDTO testClientDTO = new ClientDTO("Test Client", tempAddress, tempDevice);
        clientController.createClient(testClientDTO);

        ClientDTO testClientDTO2 = new ClientDTO("Test Client", tempAddress, tempDevice);

        assertThrows(ClientAlreadyExistsWithAddressException.class, () -> {
            clientController.createClient(testClientDTO2);
        });
    }
//
    @Test
    public void TestGetClientListEndpoint() {
        tempAddress = addressController.getAddressList().getBody().get(0);
        Address tempAddress2 = addressController.getAddressList().getBody().get(1);

        clientController.createClient(new ClientDTO("Test Client", tempAddress, tempDevice));
        clientController.createClient(new ClientDTO("Test Client", tempAddress2, tempDevice));

        ResponseEntity<List<Client>> responseEntity = clientController.getClientList();

        assertEquals(2, responseEntity.getBody().size());
    }
    @Test
    public void TestGetClientByIdEndpoint() {
        ClientDTO testClientDTO = new ClientDTO("Test Client", tempAddress, tempDevice);

        ResponseEntity<Client> responseEntity = (ResponseEntity<Client>) clientController.createClient(testClientDTO);

        Client retrievedClient = clientController.getClient(responseEntity.getBody().getId()).getBody();

        assertEquals(testClientDTO.getName(), retrievedClient.getName());
    }

    @Test
    public void TestUpdateClientEndpoint() {
        ClientDTO testClientDTO = new ClientDTO("Test Client", tempAddress, tempDevice);

        ResponseEntity<Client> createdClient = (ResponseEntity<Client>) clientController.createClient(testClientDTO);

        ClientDTO updatedClientDTO = new ClientDTO("Update Client NAME", tempAddress, tempDevice);
        ResponseEntity<Client> updatedClient =
                (ResponseEntity<Client>) clientController.updateClient(createdClient.getBody().getId(), updatedClientDTO);

        assertEquals(updatedClientDTO.getName(), updatedClient.getBody().getName());
    }
//
    @Test
    public void TestDeleteClientEndpoint() {
        ClientDTO testClientDTO = new ClientDTO("Test Client", tempAddress, tempDevice);

        ResponseEntity<Client> createdClient = (ResponseEntity<Client>) clientController.createClient(testClientDTO);

        clientController.deleteClient(createdClient.getBody().getId());

        ResponseEntity<Client> responseEntity =
                (ResponseEntity<Client>) clientController.getClient(createdClient.getBody().getId());

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

}
