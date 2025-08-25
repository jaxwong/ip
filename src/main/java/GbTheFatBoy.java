import java.time.LocalDate;

public class GbTheFatBoy {

    private final Ui ui;
    private final Storage storage;
    private TaskList taskList;

    public GbTheFatBoy(String dataFilePath) {
        this.ui = new Ui();
        this.storage = new Storage(dataFilePath);
        try {
            this.taskList = new TaskList(this.storage.loadTasks());
        }
        catch (GBException e) {
            ui.showLoadingError();
            this.taskList = new TaskList();
        }
    }


    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command command = Parser.parse(fullCommand);
                executeCommand(command);
                isExit = command.isExit();

                // Save after each command (except BYE)
                if (!isExit) {
                    saveTasksToStorage();
                }

            } catch (IllegalArgumentException ie) {
                ui.showInvalidCommand();
            } catch (GBException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private void executeCommand(Command command) throws GBException {
        switch (command.getType()) {
            case TODO -> {
                try {
                    Todo todo = Parser.parseTodo(command.getArguments());
                    taskList.add(todo);
                    ui.showTaskAdded(todo, taskList.getSize());
                } catch (GBException e) {
                    if (e.getMessage().startsWith("Invalid Todo")) {
                        ui.showEmptyDescription();
                    }
                }

            }
            case DEADLINE -> {
                try {
                    Deadline deadline = Parser.parseDeadline(command.getArguments());
                    taskList.add(deadline);
                    ui.showTaskAdded(deadline, taskList.getSize());
                } catch (GBException e) {
                    if (e.getMessage().equals("Invalid deadline format")) {
                        ui.showDeadlineFormat();
                    } else if (e.getMessage().equals("Task description and deadline cannot be empty")) {
                        ui.showEmptyTaskDetails();
                    } else if (e.getMessage().startsWith("Invalid date/time format")) {
                        ui.showDateTimeFormatError(command.getArguments());
                    } else {
                        throw e;
                    }
                }
            }
            case EVENT -> {
                try {
                    Event event = Parser.parseEvent(command.getArguments());
                    taskList.add(event);
                    ui.showTaskAdded(event, taskList.getSize());
                } catch (GBException e) {
                    if (e.getMessage().equals("Invalid event format")) {
                        ui.showEventFormat();
                    } else if (e.getMessage().equals("Event description and dates cannot be empty")) {
                        ui.showEmptyEventDetails();
                    } else if (e.getMessage().equals("End date/time cannot be before start date/time")) {
                        ui.showEndTimeBeforeStartTime();
                    } else if (e.getMessage().startsWith("Invalid date/time format")) {
                        ui.showDateTimeFormatError(command.getArguments());
                    } else {
                        throw e;
                    }
                }
            }
            case LIST -> {
                ui.showTaskList(taskList.getTasks());
            }
            case MARK -> {
                try {
                    int index = Parser.parseTaskIndex(command.getArguments());
                    taskList.mark(index);
                    Task task = taskList.getTask(index);
                    ui.showTaskMarked(task);
                } catch (GBException e) {
                    if (e.getMessage().equals("Invalid task index")) {
                        ui.showInvalidIndex(taskList.getSize());
                    } else {
                        ui.showInvalidNumberFormat();
                    }
                }
            }
            case UNMARK -> {
                try {
                    int index = Parser.parseTaskIndex(command.getArguments());
                    taskList.unmark(index);
                    Task task = taskList.getTask(index);
                    ui.showTaskUnmarked(task);
                } catch (GBException e) {
                    if (e.getMessage().equals("Invalid task index")) {
                        ui.showInvalidIndex(taskList.getSize());
                    } else {
                        ui.showInvalidNumberFormat();
                    }
                }
            }
            case DELETE -> {
                try {
                    int index = Parser.parseTaskIndex(command.getArguments());
                    Task deletedTask = taskList.delete(index);
                    ui.showTaskDeleted(deletedTask, taskList.getSize());
                } catch (GBException e) {
                    if (e.getMessage().equals("Invalid task index")) {
                        ui.showInvalidIndex(taskList.getSize());
                    } else {
                        ui.showInvalidNumberFormat();
                    }
                }
            }
            case FIND_DATE -> {
                try {
                    LocalDate targetDate = Parser.parseDate(command.getArguments());
                    ui.showTasksOnDate(taskList.findTasksByDate(targetDate), targetDate);
                } catch (GBException e) {
                    if (e.getMessage().startsWith("Invalid date format")) {
                        ui.showError(e.getMessage());
                        ui.showFindDateFormat();
                    } else if (e.getMessage().startsWith("Date cannot be empty")){
                        ui.showError(e.getMessage());
                        ui.showFindDateFormat();
                    }
                }
            }
            case BYE -> {
                ui.showGoodbye();
            }
        }
    }

    private void saveTasksToStorage() {
        try {
            storage.saveTasks(taskList.getTasks());
        } catch (GBException e) {
            ui.showError("Failed to save tasks: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new GbTheFatBoy("./data/Gbot.txt").run();
    }

    //    public static void main(String[] args) {
//        greet();
//        Storage storage = new Storage(DATA_FILE_PATH);
//        TaskList taskList = new TaskList(storage);
//        Scanner scanner = new Scanner(System.in);
//
//        while (true) {
//            String input = scanner.nextLine();
//            String[] parts = input.split(" ");
//            try {
//                CommandType command = CommandType.fromString(parts[0]);
//                // the remaining string after the command
//                String rem = "";
//                int spaceIndex = input.indexOf(" ");
//                if (spaceIndex != -1) {
//                    rem = input.substring(spaceIndex + 1).trim();
//                }
//                switch (command) {
//                    case TODO -> {
//                        Todo todo = new Todo(rem);
//                        taskList.add(todo);
//                    }
//                    case DEADLINE -> {
//                        if (!rem.contains(" /by ")) {
//                            System.out.println("Input deadline in below format:");
//                            System.out.println("deadline <task> /by <date> [time]");
//                            System.out.println("Examples:");
//                            System.out.println("deadline return book /by 2019-12-02");
//                            System.out.println("deadline submit report /by 2/12/2019 1800");
//                            System.out.println("deadline meeting /by 15/10/2019 2:30PM");
//                            continue;
//                        }
//                        String[] deadlineParts = rem.split(" /by ", 2);
//                        if (deadlineParts[0].isEmpty() || deadlineParts[1].isEmpty() ) {
//                            System.out.println("Description/deadline cannot be " +
//                                    "empty!");
//                            continue;
//                        }
//                        try {
//                            LocalDateTime deadlineDateTime = DateTimeParser.parseDateTime(deadlineParts[1].trim());
//                            Deadline deadline = new Deadline(deadlineParts[0].trim(), deadlineDateTime);
//                            taskList.add(deadline);
//                        } catch (DateTimeParseException e) {
//                            System.out.println("Invalid date/time format: " + deadlineParts[1]);
//                            System.out.println("Supported formats: yyyy-MM-dd, dd/MM/yyyy, MM/dd/yyyy");
//                            System.out.println("Time formats: HHmm, HH:mm, h:mma, ha (optional)");
//                        }
//                    }
//                    case EVENT -> {
//                        if (!rem.contains(" /from ") || !rem.contains(" /to ")) {
//                            System.out.println("Input event in the below format:");
//                            System.out.println("event <event name> /from <start date-time> /to <end date-time>");
//                            System.out.println("Examples:");
//                            System.out.println("event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
//                            System.out.println("event conference /from 15/10/2019 2:00PM /to 17/10/2019 5:00PM");
//                            continue;
//                        }
//                        String[] eventParts = rem.split(" /from ", 2);
//                        String desc = eventParts[0];
//                        String[] eventDates = eventParts[1].split(" /to ", 2);
//                        String startDateStr = eventDates[0].trim();
//                        String endDateStr = eventDates[1].trim();
//                        if (desc.isEmpty() || startDateStr.isEmpty() || endDateStr.isEmpty()) {
//                            System.out.println("Event description and dates cannot be empty!");
//                            continue;
//                        }
//
//                        try {
//                            LocalDateTime startDateTime = DateTimeParser.parseDateTime(startDateStr);
//                            LocalDateTime endDateTime = DateTimeParser.parseDateTime(endDateStr);
//
//                            if (endDateTime.isBefore(startDateTime)) {
//                                System.out.println("End date/time cannot be before start date/time!");
//                                continue;
//                            }
//
//                            Event event = new Event(desc, startDateTime, endDateTime);
//                            taskList.add(event);
//                        } catch (DateTimeParseException e) {
//                            System.out.println("Invalid date/time format in event dates.");
//                            System.out.println("Supported formats: yyyy-MM-dd, dd/MM/yyyy, MM/dd/yyyy");
//                            System.out.println("Time formats: HHmm, HH:mm, h:mma, ha (optional)");
//                        }
//                    }
//                    case LIST -> taskList.print();
//                    case MARK -> {
//                        int index = Integer.parseInt(parts[1]);
//                        taskList.mark(index);
//                    }
//                    case UNMARK -> {
//                        int index = Integer.parseInt(parts[1]);
//                        taskList.unmark(index);
//
//                    }
//                    case DELETE -> {
//                        int index = Integer.parseInt(parts[1]);
//                        taskList.delete(index);
//                    }
//                    case FIND_DATE -> {
//                        try {
//                            LocalDate targetDate = DateTimeParser.parseDate(rem);
//                            taskList.findTasksByDate(targetDate);
//                        } catch (DateTimeParseException e) {
//                            System.out.println("Invalid date format: " + rem);
//                            System.out.println("Supported formats: yyyy-MM-dd, dd/MM/yyyy, MM/dd/yyyy");
//                            System.out.println("Example: find-date 2019-12-02");
//                        }
//                    }
//                    case BYE -> {
//                        bye();
//                        return;
//                    }
//                }
//            } catch (IllegalArgumentException e) {
//                System.out.println("Invalid command");
//                System.out.println("Valid commands: todo, deadline, event, list, mark, " +
//                        "unmark, delete, find-date, bye");
//            }
//
//        }
//    }

}
