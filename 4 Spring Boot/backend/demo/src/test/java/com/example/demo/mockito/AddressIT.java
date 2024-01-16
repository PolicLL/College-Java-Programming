package com.example.demo.mockito;


import com.example.demo.DTO.AddressDTO;
import com.example.demo.Lab6Application;
import com.example.demo.controller.AddressController;
import com.example.demo.service.AddressService;
import com.example.demo.utils.DTOUtils;
import com.example.demo.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@WebMvcTest(AddressController.class)
@ExtendWith(MockitoExtension.class)
public class AddressIT {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AddressService addressService;


	private AddressDTO addressDTO;

	private UUID addressID = null;

	@BeforeEach
	public void setUp() {
		addressID = UUID.randomUUID();
		addressDTO = new AddressDTO();
		addressDTO.setId(addressID);
	}

	@Test
	public void testCreateTrainee() throws Exception {

		addressDTO.setStreetName("Street Name 1");

		when(addressService.createAddress(any(AddressDTO.class))).thenReturn(addressDTO);

		mockMvc.perform(MockMvcRequestBuilders
						.post("/address")
						.contentType(MediaType.APPLICATION_JSON) // Set content type
						.content(TestUtils.asJsonString(new AddressDTO()))) // Convert DTO to JSON string
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.streetName").value("Street Name 1"));

	}

	@Test
	public void testGetAddressById() throws Exception {

		addressDTO.setStreetName("Street Name 1");

		when(addressService.getAddressById(addressID)).thenReturn(addressDTO);

		mockMvc.perform(MockMvcRequestBuilders.get("/address/{addressId}", addressID)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(addressID.toString()))
				.andExpect(jsonPath("$.streetName").value("Street Name 1"));
	}

	@Test
	public void testGetAddressList() throws Exception {

		addressDTO.setStreetName("Street Name 1");

		List<AddressDTO> addressList = Collections.singletonList(addressDTO);
		Page<AddressDTO> addressPage = new PageImpl<>(addressList);

		when(addressService.getAddressPageWithSorting(anyInt(), anyInt(), any(String[].class)))
				.thenReturn(addressPage);

		mockMvc.perform(MockMvcRequestBuilders.get("/address"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(jsonPath("$.content[0].id", equalTo(addressDTO.getId().toString())))
				.andExpect(jsonPath("$.content[0].streetName", equalTo(addressDTO.getStreetName())));

		verify(addressService, times(1)).getAddressPageWithSorting(eq(0), eq(3), any(String[].class));
	}

	@Test
	public void testUpdateAddress() throws Exception {

		addressDTO.setStreetName("Updated Street");

		when(addressService.updateAddress(eq(addressID), any(AddressDTO.class))).thenReturn(addressDTO);

		mockMvc.perform(MockMvcRequestBuilders.put("/address/{addressId}", addressID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(addressDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.streetName", equalTo("Updated Street")));

		verify(addressService, times(1)).updateAddress(eq(addressID), any(AddressDTO.class));
	}

	@Test
	public void testDeleteAddress() throws Exception {
		UUID addressId = UUID.randomUUID();

		mockMvc.perform(MockMvcRequestBuilders.delete("/address/{addressId}", addressId))
				.andExpect(status().isNoContent());

		verify(addressService, times(1)).deleteAddress(eq(addressId));
	}

}





























