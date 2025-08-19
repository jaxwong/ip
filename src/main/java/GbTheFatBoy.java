import java.util.Scanner;

public class GbTheFatBoy {

    public static final String NAME = "GbTheFatBoy";
    public static final String MESSAGE = "I'm smelly meow meow!";
    public static final String LINE = "_".repeat(60);

    public static void main(String[] args) {
        greet();
        TaskList taskList = new TaskList();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String description = scanner.nextLine();
            String[] parts = description.split(" ");
            try {
                CommandType command = CommandType.fromString(parts[0]);
                // the remaining string after the command
                String rem = description.substring(description.indexOf(" ") + 1).trim();
                switch (command) {
                    case TODO -> {
                        Todo todo = new Todo(rem);
                        taskList.add(todo);
                    }
                    case DEADLINE -> {
                        String[] deadlineParts = rem.split(" /by ", 2);
                        Deadline deadline = new Deadline(deadlineParts[0],
                                deadlineParts[1]);
                        taskList.add(deadline);
                    }
                    case EVENT -> {
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
                        if (index >= 1) {
                            taskList.mark(index);
                        }
                    }
                    case UNMARK -> {
                        int index = Integer.parseInt(parts[1]);
                        if (index >= 1) {
                            taskList.unmark(index);
                        }
                    }
                    case BYE -> {
                        bye();
                        return;
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid command");
                System.out.println("Valid commands: todo, deadline, event, list, mark, " +
                        "unmark, bye");
            }
//            if (description.equals("bye")) break;
//            else if (description.equals("list")) taskList.print();
//            else if (description.startsWith("mark")) {
//                String[] parts = description.split(" ");
//                String command = parts[0];
//                int index = Integer.parseInt(parts[1]);
//                if (command.equalsIgnoreCase("mark") && index >= 1) {
//                    taskList.mark(index);
//                }
//            } else if (description.startsWith("unmark")) {
//                String[] parts = description.split(" ");
//                String command = parts[0];
//                int index = Integer.parseInt(parts[1]);
//                if (command.equalsIgnoreCase("unmark") && index >= 1) {
//                    taskList.unmark(index);
//                }
//            } else {
//                taskList.add(new Task(description));
//            }
        }
//        bye();
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
