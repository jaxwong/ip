package GbTheFatBoy.parser;

import GbTheFatBoy.task.Deadline;
import GbTheFatBoy.task.Event;
import GbTheFatBoy.task.Todo;
import GbTheFatBoy.command.Command;
import GbTheFatBoy.command.CommandType;
import GbTheFatBoy.exception.GBException;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
     * @throws GBException If the command is empty or invalid.
     */
    public static Command parse(String fullCommand) throws GBException {
        if (fullCommand.trim().isEmpty()) {
            throw new GBException("Invalid command: empty command");
        }

        String[] parts = fullCommand.trim().split(" ", 2);
        try {
            CommandType command = CommandType.fromString(parts[0]);
            String arguments = parts.length > 1 ? parts[1] : " ";
            return new Command(command, arguments.trim());
        } catch (ArrayIndexOutOfBoundsException e) { // arguments
            throw new GBException("Task description cannot be empty");
        }
    }

    /**
     * Parses arguments into a Todo task.
     *
     * @param arguments The task description for the Todo.
     * @return A Todo object with the specified description.
     * @throws GBException If the description is empty.
     */
    public static Todo parseTodo(String arguments) throws GBException {
        if (arguments.trim().isEmpty()) {
            throw new GBException("Invalid Todo: description cannot be empty!");
        }

        return new Todo(arguments);
    }

    /**
     * Parses arguments into a Deadline task.
     * Expects format: "description /by date/time".
     *
     * @param arguments The arguments containing description and deadline information.
     * @return A Deadline object with the parsed description and deadline.
     * @throws GBException If the format is invalid, description/deadline is empty, or date format is invalid.
     */
    public static Deadline parseDeadline(String arguments) throws GBException {
        if (!arguments.contains(" /by ")) {
            throw new GBException("Invalid deadline format");
        }

        String[] deadlineParts = arguments.split(" /by ", 2);
        String desc = deadlineParts[0];
        String deadlineStr = deadlineParts[1];
        if (desc.isEmpty() || deadlineStr.isEmpty()) {
            throw new GBException("Task description and deadline cannot be empty");
        }

        try {
            LocalDateTime deadlineDateTime =
                    DateTimeParser.parseDateTime(deadlineStr.trim());
            return new Deadline(desc.trim(), deadlineDateTime);
        } catch (DateTimeParseException e) {
            throw new GBException("Invalid date/time format " + deadlineParts[1]);
        }
    }

    /**
     * Parses arguments into an Event task.
     * Expects format: "description /from start_date/time /to end_date/time".
     *
     * @param arguments The arguments containing description, start date/time, and end date/time.
     * @return An Event object with the parsed description and date/time range.
     * @throws GBException If the format is invalid, any field is empty, dates are invalid, or end date is before start date.
     */
    public static Event parseEvent(String arguments) throws GBException {
        if (!arguments.contains(" /from " ) || !arguments.contains(" /to ")) {
            throw new GBException("Invalid event format");
        }

        String[] eventParts = arguments.split(" /from ", 2);
        String desc = eventParts[0];
        String[] eventDates = eventParts[1].split(" /to ", 2);
        String startDateStr = eventDates[0].trim();
        String endDateStr = eventDates[1].trim();
        if (desc.isEmpty() || startDateStr.isEmpty() || endDateStr.isEmpty()) {
            throw new GBException("Description/startDate/endDate cannot be empty!");
        }

        try {
            LocalDateTime startDateTime = DateTimeParser.parseDateTime(startDateStr);
            LocalDateTime endDateTime = DateTimeParser.parseDateTime(endDateStr);

            if (endDateTime.isBefore(startDateTime)) {
                throw new GBException("End date cannot be before start date!");
            }
            return new Event(desc, startDateTime, endDateTime);

        } catch (DateTimeParseException e) {
            throw new GBException("Invalid date/time format in event dates");
        }
    }

    /**
     * Parses a string argument into a task index integer.
     *
     * @param arguments The string containing the task index.
     * @return The parsed integer index.
     * @throws GBException If the argument is empty or not a valid integer.
     */
    public static int parseTaskIndex(String arguments) throws GBException {
        if (arguments.trim().isEmpty()) {
            throw new GBException("Index cannot be empty!");
        }

        try {
            return Integer.parseInt(arguments.trim());
        } catch (NumberFormatException e) {
            throw new GBException("Invalid index: index must be a whole number!");
        }
    }

    /**
     * Parses a string argument into a LocalDate object.
     *
     * @param arguments The string containing the date to parse.
     * @return A LocalDate object representing the parsed date.
     * @throws GBException If the argument is empty or has an invalid date format.
     */
    public static LocalDate parseDate(String arguments) throws GBException {
        if (arguments.trim().isEmpty()) {
            throw new GBException("Date cannot be empty");
        }

        try {
            return DateTimeParser.parseDate(arguments.trim());
        } catch (DateTimeParseException e) {
            throw new GBException("Invalid date format: " + arguments);
        }
    }
}
