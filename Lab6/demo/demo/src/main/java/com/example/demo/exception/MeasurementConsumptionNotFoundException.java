package com.example.demo.exception;

import java.util.UUID;


public class MeasurementConsumptionNotFoundException extends RuntimeException {
	public MeasurementConsumptionNotFoundException(UUID measurementID) {
		super("ERROR CODE 404 : " + "Measurement with ID " + measurementID + " not found.");
	}
}
