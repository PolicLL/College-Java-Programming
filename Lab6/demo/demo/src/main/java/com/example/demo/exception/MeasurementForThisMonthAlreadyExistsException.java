package com.example.demo.exception;

import com.example.demo.model.Address;

import java.util.Date;

public class MeasurementForThisMonthAlreadyExistsException extends RuntimeException {

	public MeasurementForThisMonthAlreadyExistsException(int date) {
		super("There is already measurement in this month :  " + date);
	}
}