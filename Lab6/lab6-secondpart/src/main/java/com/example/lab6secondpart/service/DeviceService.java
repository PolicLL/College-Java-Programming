package com.example.lab6secondpart.service;

import com.example.lab6secondpart.exception.ClientNotFoundException;
import com.example.lab6secondpart.exception.DeviceNotFoundException;
import com.example.lab6secondpart.model.Client;
import com.example.lab6secondpart.model.Device;
import com.example.lab6secondpart.model.MeasurementConsumption;
import com.example.lab6secondpart.utils.DateUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service

public class DeviceService {

	private final RestTemplate restTemplate;

	public DeviceService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public List<Device> getAllDevices() {

		final String ENDPOINT = "http://localhost:8080/device";

		ResponseEntity<List<Device>> response = restTemplate.exchange(
				ENDPOINT, // Your API endpoint URL
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>() {}
		);

		List<Device> listOfDevices = response.getBody();

		assert listOfDevices != null;

		for(Device device : listOfDevices){
			device.setClient(getClientByDeviceId(device.getId()));
		}

		if (response.getStatusCode() == HttpStatus.OK) {
			return listOfDevices;
		} else {
			return Collections.emptyList();
		}
	}

	public MeasurementConsumption createMeasurement(UUID deviceId, MeasurementConsumption measurementDTO) {
		final String ENDPOINT = "http://localhost:8080/device/" + deviceId + "/measure";

		if(measurementDTO.getMeasurementDate() == null){
			measurementDTO.setMeasurementDate(DateUtils.getTodayDateInDesiredFormat());
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<MeasurementConsumption> requestEntity = new HttpEntity<>(measurementDTO, headers);

		ResponseEntity<Device> response = restTemplate.exchange(
				ENDPOINT,
				HttpMethod.POST,
				requestEntity,
				Device.class
		);

		if (response.getStatusCode() == HttpStatus.OK) {
			return measurementDTO;
		}

		throw new DeviceNotFoundException(deviceId);

	}

	// get client by device id

	public Client getClientByDeviceId(UUID deviceId) {
		final String ENDPOINT = "http://localhost:8080/client/byDevice/" + deviceId;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<?> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<Client> response = restTemplate.exchange(
				ENDPOINT,
				HttpMethod.GET,
				requestEntity,
				new ParameterizedTypeReference<Client>() {},
				deviceId // Pass device ID as a path variable
		);

		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		}

		throw new ClientNotFoundException(deviceId);
	}

	public List<MeasurementConsumption> getAllMeasurementsForDevice(UUID deviceId) {
		final String ENDPOINT = "http://localhost:8080/measurement-consumption/byDevice/" + deviceId;

		ResponseEntity<List<MeasurementConsumption>> response = restTemplate.exchange(
				ENDPOINT,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>() {
				}
		);

		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		} else {
			return Collections.emptyList();
		}
	}


}
