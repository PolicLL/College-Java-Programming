package com.example.demo.service.implementation;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.exception.DeviceNotFoundException;
import com.example.demo.exception.MeasurementForThisMonthInYearExistsException;
import com.example.demo.mapper.DeviceMapper;
import com.example.demo.model.Device;
import com.example.demo.model.measurement.MeasurementConsumption;
import com.example.demo.repository.DeviceRepository;
import com.example.demo.service.DeviceService;
import com.example.demo.service.MeasurementConsumptionService;
import com.example.demo.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

	private final DeviceRepository deviceRepository;

	private final MeasurementConsumptionService measurementConsumptionService;

	private DeviceMapper deviceMapper;

	private static final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

	// CREATE

	public DeviceDTO createDevice(DeviceDTO deviceDTO) {

		logger.info("Creating new device: {}", deviceDTO);
		Device newDevice = deviceMapper.toDevice(deviceDTO);
		deviceRepository.save(newDevice);
		logger.info("Device created successfully: {}", newDevice);
		return deviceMapper.toDeviceDTO(newDevice);
	}

	// READ

	public List<DeviceDTO> getDeviceList() {
		logger.info("Fetching all devices");

		List<Device> devices = deviceRepository.findAll();
		List<DeviceDTO> deviceDTOs = devices.stream()
				.map(DeviceMapper.INSTANCE::toDeviceDTO)
				.collect(Collectors.toList());

		logger.info("Fetched {} devices successfully", deviceDTOs.size());
		return deviceDTOs;
	}


	public DeviceDTO getDeviceById(UUID id) {

		logger.info("Fetching device by ID: {}", id);
		Device device = retrieveDevice(id);
		logger.info("Fetched device successfully: {}", device);
		return deviceMapper.toDeviceDTO(device);
	}

	public Device retrieveDevice(UUID id) {

		logger.info("Retrieving device by ID: {}", id);
		Device device = deviceRepository.findById(id)
				.orElseThrow(() -> {
					logger.error("Device with ID {} not found", id);
					return new DeviceNotFoundException(id);
				});
		logger.info("Retrieved device successfully: {}", device);
		return device;
	}

	// UPDATE

	public DeviceDTO updateDevice(UUID deviceID, DeviceDTO deviceDTO) {

		logger.info("Updating device with ID {}: {}", deviceID, deviceDTO);
		Device existingDevice = retrieveDevice(deviceID);

		Device updatedDevice = deviceMapper.toDevice(deviceDTO);
		updatedDevice.setId(existingDevice.getId()); // Ensure the ID is preserved

		return deviceMapper.toDeviceDTO(deviceRepository.save(updatedDevice));

	}

	// DELETE

	public void deleteDevice(UUID deviceId) {

		logger.info("Deleting device with ID: {}", deviceId);
		deviceRepository.deleteById(deviceId);
		logger.info("Device deleted successfully");
	}

	// OTHER

	public DeviceDTO measureNow(UUID deviceID) {

		logger.info("Measuring consumption now for device with ID: {}", deviceID);
		Device device = retrieveDevice(deviceID);
		device.generateMeasurementNow(1, 1000);
		Device updatedDevice = deviceRepository.save(device);
		logger.info("Consumption measured successfully, updated device: {}", updatedDevice);
		return deviceMapper.toDeviceDTO(updatedDevice);
	}

	public void deleteAll() {

		logger.info("Deleting all devices");
		deviceRepository.deleteAll();
		logger.info("All devices deleted successfully");
	}

	public DeviceDTO measureForMonth(UUID deviceID, int month, int year) {

		logger.info("Measuring consumption for month {} and year {} for device with ID: {}", month, year, deviceID);
		Device device = retrieveDevice(deviceID);

		if (!isThereMeasurementForMonthInYear(deviceID, month, year)) {
			device.measureConsumptionForMonth(month, 1, 1000);
			Device updatedDevice = deviceRepository.save(device);
			logger.info("Consumption measured successfully, updated device: {}", updatedDevice);
			return deviceMapper.toDeviceDTO(updatedDevice);
		}

		logger.error("Measurement for the specified month {} and year {} already exists", month, year);
		throw new MeasurementForThisMonthInYearExistsException(month);
	}

	public DeviceDTO measureForDate(UUID deviceID, MeasurementConsumptionDTO measurementConsumptionDTO) {

		logger.info("Measuring consumption for specific date for device with ID: {}", deviceID);
		Device device = retrieveDevice(deviceID);

		int month = DateUtils.getMonthFromDate(measurementConsumptionDTO.getMeasurementDate());
		int year = DateUtils.getYearFromDate(measurementConsumptionDTO.getMeasurementDate());

		if (isThereMeasurementForMonthInYear(deviceID, month, year)) {
			logger.error("Measurement for the specified month {} and year {} already exists", month, year);
			throw new MeasurementForThisMonthInYearExistsException(month);
		}

		//
		MeasurementConsumption consumptionDTO = measurementConsumptionService.createMeasurementConsumption(measurementConsumptionDTO, deviceID);
		device.addMeasurement(consumptionDTO);
		//

		Device updatedDevice = deviceRepository.save(device);
		logger.info("Consumption measured successfully, updated device: {}", updatedDevice);
		return deviceMapper.toDeviceDTO(updatedDevice);
	}

	public boolean isThereMeasurementForMonthInYear(UUID deviceID, int month, int year) {

		logger.info("Checking if there is a measurement for month {} and year {} for device with ID: {}", month, year, deviceID);
		Device device = retrieveDevice(deviceID);

		if (device.getConsumptionsHistory() == null) {
			logger.info("No measurements found for the device");
			return false;
		}

		for (MeasurementConsumption measurementConsumption : device.getConsumptionsHistory()) {
			if (isDateInMonth(measurementConsumption.getMeasurementDate(), month) && isSameYear(measurementConsumption.getMeasurementDate(), year)) {
				logger.info("Measurement found for the specified month {} and year {} for device with ID: {}", month, year, deviceID);
				return true;
			}
		}

		logger.info("No measurements found for the specified month {} and year {} for device with ID: {}", month, year, deviceID);
		return false;
	}

	private boolean isDateInMonth(Date date, int month) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int dateMonth = calendar.get(Calendar.MONTH) + 1; // Calendar months are zero-based (0 - January, 1 - February, etc.)

		return dateMonth == month;
	}

	private boolean isSameYear(Date date, int year) {

		return DateUtils.getYearFromDate(date) == year;
	}

}
