package com.example.demo.mockito;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.DTO.ClientDTO;
import com.example.demo.controller.ClientController;
import com.example.demo.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;


@WebMvcTest(ClientController.class)
@ExtendWith(MockitoExtension.class)
public class ClientIT {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ClientService clientService;

	private ClientDTO clientDTO;

	private UUID clientID = null;

	@BeforeEach
	public void setUp() {

		clientID = UUID.randomUUID();
		clientDTO = new ClientDTO();
		clientDTO.setId(clientID);
	}

	@Test
	public void testCreateClient() throws Exception {

		clientDTO.setName("Test Client");

		when(clientService.createClient(any(ClientDTO.class))).thenReturn(clientDTO);

		mockMvc.perform(MockMvcRequestBuilders.post("/client")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(clientDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", equalTo("Test Client")));

		verify(clientService, times(1)).createClient(any(ClientDTO.class));
	}

	@Test
	public void testGetClientList() throws Exception {

		clientDTO.setName("Test Client");

		List<ClientDTO> clients = Collections.singletonList(clientDTO);

		when(clientService.getClientList()).thenReturn(clients);

		mockMvc.perform(MockMvcRequestBuilders.get("/client"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name", equalTo("Test Client")));

		verify(clientService, times(1)).getClientList();
	}

	@Test
	public void testGetClient() throws Exception {

		clientDTO.setName("Test Client");

		when(clientService.getClientById(eq(clientID))).thenReturn(clientDTO);

		mockMvc.perform(MockMvcRequestBuilders.get("/client/{clientId}", clientID))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", equalTo(clientID.toString())))
				.andExpect(jsonPath("$.name", equalTo("Test Client")));

		verify(clientService, times(1)).getClientById(eq(clientID));
	}

	@Test
	public void testUpdateClient() throws Exception {

		clientDTO.setName("Updated Client");

		when(clientService.updateClient(eq(clientID), any(ClientDTO.class))).thenReturn(clientDTO);

		mockMvc.perform(MockMvcRequestBuilders.put("/client/{clientId}", clientID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(clientDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", equalTo("Updated Client")));

		verify(clientService, times(1)).updateClient(eq(clientID), any(ClientDTO.class));
	}

	@Test
	public void testDeleteClient() throws Exception {

		UUID clientId = UUID.randomUUID();

		mockMvc.perform(MockMvcRequestBuilders.delete("/client/{clientId}", clientId))
				.andExpect(status().isNoContent());

		verify(clientService, times(1)).deleteClient(eq(clientId));
	}

	@Test
	public void testGetClientsByDeviceId() throws Exception {

		UUID deviceID = UUID.randomUUID();
		clientDTO.setName("Test Client");

		when(clientService.getClientsByDeviceId(eq(deviceID))).thenReturn(clientDTO);

		mockMvc.perform(MockMvcRequestBuilders.get("/client/byDevice/{deviceId}", deviceID))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", equalTo("Test Client")));

		verify(clientService, times(1)).getClientsByDeviceId(eq(deviceID));
	}

	// Utility method to convert objects to JSON strings
	private String asJsonString(Object obj) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
