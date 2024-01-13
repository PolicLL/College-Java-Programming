package com.example.demo.service;

import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.DTO.MeasurementConsumptionReport;
import com.example.demo.exception.MeasurementConsumptionNotFoundException;
import com.example.demo.exception.MeasurementForThisMonthInYearExistsException;
import com.example.demo.mapper.MeasurementConsumptionMapper;
import com.example.demo.model.Device;
import com.example.demo.model.measurement.MeasurementConsumption;
import com.example.demo.repository.MeasurementConsumptionRepository;
import com.example.demo.service.implementation.DeviceServiceImpl;
import com.example.demo.utils.DateUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MeasurementConsumptionService {

	private final MeasurementConsumptionRepository measurementConsumptionRepository;

	private final DeviceServiceImpl deviceServiceImpl;

	private MeasurementConsumptionMapper measurementConsumptionMapper;

	public MeasurementConsumption createMeasurementConsumption(MeasurementConsumptionDTO measurementDTO, UUID deviceID) {

		measurementDTO.setDeviceID(deviceID);
		MeasurementConsumption newMeasurement = measurementConsumptionMapper.toMeasurementConsumption(measurementDTO);


		int month = DateUtils.getMonthFromDate(measurementDTO.getMeasurementDate());
		int year = DateUtils.getYearFromDate(measurementDTO.getMeasurementDate());

		if(!deviceServiceImpl.isThereMeasurementForMonthInYear(deviceID, month, year)){
			return measurementConsumptionRepository.save(newMeasurement);
		}

		throw new MeasurementForThisMonthInYearExistsException(month);
	}

	public List<MeasurementConsumption> getMeasurementConsumptionList() {
		return measurementConsumptionRepository.findAll();
	}

	public Optional<MeasurementConsumption> getMeasurementConsumptionById(UUID id) {
		return measurementConsumptionRepository.findById(id);
	}

	public MeasurementConsumption updateMeasurementConsumption(UUID measurementID, MeasurementConsumptionDTO measurementDTO) {
		MeasurementConsumption measurementToUpdate = retrieveMeasurementConsumption(measurementID);
		measurementToUpdate.updateUsingDTO(measurementDTO);
		return measurementConsumptionRepository.save(measurementToUpdate);
	}

	private MeasurementConsumption retrieveMeasurementConsumption(UUID id){
		return measurementConsumptionRepository.findById(id)
				.orElseThrow(() -> new MeasurementConsumptionNotFoundException(id));
	}

	public void deleteMeasurementConsumption(UUID measurementId) {
		measurementConsumptionRepository.deleteMeasurementConsumptionById(measurementId);
	}

	@Transactional
	public void deleteAllMeasurementConsumptions() {
		measurementConsumptionRepository.deleteAllMeasurementConsumptions();
	}

	public MeasurementConsumptionReport getMeasurementConsumptionListForYear(int year) {
		List<MeasurementConsumption> measurementConsumptionList = measurementConsumptionRepository.findByYear(year);
		double sumConsumption = measurementConsumptionList.stream()
				.mapToDouble(MeasurementConsumption::getMeasurementValue)
				.sum();

		return new MeasurementConsumptionReport(measurementConsumptionList, sumConsumption);
	}

	public MeasurementConsumptionReport getMeasurementConsumptionListForYearForMonth(int year, int month) {
		List<MeasurementConsumption> measurementConsumptionList =
				measurementConsumptionRepository.findByYearAndMonth(year, month);
		double sumConsumption = measurementConsumptionList.stream()
				.mapToDouble(MeasurementConsumption::getMeasurementValue)
				.sum();

		return new MeasurementConsumptionReport(measurementConsumptionList, sumConsumption);
	}

	public Map<String, Double> getMeasurementConsumptionListByYearForAllMonths(int year) {
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

		return monthlyConsumptionMap;


	}

	public static String getMonthName(int monthNumber) {
		if (monthNumber < 1 || monthNumber > 12)
			throw new IllegalArgumentException("Invalid month number. Must be between 1 and 12.");

		return Month.of(monthNumber).name().toUpperCase();
	}

	public List<MeasurementConsumption> getAllMeasurementsForDevice(UUID deviceId) {
		return measurementConsumptionRepository.findByDeviceId(deviceId);
	}
}