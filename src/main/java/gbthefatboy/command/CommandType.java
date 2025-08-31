package gbthefatboy.command;

/**
 * Enumeration of all supported command types in the application.
 * Each enum value represents a different action the user can perform.
 */
public enum CommandType {
    TODO,
    DEADLINE,
    EVENT,
    LIST,
    MARK,
    UNMARK,
    DELETE,
    FIND_DATE,
    FIND,
    BYE;

    /**
     * Converts a string input to the corresponding CommandType.
     * Case-insensitive matching is performed.
     *
     * @param input The string representation of the command.
     * @return The corresponding CommandType enum value.
     * @throws IllegalArgumentException If the input doesn't match any known command.
     */
    public static CommandType fromString(String input) {
        return switch(input.toLowerCase()) {
        case "todo" -> TODO;
        case "deadline" -> DEADLINE;
        case "event" -> EVENT;
        case "list" -> LIST;
        case "mark" -> MARK;
        case "unmark" -> UNMARK;
        case "delete" -> DELETE;
        case "find-date" -> FIND_DATE;
        case "find" -> FIND;
        case "bye" -> BYE;
        default -> throw new IllegalArgumentException("Unknown command " + input);
        };
    }
}


