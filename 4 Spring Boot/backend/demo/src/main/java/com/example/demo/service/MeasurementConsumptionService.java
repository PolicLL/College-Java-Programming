package com.example.demo.service;

import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.DTO.MeasurementConsumptionReport;
import com.example.demo.model.measurement.MeasurementConsumption;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MeasurementConsumptionService {

	MeasurementConsumptionDTO createMeasurementConsumption(MeasurementConsumptionDTO measurementDTO, UUID deviceID);

	List<MeasurementConsumptionDTO> getMeasurementConsumptionList();

	MeasurementConsumptionDTO getMeasurementConsumptionById(UUID id);

	MeasurementConsumptionDTO updateMeasurementConsumption(UUID measurementID, MeasurementConsumptionDTO measurementDTO);

	void deleteMeasurementConsumption(UUID measurementId);

	void deleteAllMeasurementConsumptions();

	MeasurementConsumptionReport getMeasurementConsumptionListForYear(int year);

	MeasurementConsumptionReport getMeasurementConsumptionListForYearForMonth(int year, int month);

	Map<String, Double> getMeasurementConsumptionListByYearForAllMonths(int year);

	List<MeasurementConsumptionDTO> getAllMeasurementsForDevice(UUID deviceId);
}
