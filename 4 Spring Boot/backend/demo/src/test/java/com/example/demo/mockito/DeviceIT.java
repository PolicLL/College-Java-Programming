package com.example.demo.mockito;

import com.example.demo.DTO.ClientDTO;
import com.example.demo.DTO.DeviceDTO;
import com.example.demo.controller.ClientController;
import com.example.demo.controller.DeviceController;
import com.example.demo.service.ClientService;
import com.example.demo.service.DeviceService;
import com.example.demo.utils.TestUtils;
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

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;


@WebMvcTest(DeviceController.class)
@ExtendWith(MockitoExtension.class)
public class DeviceIT {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DeviceService deviceService;

	private DeviceDTO deviceDTO;

	private UUID deviceID = null;

	@BeforeEach
	public void setUp() {

		deviceID = UUID.randomUUID();
		deviceDTO = new DeviceDTO();
		deviceDTO.setId(deviceID);
	}

	@Test
	public void testCreateDevice() throws Exception {

		deviceDTO.setName("Device Name");

		when(deviceService.createDevice(any(DeviceDTO.class))).thenReturn(deviceDTO);

		mockMvc.perform(MockMvcRequestBuilders.post("/device")
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(deviceDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", equalTo("Device Name")));

		verify(deviceService, times(1)).createDevice(any(DeviceDTO.class));
	}

	@Test
	public void testGetDeviceList() throws Exception {

		deviceDTO.setName("Device Name");

		List<DeviceDTO> deviceDTOList = Collections.singletonList(deviceDTO);

		when(deviceService.getDeviceList()).thenReturn(deviceDTOList);

		mockMvc.perform(MockMvcRequestBuilders.get("/device"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name", equalTo("Device Name")));

		verify(deviceService, times(1)).getDeviceList();
	}

	@Test
	public void testGetDevice() throws Exception{
		deviceDTO.setName("Device Name");

		when(deviceService.getDeviceById(eq(deviceID))).thenReturn(deviceDTO);

		mockMvc.perform(MockMvcRequestBuilders.get("/device/{deviceId}", deviceID))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", equalTo(deviceID.toString())))
				.andExpect(jsonPath("$.name", equalTo("Device Name")));

		verify(deviceService, times(1)).getDeviceById(eq(deviceID));
	}

	@Test
	public void testUpdateDevice() throws Exception {

		deviceDTO.setName("Device Update Name");


		when(deviceService.updateDevice(eq(deviceID), any(DeviceDTO.class))).thenReturn(deviceDTO);

		mockMvc.perform(MockMvcRequestBuilders.put("/device/{deviceId}", deviceID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(new DeviceDTO())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", equalTo("Device Update Name")));

		verify(deviceService, times(1)).updateDevice(eq(deviceID), any(DeviceDTO.class));
	}

	@Test
	public void testDeleteDevice() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.delete("/device/{deviceId}", deviceID))
				.andExpect(status().isNoContent());

		verify(deviceService, times(1)).deleteDevice(eq(deviceID));
	}

}
