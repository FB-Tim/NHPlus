package de.hitec.nhplus.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for converting between {@link String} representations and Java time objects
 * such as {@link LocalDate} and {@link LocalTime}, using consistent date and time formats.
 */
public class DateConverter {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm";

    /**
     * Converts a string to a {@link LocalDate} using the pattern {@code yyyy-MM-dd}.
     *
     * @param date the date string in format "yyyy-MM-dd"
     * @return the corresponding LocalDate object
     * @throws java.time.format.DateTimeParseException if the string cannot be parsed
     */
    public static LocalDate convertStringToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    /**
     * Converts a string to a {@link LocalTime} using the pattern {@code HH:mm}.
     *
     * @param time the time string in format "HH:mm"
     * @return the corresponding LocalTime object
     * @throws java.time.format.DateTimeParseException if the string cannot be parsed
     */
    public static LocalTime convertStringToLocalTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    /**
     * Converts a {@link LocalDate} to a string representation using the pattern {@code yyyy-MM-dd}.
     *
     * @param date the LocalDate to convert
     * @return formatted date string in "yyyy-MM-dd" format
     */
    public static String convertLocalDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    /**
     * Converts a {@link LocalTime} to a string representation using the pattern {@code HH:mm}.
     *
     * @param time the LocalTime to convert
     * @return formatted time string in "HH:mm" format
     */
    public static String convertLocalTimeToString(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }
}
