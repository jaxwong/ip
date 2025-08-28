package GbTheFatBoy.command;

/**
 * Represents a parsed command with its type and associated arguments.
 * Encapsulates the command information extracted from user input.
 */
public class Command {

    private final CommandType type;
    private final String arguments;

    /**
     * Creates a new Command with the specified type and arguments.
     *
     * @param type The type of command.
     * @param arguments The arguments associated with the command.
     */
    public Command(CommandType type, String arguments) {
        this.type = type;
        this.arguments= arguments;
    }

    public CommandType getType() {
        return this.type;
    }

    public String getArguments() {
        return this.arguments;
    }

    /**
     * Checks if this command is an exit command.
     *
     * @return True if the command type is BYE, false otherwise.
     */
    public boolean isExit() {
        return this.type == CommandType.BYE;
    }
}
