import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class GbTheFatBoy {

    public static final String NAME = "GbTheFatBoy";
    public static final String MESSAGE = "I'm smelly meow meow!";
    public static final String LINE = "_".repeat(60);
    private static final String DATA_FILE_PATH = "./data/GBot.txt";

    public static void main(String[] args) {
        greet();
        Storage storage = new Storage(DATA_FILE_PATH);
        TaskList taskList = new TaskList(storage);
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
                            System.out.println("deadline <task> /by <date> [time]");
                            System.out.println("Examples:");
                            System.out.println("deadline return book /by 2019-12-02");
                            System.out.println("deadline submit report /by 2/12/2019 1800");
                            System.out.println("deadline meeting /by 15/10/2019 2:30PM");
                            continue;
                        }
                        String[] deadlineParts = rem.split(" /by ", 2);
                        if (deadlineParts[0].isEmpty() || deadlineParts[1].isEmpty() ) {
                            System.out.println("Description/deadline cannot be " +
                                    "empty!");
                            continue;
                        }
                        try {
                            LocalDateTime deadlineDateTime = DateTimeParser.parseDateTime(deadlineParts[1].trim());
                            Deadline deadline = new Deadline(deadlineParts[0].trim(), deadlineDateTime);
                            taskList.add(deadline);
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date/time format: " + deadlineParts[1]);
                            System.out.println("Supported formats: yyyy-MM-dd, dd/MM/yyyy, MM/dd/yyyy");
                            System.out.println("Time formats: HHmm, HH:mm, h:mma, ha (optional)");
                        }
                    }
                    case EVENT -> {
                        if (!rem.contains(" /from ") || !rem.contains(" /to ")) {
                            System.out.println("Input event in the below format:");
                            System.out.println("event <event name> /from <start date-time> /to <end date-time>");
                            System.out.println("Examples:");
                            System.out.println("event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
                            System.out.println("event conference /from 15/10/2019 2:00PM /to 17/10/2019 5:00PM");
                            continue;
                        }
                        String[] eventParts = rem.split(" /from ", 2);
                        String desc = eventParts[0];
                        String[] eventDates = eventParts[1].split(" /to ", 2);
                        String startDateStr = eventDates[0].trim();
                        String endDateStr = eventDates[1].trim();
                        if (desc.isEmpty() || startDateStr.isEmpty() || endDateStr.isEmpty()) {
                            System.out.println("Event description and dates cannot be empty!");
                            continue;
                        }

                        try {
                            LocalDateTime startDateTime = DateTimeParser.parseDateTime(startDateStr);
                            LocalDateTime endDateTime = DateTimeParser.parseDateTime(endDateStr);

                            if (endDateTime.isBefore(startDateTime)) {
                                System.out.println("End date/time cannot be before start date/time!");
                                continue;
                            }

                            Event event = new Event(desc, startDateTime, endDateTime);
                            taskList.add(event);
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date/time format in event dates.");
                            System.out.println("Supported formats: yyyy-MM-dd, dd/MM/yyyy, MM/dd/yyyy");
                            System.out.println("Time formats: HHmm, HH:mm, h:mma, ha (optional)");
                        }
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
                    case FIND_DATE -> {
                        try {
                            LocalDate targetDate = DateTimeParser.parseDate(rem);
                            taskList.findTasksByDate(targetDate);
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format: " + rem);
                            System.out.println("Supported formats: yyyy-MM-dd, dd/MM/yyyy, MM/dd/yyyy");
                            System.out.println("Example: find-date 2019-12-02");
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
                        "unmark, delete, find-date, bye");
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
