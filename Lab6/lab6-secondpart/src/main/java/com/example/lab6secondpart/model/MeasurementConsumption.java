package com.example.lab6secondpart.model;

import com.example.lab6secondpart.model.enums.MeasuringUnitEnergyConsumption;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementConsumption {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date measurementDate;

	private MeasuringUnitEnergyConsumption measuringUnitEnergyConsumption;
	private double measurementValue;

	// Getters and setters


	@Override
	public String toString() {
		return "MeasurementConsumptionDTO{" +
				"measurementDate=" + measurementDate +
				", measuringUnitEnergyConsumption=" + measuringUnitEnergyConsumption +
				", measurementValue=" + measurementValue +
				'}';
	}
}