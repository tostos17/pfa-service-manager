package com.pfa.app.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AgeCalculator {

    /**
     * Calculates age in terms of total years and remaining days.
     * * @param birthDate The date of birth
     * @return A formatted string (e.g., "25 years and 112 days")
     */
    public static String getAgeInYearsAndDays(LocalDate birthDate) {
        LocalDate today = LocalDate.now();

        // Guard against future dates
        if (birthDate.isAfter(today)) {
            throw new IllegalArgumentException("Birth date cannot be in the future.");
        }

        // 1. Calculate the total number of full years elapsed
        long years = ChronoUnit.YEARS.between(birthDate, today);

        // 2. Add those years to the birthdate to get the most recent birthday
        LocalDate lastBirthday = birthDate.plusYears(years);

        // 3. Calculate the remaining days between that last birthday and today
        long days = ChronoUnit.DAYS.between(lastBirthday, today);

        return years + " years and " + days + " days";
    }

}
