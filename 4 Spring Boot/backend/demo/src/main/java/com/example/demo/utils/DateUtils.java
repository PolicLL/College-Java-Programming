package com.example.demo.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static int getMonthFromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// Get the month (months are zero-based, so January is 0)
		int month = calendar.get(Calendar.MONTH) + 1; // Adding 1 to get the actual month number (1 - January, 2 - February, etc.)

		return month;
	}

	public static Date getDate() {
		return new Date();
	}

	public static int getYearFromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// Get the month (months are zero-based, so January is 0)

		return calendar.get(Calendar.YEAR) + 1;
	}

	public static Date createDateWithMonth(int targetMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, targetMonth - 1); // Subtract 1 as months are zero-based

		// Set the year to the current year (you might want to adjust if you need a different year)
		int currentYear = calendar.get(Calendar.YEAR);
		calendar.set(Calendar.YEAR, currentYear);

		return calendar.getTime();
	}
}
