package gbthefatboy.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Utility class for parsing date and time strings in various formats.
 * Supports multiple date and time formats and can parse combined date-time strings.
 */
public class DateTimeParser {

    // List of supported date formats
    private static final List<DateTimeFormatter> DATE_FORMATS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"), // 2019-12-02
            DateTimeFormatter.ofPattern("dd/MM/yyyy"), // 02/12/2019
            DateTimeFormatter.ofPattern("d/M/yyyy"), // 2/12/2019
            DateTimeFormatter.ofPattern("MM/dd/yyyy"), // 12/02/2019
            DateTimeFormatter.ofPattern("M/d/yyyy"), // 12/2/2019
            DateTimeFormatter.ofPattern("yyyy/MM/dd"), // 2019/12/02
            DateTimeFormatter.ofPattern("yyyy/M/d") // 2019/12/2
    );

    // List of supported time formats
    private static final List<DateTimeFormatter> TIME_FORMATS = List.of(
            DateTimeFormatter.ofPattern("HHmm"), // 1800
            DateTimeFormatter.ofPattern("HH:mm"), // 18:00
            DateTimeFormatter.ofPattern("h:mma"), // 6:00PM
            DateTimeFormatter.ofPattern("h:mm a"), // 6:00 PM
            DateTimeFormatter.ofPattern("ha"), // 6PM
            DateTimeFormatter.ofPattern("h a") // 6 PM
    );

    /**
     * Parses a date-time string that may contain date only, date and time, or a full date-time format.
     * If only date is provided, defaults time to 23:59.
     *
     * @param dateTimeStr The date-time string to parse.
     * @return A LocalDateTime object representing the parsed date and time.
     * @throws DateTimeParseException If the string cannot be parsed in any supported format.
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) throws DateTimeParseException {
        dateTimeStr = dateTimeStr.trim();

        String[] parts = dateTimeStr.split("\\s+");

        if (parts.length == 1) {

            LocalDate date = parseDate(parts[0]);
            return date.atTime(23, 59);
        } else if (parts.length == 2) {

            LocalDate date = parseDate(parts[0]);
            LocalTime time = parseTime(parts[1]);
            return date.atTime(time);
        } else {

            return parseAsDateTime(dateTimeStr);
        }
    }

    private static LocalDate parseDateHelper(String dateStr) throws DateTimeParseException {
        for (DateTimeFormatter formatter : DATE_FORMATS) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                // Try next format
            }
        }
        throw new DateTimeParseException("Unable to parse date: " + dateStr, dateStr, 0);
    }


    private static LocalTime parseTime(String timeStr) throws DateTimeParseException {
        // Convert common variations
        timeStr = timeStr.toUpperCase().replace("PM", " PM").replace("AM", " AM");

        for (DateTimeFormatter formatter : TIME_FORMATS) {
            try {
                return LocalTime.parse(timeStr, formatter);
            } catch (DateTimeParseException e) {
                // Try next format
            }
        }
        throw new DateTimeParseException("Unable to parse time: " + timeStr, timeStr, 0);
    }


    private static LocalDateTime parseAsDateTime(String dateTimeStr) throws DateTimeParseException {
        try {
            return LocalDateTime.parse(dateTimeStr);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Unable to parse date-time: " + dateTimeStr, dateTimeStr, 0);
        }
    }

    /**
     * Parses a date string in one of the supported date formats.
     *
     * @param dateStr The date string to parse.
     * @return A LocalDate object representing the parsed date.
     * @throws DateTimeParseException If the string cannot be parsed in any supported date format.
     */
    public static LocalDate parseDate(String dateStr) throws DateTimeParseException {
        return parseDateHelper(dateStr.trim());
    }
}
