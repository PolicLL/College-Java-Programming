package com.example.demo.utils;

import java.util.Calendar;

import java.util.Date;

public class RandomValueGenerator {

    public static double generateRandomValue(double startRange, double endRange){
        if (startRange >= endRange) {
            double temp = startRange;
            startRange = endRange;
            endRange = temp;
        }

        return startRange + (Math.random() * (endRange - startRange));
    }

    /**
     *
     * Function for generating random date in last 30 days
     *
     * @return
     */
    public static Date generateRandomDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // Get the current time in milliseconds
        long currentTimeInMillis = calendar.getTimeInMillis();

        // Calculate the maximum date possible (30 days ago)
        long thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000;
        long minTimeInMillis = currentTimeInMillis - thirtyDaysInMillis;

        // Generate a random date within the last 30 days
        long randomTimeInMillis = minTimeInMillis + (long) (Math.random() * (currentTimeInMillis - minTimeInMillis));

        // Set the generated time to the calendar
        calendar.setTimeInMillis(randomTimeInMillis);

        return calendar.getTime();
    }

}
