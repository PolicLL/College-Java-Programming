package com.example.demo.controller;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.exception.DeviceNotFoundException;
import com.example.demo.exception.ErrorResponse;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.model.Device;
import com.example.demo.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/device")
public class DeviceController {
    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // CREATE

    @PostMapping
    public ResponseEntity<?> createDevice(@RequestBody DeviceDTO deviceDTO) {
        try {
            Device createdDevice = deviceService.createDevice(deviceDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDevice);
        } catch (InvalidInputException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    // READ

    @GetMapping
    public ResponseEntity<List<Device>> getDeviceList() {
        List<Device> devices = deviceService.getDeviceList();

        if (devices.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(devices);
         else
             return ResponseEntity.ok(devices);
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getDevice(@PathVariable UUID deviceId) {
        Optional<Device> device = deviceService.getDeviceById(deviceId);

        return device.map(tempDevice -> ResponseEntity.ok().body(tempDevice))
                        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .build());
    }

    // UPDATE


    @PutMapping("/{deviceId}")
    public ResponseEntity<?> updateDevice(@PathVariable UUID deviceId, @RequestBody DeviceDTO deviceDTO) {
        try {
            Device updatedDevice = deviceService.updateDevice(deviceId, deviceDTO);
            return ResponseEntity.ok(updatedDevice);
        } catch (DeviceNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


    // DELETE

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<?> deleteDevice(@PathVariable UUID deviceId) {
        try {
            deviceService.deleteDevice(deviceId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (DeviceNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    // OTHER

    @GetMapping("/{deviceId}/measure")
    public ResponseEntity<?> measureDevice(@PathVariable UUID deviceId) {
        try {
            Device updatedDevice = deviceService.measureNow(deviceId);
            return ResponseEntity.ok(updatedDevice);
        } catch (DeviceNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping("/{deviceId}/measure/{month}")
    public ResponseEntity<?> measureDeviceForMonth(@PathVariable UUID deviceId, @PathVariable int month) {
        try {
            Device updatedDevice = deviceService.measureForMonth(deviceId, month);
            return ResponseEntity.ok(updatedDevice);
        } catch (DeviceNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping("/{deviceId}/measure")
    public ResponseEntity<?> measureDeviceForDate(@PathVariable UUID deviceId, @RequestBody MeasurementConsumptionDTO requestDTO) {
        try {
            Date measurementDate = requestDTO.getMeasurementDate();
            // Call your service method passing measurementDate and deviceId
            Device updatedDevice = deviceService.measureForDate(deviceId, measurementDate);
            return ResponseEntity.ok(updatedDevice);
        } catch (DeviceNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

















}