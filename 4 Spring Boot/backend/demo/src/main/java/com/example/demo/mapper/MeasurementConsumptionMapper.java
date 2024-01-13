package com.example.demo.mapper;

import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.model.Device;
import com.example.demo.model.measurement.MeasurementConsumption;

public class MeasurementConsumptionMapper {
	public static MeasurementConsumption toMeasurementConsumption(MeasurementConsumptionDTO dto, Device device) {
		return new MeasurementConsumption(device, dto.getMeasurementDate(), dto.getMeasuringUnitEnergyConsumption(), dto.getMeasurementValue());
	}
}