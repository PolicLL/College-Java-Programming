package com.example.demo.controller;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.model.Device;
import com.example.demo.service.DeviceService;
import com.example.demo.service.implementation.DeviceServiceImpl;
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

	private final DeviceService deviceServiceImpl;

	private static final Logger logger = Logger.getLogger(DeviceController.class.getName());

	// CREATE

	@PostMapping
	public ResponseEntity<?> createDevice(@RequestBody DeviceDTO deviceDTO) {

		DeviceDTO createdDevice = deviceServiceImpl.createDevice(deviceDTO);
		logger.info("Device created: " + createdDevice.toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(createdDevice);
	}

	// READ

	@GetMapping
	public ResponseEntity<List<DeviceDTO>> getDeviceList() {
		List<DeviceDTO> devices = deviceServiceImpl.getDeviceList();

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
	public ResponseEntity<DeviceDTO> getDevice(@PathVariable UUID deviceId) {

		return Optional.ofNullable(deviceServiceImpl.getDeviceById(deviceId))
				.map(device -> {
					logger.info("Retrieved device by ID: " + deviceId.toString());
					return ResponseEntity.ok(device);
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// UPDATE

	@PutMapping("/{deviceId}")
	public ResponseEntity<?> updateDevice(@PathVariable UUID deviceId, @RequestBody DeviceDTO deviceDTO) {

		DeviceDTO updatedDevice = deviceServiceImpl.updateDevice(deviceId, deviceDTO);
		logger.info("Device updated: " + updatedDevice.toString());
		return ResponseEntity.ok(updatedDevice);
	}

	// DELETE

	@DeleteMapping("/{deviceId}")
	public ResponseEntity<?> deleteDevice(@PathVariable UUID deviceId) {

		deviceServiceImpl.deleteDevice(deviceId);
		logger.info("Device deleted: " + deviceId.toString());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	// OTHER

	@PostMapping("/{deviceId}/measure")
	public ResponseEntity<?> measureDeviceForDate(@PathVariable UUID deviceId, @RequestBody MeasurementConsumptionDTO requestDTO) {

		DeviceDTO updatedDevice = deviceServiceImpl.measureForDate(deviceId, requestDTO);
		logger.info("Measured device for date: " + updatedDevice.toString());
		return ResponseEntity.ok(updatedDevice);
	}

	@GetMapping("/{deviceId}/measure")
	public ResponseEntity<?> measureDevice(@PathVariable UUID deviceId) {

		DeviceDTO updatedDevice = deviceServiceImpl.measureNow(deviceId);
		logger.info("Measured device now: " + updatedDevice.toString());
		return ResponseEntity.ok(updatedDevice);
	}

	@PostMapping("/{deviceId}/measure/{year}/{month}")
	public ResponseEntity<?> measureDeviceForMonth(@PathVariable UUID deviceId, @PathVariable int year, @PathVariable int month) {

		DeviceDTO updatedDevice = deviceServiceImpl.measureForMonth(deviceId, month, year);
		logger.info("Measured device for month: " + updatedDevice.toString());
		return ResponseEntity.ok(updatedDevice);
	}

}
