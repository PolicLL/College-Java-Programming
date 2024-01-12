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
}
