package com.example.demo.controller;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.exception.DeviceNotFoundException;
import com.example.demo.exception.response.ErrorResponse;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.model.Device;
import com.example.demo.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;


@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {

	private final DeviceService deviceService;

	private static final Logger logger = Logger.getLogger(DeviceController.class.getName());

	// CREATE

	@PostMapping
	public ResponseEntity<?> createDevice(@RequestBody DeviceDTO deviceDTO) {

		try {
			Device createdDevice = deviceService.createDevice(deviceDTO);
			logger.info("Device created: " + createdDevice.toString());
			return ResponseEntity.status(HttpStatus.CREATED).body(createdDevice);
		} catch (InvalidInputException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
			logger.warning("Invalid input while creating device: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	// READ

	@GetMapping
	public ResponseEntity<List<Device>> getDeviceList() {
		List<Device> devices = deviceService.getDeviceList();

		return Optional.ofNullable(devices)
				.map(list -> {
					if (list.isEmpty()) {
						logger.warning("No devices found.");
						return ResponseEntity.status(HttpStatus.NOT_FOUND).body(list);
					} else {
						logger.info("Retrieved device list: " + list.toString());
						return ResponseEntity.ok(list);
					}
				})
				.orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}


	@GetMapping("/{deviceId}")
	public ResponseEntity<Device> getDevice(@PathVariable UUID deviceId) {

		return Optional.ofNullable(deviceService.getDeviceById(deviceId))
				.map(device -> {
					logger.info("Retrieved device by ID: " + deviceId.toString());
					return ResponseEntity.ok(device);
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// UPDATE

	@PutMapping("/{deviceId}")
	public ResponseEntity<?> updateDevice(@PathVariable UUID deviceId, @RequestBody DeviceDTO deviceDTO) {

		try {
			Device updatedDevice = deviceService.updateDevice(deviceId, deviceDTO);
			logger.info("Device updated: " + updatedDevice.toString());
			return ResponseEntity.ok(updatedDevice);
		} catch (DeviceNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
			logger.warning("Device not found while updating: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	// DELETE

	@DeleteMapping("/{deviceId}")
	public ResponseEntity<?> deleteDevice(@PathVariable UUID deviceId) {

		try {
			deviceService.deleteDevice(deviceId);
			logger.info("Device deleted: " + deviceId.toString());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (DeviceNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
			logger.warning("Device not found while deleting: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	// OTHER

	@PostMapping("/{deviceId}/measure")
	public ResponseEntity<?> measureDeviceForDate(@PathVariable UUID deviceId, @RequestBody MeasurementConsumptionDTO requestDTO) {

		try {
			Device updatedDevice = deviceService.measureForDate(deviceId, requestDTO);
			logger.info("Measured device for date: " + updatedDevice.toString());
			return ResponseEntity.ok(updatedDevice);
		} catch (DeviceNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
			logger.warning("Device not found while measuring for date: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	@GetMapping("/{deviceId}/measure")
	public ResponseEntity<?> measureDevice(@PathVariable UUID deviceId) {

		try {
			Device updatedDevice = deviceService.measureNow(deviceId);
			logger.info("Measured device now: " + updatedDevice.toString());
			return ResponseEntity.ok(updatedDevice);
		} catch (DeviceNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
			logger.warning("Device not found while measuring now: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	@PostMapping("/{deviceId}/measure/{year}/{month}")
	public ResponseEntity<?> measureDeviceForMonth(@PathVariable UUID deviceId, @PathVariable int year, @PathVariable int month) {

		try {
			Device updatedDevice = deviceService.measureForMonth(deviceId, month, year);
			logger.info("Measured device for month: " + updatedDevice.toString());
			return ResponseEntity.ok(updatedDevice);
		} catch (DeviceNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
			logger.warning("Device not found while measuring for month: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

}
