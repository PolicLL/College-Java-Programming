package com.example.lab6secondpart.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

	public static Date getTodayDate() {
		return new Date(); // This will return the current date and time
	}

	public static Date getTodayDateInDesiredFormat() {
		// Get today's date
		LocalDate today = LocalDate.now();

		// Convert LocalDate to Date
		return Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
