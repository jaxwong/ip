public enum CommandType {
    TODO,
    DEADLINE,
    EVENT,
    LIST,
    MARK,
    UNMARK,
    DELETE,
    BYE;

    public static CommandType fromString(String input) {
        return switch(input.toLowerCase()) {
            case "todo" -> TODO;
            case "deadline" -> DEADLINE;
            case "event" -> EVENT;
            case "list" -> LIST;
            case "mark" -> MARK;
            case "unmark" -> UNMARK;
            case "delete" -> DELETE;
            case "bye" -> BYE;
            default -> throw new IllegalArgumentException("Unknown command " + input);
        };
    }
}


