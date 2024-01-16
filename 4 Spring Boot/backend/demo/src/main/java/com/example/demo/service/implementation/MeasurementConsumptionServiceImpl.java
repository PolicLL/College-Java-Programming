package com.example.demo.service.implementation;

import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.DTO.MeasurementConsumptionReport;
import com.example.demo.exception.MeasurementConsumptionNotFoundException;
import com.example.demo.exception.MeasurementForThisMonthInYearExistsException;
import com.example.demo.mapper.MeasurementConsumptionMapper;
import com.example.demo.model.measurement.MeasurementConsumption;
import com.example.demo.repository.MeasurementConsumptionRepository;
import com.example.demo.service.DeviceService;
import com.example.demo.service.MeasurementConsumptionService;
import com.example.demo.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
@AllArgsConstructor
public class MeasurementConsumptionServiceImpl implements MeasurementConsumptionService {

	private static final Logger logger = LoggerFactory.getLogger(MeasurementConsumptionServiceImpl.class);

	private final MeasurementConsumptionRepository measurementConsumptionRepository;
	private final DeviceService deviceServiceImpl;
	private final MeasurementConsumptionMapper measurementConsumptionMapper;

	public MeasurementConsumptionDTO createMeasurementConsumption(MeasurementConsumptionDTO measurementDTO, UUID deviceID) {

		logger.info("Creating measurement consumption...");

		measurementDTO.setDeviceID(deviceID);
		MeasurementConsumption newMeasurement = measurementConsumptionMapper.toMeasurementConsumption(measurementDTO);

		int month = DateUtils.getMonthFromDate(measurementDTO.getMeasurementDate());
		int year = DateUtils.getYearFromDate(measurementDTO.getMeasurementDate());

		if (!deviceServiceImpl.isThereMeasurementForMonthInYear(deviceID, month, year)) {
			MeasurementConsumption measurementConsumption = measurementConsumptionRepository.save(newMeasurement);
			logger.info("Measurement consumption created successfully.");
			MeasurementConsumptionDTO result = measurementConsumptionMapper.toMeasurementConsumptionDTO(newMeasurement);

			logger.info("Result : {}", result);

			return result;
		}

		logger.error("Measurement for this month in year already exists.");
		throw new MeasurementForThisMonthInYearExistsException(month);
	}

	public List<MeasurementConsumptionDTO> getMeasurementConsumptionList() {

		logger.info("Retrieving measurement consumption list...");
		return measurementConsumptionRepository.findAll()
				.stream()
				.map(measurementConsumptionMapper::toMeasurementConsumptionDTO)
				.collect(Collectors.toList());
	}

	public MeasurementConsumptionDTO getMeasurementConsumptionById(UUID id) {

		logger.info("Retrieving measurement consumption by ID: {}", id);
		MeasurementConsumption measurementConsumption = retrieveMeasurementConsumption(id);
		return measurementConsumptionMapper.toMeasurementConsumptionDTO(measurementConsumption);
	}

	public MeasurementConsumptionDTO updateMeasurementConsumption(UUID measurementID, MeasurementConsumptionDTO measurementDTO) {

		logger.info("Updating measurement consumption with ID: {}", measurementID);
		MeasurementConsumption measurementToUpdate = retrieveMeasurementConsumption(measurementID);
		measurementToUpdate.updateUsingDTO(measurementDTO);

		MeasurementConsumption newMeasurementConsumption = measurementConsumptionMapper.toMeasurementConsumption(measurementDTO);
		newMeasurementConsumption.setId(measurementToUpdate.getId());

		measurementConsumptionRepository.save(newMeasurementConsumption);
		logger.info("Measurement consumption updated successfully.");
		return measurementDTO;
	}

	private MeasurementConsumption retrieveMeasurementConsumption(UUID id) {

		logger.info("Retrieving measurement consumption by ID: {}", id);
		return measurementConsumptionRepository.findById(id)
				.orElseThrow(() -> new MeasurementConsumptionNotFoundException(id));
	}

	public void deleteMeasurementConsumption(UUID measurementId) {

		logger.info("Deleting measurement consumption with ID: {}", measurementId);
		measurementConsumptionRepository.deleteMeasurementConsumptionById(measurementId);
		logger.info("Measurement consumption deleted successfully.");
	}

	@Transactional
	public void deleteAllMeasurementConsumptions() {

		logger.info("Deleting all measurement consumptions...");
		measurementConsumptionRepository.deleteAllMeasurementConsumptions();
		logger.info("All measurement consumptions deleted successfully.");
	}

	public MeasurementConsumptionReport getMeasurementConsumptionListForYear(int year) {

		logger.info("Retrieving measurement consumption list for year: {}", year);
		List<MeasurementConsumption> measurementConsumptionList = measurementConsumptionRepository.findByYear(year);
		double sumConsumption = measurementConsumptionList.stream()
				.mapToDouble(MeasurementConsumption::getMeasurementValue)
				.sum();

		logger.info("Measurement consumption list retrieved successfully.");
		return new MeasurementConsumptionReport(measurementConsumptionList, sumConsumption);
	}

	public MeasurementConsumptionReport getMeasurementConsumptionListForYearForMonth(int year, int month) {

		logger.info("Retrieving measurement consumption list for year: {} and month: {}", year, month);
		List<MeasurementConsumption> measurementConsumptionList =
				measurementConsumptionRepository.findByYearAndMonth(year, month);
		double sumConsumption = measurementConsumptionList.stream()
				.mapToDouble(MeasurementConsumption::getMeasurementValue)
				.sum();

		logger.info("Measurement consumption list retrieved successfully.");
		return new MeasurementConsumptionReport(measurementConsumptionList, sumConsumption);
	}

	public Map<String, Double> getMeasurementConsumptionListByYearForAllMonths(int year) {

		logger.info("Retrieving measurement consumption list by year for all months: {}", year);
		List<Map<Integer, Double>> result =
				measurementConsumptionRepository.getMonthlyConsumptionByYear(year);

		Map<String, Double> monthlyConsumptionMap = new HashMap<>();

		for (Month month : Month.values()) {
			monthlyConsumptionMap.put(month.toString(), 0.0);
		}

		// Update map with values from the repository result
		for (Map<Integer, Double> ent : result) {
			int monthNumber = Integer.parseInt(String.valueOf(ent.get("month")));
			Double sum = ent.get("sumValue");

			monthlyConsumptionMap.put(getMonthName(monthNumber), sum);
		}

		logger.info("Measurement consumption list retrieved successfully.");
		return monthlyConsumptionMap;
	}

	public static String getMonthName(int monthNumber) {

		if (monthNumber < 1 || monthNumber > 12)
			throw new IllegalArgumentException("Invalid month number. Must be between 1 and 12.");

		return Month.of(monthNumber).name().toUpperCase();
	}

	public List<MeasurementConsumptionDTO> getAllMeasurementsForDevice(UUID deviceId) {

		logger.info("Retrieving all measurements for device with ID: {}", deviceId);
		return measurementConsumptionRepository.findByDeviceId(deviceId)
				.stream()
				.map(measurementConsumptionMapper::toMeasurementConsumptionDTO)
				.collect(Collectors.toList());
	}

}
