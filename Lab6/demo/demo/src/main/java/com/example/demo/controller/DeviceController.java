package com.example.demo.controller;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.exception.DeviceNotFoundException;
import com.example.demo.exception.response.ErrorResponse;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.model.Device;
import com.example.demo.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;


    // CREATE

    @PostMapping
    public ResponseEntity<?> createDevice(@RequestBody DeviceDTO deviceDTO) {
        Device createdDevice = deviceService.createDevice(deviceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDevice);
    }


    // READ

    @GetMapping
    public ResponseEntity<List<Device>> getDeviceList() {
        List<Device> devices = deviceService.getDeviceList();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getDevice(@PathVariable UUID deviceId) {
        Device device = deviceService.getDeviceById(deviceId);
        return ResponseEntity.ok().body(device);
    }

    // UPDATE


    @PutMapping("/{deviceId}")
    public ResponseEntity<?> updateDevice(@PathVariable UUID deviceId, @RequestBody DeviceDTO deviceDTO) {
        Device updatedDevice = deviceService.updateDevice(deviceId, deviceDTO);
        return ResponseEntity.ok(updatedDevice);
    }


    // DELETE

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<?> deleteDevice(@PathVariable UUID deviceId) {
        deviceService.deleteDevice(deviceId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // OTHER

    @PostMapping("/{deviceId}/measure")
    public ResponseEntity<?> measureDeviceForDate(@PathVariable UUID deviceId, @RequestBody MeasurementConsumptionDTO requestDTO) {
        Device updatedDevice = deviceService.measureForDate(deviceId, requestDTO);
        return ResponseEntity.ok(updatedDevice);
    }

    @GetMapping("/{deviceId}/measure")
    public ResponseEntity<?> measureDevice(@PathVariable UUID deviceId) {
        Device updatedDevice = deviceService.measureNow(deviceId);
        return ResponseEntity.ok(updatedDevice);
    }

    @PostMapping("/{deviceId}/measure/{year}/{month}")
    public ResponseEntity<?> measureDeviceForMonth(@PathVariable UUID deviceId, @PathVariable int year, @PathVariable int month) {
        Device updatedDevice = deviceService.measureForMonth(deviceId, month, year);
        return ResponseEntity.ok(updatedDevice);
    }


















}