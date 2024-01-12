package com.example.demo.DTO;

import com.example.demo.model.measurement.MeasuringUnitEnergyConsumption;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class MeasurementConsumptionDTO {

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Date measurementDate;
	private MeasuringUnitEnergyConsumption measuringUnitEnergyConsumption;
	private double measurementValue;

	// Getters and setters
}