import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {

    // parse(String fullCommand);
    public static Command parse(String fullCommand) throws GBException {
        if (fullCommand.trim().isEmpty()) {
            throw new GBException("Invalid command: empty command");
        }

        String[] parts = fullCommand.trim().split(" ", 2);
        try {
            CommandType command = CommandType.fromString(parts[0]);
            String arguments = parts.length > 1 ? parts[1] : " ";
            return new Command(command, arguments);
        } catch (ArrayIndexOutOfBoundsException e) { // arguments
            throw new GBException("Task description cannot be empty");
        }
    }

    // parseTodo(String arguments);
    public static Todo parseTodo(String arguments) throws GBException {
        if (arguments.trim().isEmpty()) {
            throw new GBException("Invalid Todo: description cannot be empty!");
        }

        return new Todo(arguments);
    }

    // parseDeadline(String arguments);
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

    // parseEvent(String arguments)
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

    // parseTaskIndex(String arguments)
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

    // parse
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
