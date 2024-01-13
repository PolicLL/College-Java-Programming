package com.example.demo.service;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.model.Device;
import com.example.demo.model.measurement.MeasurementConsumption;

import java.util.List;
import java.util.UUID;

public interface DeviceService {

	DeviceDTO createDevice(DeviceDTO deviceDTO);

	List<DeviceDTO> getDeviceList();

	DeviceDTO getDeviceById(UUID id);

	Device retrieveDevice(UUID id);

	DeviceDTO updateDevice(UUID deviceID, DeviceDTO deviceDTO);

	void deleteDevice(UUID deviceId);

	void deleteAll();

	DeviceDTO measureNow(UUID deviceID);

	DeviceDTO measureForMonth(UUID deviceID, int month, int year);

	DeviceDTO measureForDate(UUID deviceID, MeasurementConsumptionDTO requestDTO);

	boolean isThereMeasurementForMonthInYear(UUID deviceID, int month, int year);
}
