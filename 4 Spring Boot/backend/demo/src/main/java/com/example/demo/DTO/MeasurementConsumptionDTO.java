package com.example.demo.DTO;

import com.example.demo.model.measurement.MeasuringUnitEnergyConsumption;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public class MeasurementConsumptionDTO {

	private UUID id;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
	private Date measurementDate;
	private MeasuringUnitEnergyConsumption measuringUnitEnergyConsumption;
	private double measurementValue;
	private UUID deviceID;


	public MeasurementConsumptionDTO(Date measurementDate, MeasuringUnitEnergyConsumption measuringUnitEnergyConsumption, double measurementValue) {

		this.measurementDate = measurementDate;
		this.measuringUnitEnergyConsumption = measuringUnitEnergyConsumption;
		this.measurementValue = measurementValue;
	}

	public MeasurementConsumptionDTO(Date measurementDate, MeasuringUnitEnergyConsumption measuringUnitEnergyConsumption, double measurementValue, UUID deviceID) {

		this.measurementDate = measurementDate;
		this.measuringUnitEnergyConsumption = measuringUnitEnergyConsumption;
		this.measurementValue = measurementValue;
		this.deviceID = deviceID;
	}

}