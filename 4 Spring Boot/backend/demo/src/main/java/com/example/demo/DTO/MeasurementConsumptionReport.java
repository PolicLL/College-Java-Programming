package com.example.demo.DTO;

import com.example.demo.model.measurement.MeasurementConsumption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class MeasurementConsumptionReport {
	private List<MeasurementConsumption> measurementConsumptionList;
	private double sumConsumption;
}
