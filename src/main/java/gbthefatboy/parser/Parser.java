package gbthefatboy.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import gbthefatboy.command.Command;
import gbthefatboy.command.CommandType;
import gbthefatboy.exception.GbException;
import gbthefatboy.task.Deadline;
import gbthefatboy.task.Event;
import gbthefatboy.task.Todo;


/**
 * Utility class for parsing user input into various task objects and commands.
 * Provides static methods to parse different types of tasks and extract command information.
 */
public class Parser {

    /**
     * Parses a full command string into a Command object.
     * Extracts the command type and arguments from the input string.
     *
     * @param fullCommand The complete command string to parse.
     * @return A Command object containing the parsed command type and arguments.
     * @throws GbException If the command is empty or invalid.
     */
    public static Command parse(String fullCommand) throws GbException {
        if (fullCommand.trim().isEmpty()) {
            throw new GbException("Invalid command: empty command");
        }

        String[] parts = fullCommand.trim().split(" ", 2);
        try {
            CommandType command = CommandType.fromString(parts[0]);
            String arguments = parts.length > 1 ? parts[1] : " ";
            return new Command(command, arguments.trim());
        } catch (ArrayIndexOutOfBoundsException e) { // arguments
            throw new GbException("Task description cannot be empty");
        }
    }

    /**
     * Parses arguments into a Todo task.
     *
     * @param arguments The task description for the Todo.
     * @return A Todo object with the specified description.
     * @throws GbException If the description is empty.
     */
    public static Todo parseTodo(String arguments) throws GbException {
        if (arguments.trim().isEmpty()) {
            throw new GbException("Invalid Todo: description cannot be empty!");
        }

        return new Todo(arguments);
    }

    /**
     * Parses arguments into a Deadline task.
     * Expects format: "description /by date/time".
     *
     * @param arguments The arguments containing description and deadline information.
     * @return A Deadline object with the parsed description and deadline.
     * @throws GbException If the format is invalid, description/deadline is empty, or date format is invalid.
     */
    public static Deadline parseDeadline(String arguments) throws GbException {
        if (!arguments.contains(" /by ")) {
            throw new GbException("Invalid deadline format");
        }

        String[] deadlineParts = arguments.split(" /by ", 2);
        String desc = deadlineParts[0];
        String deadlineStr = deadlineParts[1];
        if (desc.isEmpty() || deadlineStr.isEmpty()) {
            throw new GbException("Task description and deadline cannot be empty");
        }

        try {
            LocalDateTime deadlineDateTime =
                    DateTimeParser.parseDateTime(deadlineStr.trim());
            return new Deadline(desc.trim(), deadlineDateTime);
        } catch (DateTimeParseException e) {
            throw new GbException("Invalid date/time format " + deadlineParts[1]);
        }
    }

    /**
     * Parses arguments into an Event task.
     * Expects format: "description /from start_date/time /to end_date/time".
     *
     * @param arguments The arguments containing description, start date/time, and end date/time.
     * @return An Event object with the parsed description and date/time range.
     * @throws GbException If the format is invalid, any field is empty, dates are invalid, or
     *     any date is before start date.
     */
    public static Event parseEvent(String arguments) throws GbException {
        if (!arguments.contains(" /from ") || !arguments.contains(" /to ")) {
            throw new GbException("Invalid event format");
        }

        String[] eventParts = arguments.split(" /from ", 2);
        String desc = eventParts[0];
        String[] eventDates = eventParts[1].split(" /to ", 2);
        String startDateStr = eventDates[0].trim();
        String endDateStr = eventDates[1].trim();
        if (desc.isEmpty() || startDateStr.isEmpty() || endDateStr.isEmpty()) {
            throw new GbException("Description/startDate/endDate cannot be empty!");
        }

        try {
            LocalDateTime startDateTime = DateTimeParser.parseDateTime(startDateStr);
            LocalDateTime endDateTime = DateTimeParser.parseDateTime(endDateStr);

            if (endDateTime.isBefore(startDateTime)) {
                throw new GbException("End date cannot be before start date!");
            }
            return new Event(desc, startDateTime, endDateTime);

        } catch (DateTimeParseException e) {
            throw new GbException("Invalid date/time format in event dates");
        }
    }

    /**
     * Parses a string argument into a task index integer.
     *
     * @param arguments The string containing the task index.
     * @return The parsed integer index.
     * @throws GbException If the argument is empty or not a valid integer.
     */
    public static int parseTaskIndex(String arguments) throws GbException {
        if (arguments.trim().isEmpty()) {
            throw new GbException("Index cannot be empty!");
        }

        try {
            return Integer.parseInt(arguments.trim());
        } catch (NumberFormatException e) {
            throw new GbException("Invalid index: index must be a whole number!");
        }
    }

    /**
     * Parses a string argument into a LocalDate object.
     *
     * @param arguments The string containing the date to parse.
     * @return A LocalDate object representing the parsed date.
     * @throws GbException If the argument is empty or has an invalid date format.
     */
    public static LocalDate parseDate(String arguments) throws GbException {
        if (arguments.trim().isEmpty()) {
            throw new GbException("Date cannot be empty");
        }

        try {
            return DateTimeParser.parseDate(arguments.trim());
        } catch (DateTimeParseException e) {
            throw new GbException("Invalid date format: " + arguments);
        }
    }
}
