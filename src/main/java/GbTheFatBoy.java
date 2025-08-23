import java.util.Scanner;
import java.util.Arrays;

public class GbTheFatBoy {

    public static final String NAME = "GbTheFatBoy";
    public static final String MESSAGE = "I'm smelly meow meow!";
    public static final String LINE = "_".repeat(60);

    public static void main(String[] args) {
        greet();
        TaskList taskList = new TaskList();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            try {
                CommandType command = CommandType.fromString(parts[0]);
                // the remaining string after the command
                String rem = "";
                int spaceIndex = input.indexOf(" ");
                if (spaceIndex != -1) {
                    rem = input.substring(spaceIndex + 1).trim();
                }
                switch (command) {
                    case TODO -> {
                        Todo todo = new Todo(rem);
                        taskList.add(todo);
                    }
                    case DEADLINE -> {
                        if (!rem.contains(" /by ")) {
                            System.out.println("Input deadline in below format:");
                            System.out.println("deadline <task> /by <deadline>");
                            System.out.println("e.g. deadline return book /by Sunday");
                            continue;
                        }
                        String[] deadlineParts = rem.split(" /by ", 2);
                        if (deadlineParts[0].isEmpty() || deadlineParts[1].isEmpty() ) {
                            System.out.println("Description/deadline cannot be " +
                                    "empty!");
                            continue;
                        }
                        Deadline deadline = new Deadline(deadlineParts[0],
                                deadlineParts[1]);
                        taskList.add(deadline);
                    }
                    case EVENT -> {
                        if (!rem.contains(" /from ") || !rem.contains(" /to ")) {
                            System.out.println("Input event in the below format:");
                            System.out.println("event <event name> /from <start time> " +
                                    "/to <end time>");
                            System.out.println("e.g. event project meeting /from Mon " +
                                    "2pm /to 4pm");
                            continue;
                        }
                        String[] eventParts = rem.split(" /from ", 2);
                        String desc = eventParts[0];
                        String[] eventDates = eventParts[1].split(" /to ", 2);
                        String startDate = eventDates[0];
                        String endDate = eventDates[1];
                        Event event = new Event(desc, startDate, endDate);
                        taskList.add(event);
                    }
                    case LIST -> taskList.print();
                    case MARK -> {
                        int index = Integer.parseInt(parts[1]);
                        taskList.mark(index);
                    }
                    case UNMARK -> {
                        int index = Integer.parseInt(parts[1]);
                        taskList.unmark(index);

                    }
                    case DELETE -> {
                        int index = Integer.parseInt(parts[1]);
                        taskList.delete(index);
                    }
                    case BYE -> {
                        bye();
                        return;
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid command");
                System.out.println("Valid commands: todo, deadline, event, list, mark, " +
                        "unmark, delete, bye");
            }

        }
    }

    public static void greet() {
        System.out.println(LINE);
        System.out.println("Hello! I'm " + NAME);
        System.out.println(MESSAGE);
        System.out.println("What can I do for you?");
        System.out.println(LINE);
        System.out.println();
    }

    public static void bye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

}
