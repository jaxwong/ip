public class Command {

    private final CommandType type;
    private final String arguments;

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

    public boolean isExit() {
        return this.type == CommandType.BYE;
    }
}
