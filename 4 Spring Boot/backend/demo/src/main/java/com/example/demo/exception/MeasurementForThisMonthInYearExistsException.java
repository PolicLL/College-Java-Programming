package com.example.demo.exception;

public class MeasurementForThisMonthInYearExistsException extends RuntimeException {

	public MeasurementForThisMonthInYearExistsException(int date) {
		super("There is already measurement in this month :  " + date);
	}

	public MeasurementForThisMonthInYearExistsException(int month, int year) {
		super("There is already measurement in this month :  " + month + " for the year : " + year + ".");
	}
}