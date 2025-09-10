package gbthefatboy.entry;

import java.time.LocalDate;

import gbthefatboy.command.Command;
import gbthefatboy.exception.GbException;
import gbthefatboy.parser.Parser;
import gbthefatboy.storage.Storage;
import gbthefatboy.storage.TaskList;
import gbthefatboy.task.Deadline;
import gbthefatboy.task.Event;
import gbthefatboy.task.Task;
import gbthefatboy.task.Todo;
import gbthefatboy.ui.Ui;

/**
 * Main application class that coordinates all components of the task management system.
 * Handles initialization, command processing, and the main application loop.
 */
public class GbTheFatBoy {

    private final Ui ui;
    private final Storage storage;
    private TaskList taskList;

    /**
     * Creates a new GbTheFatBoy application instance.
     * Initializes UI, storage, and attempts to load existing tasks.
     *
     * @param dataFilePath The file path where tasks are stored.
     */
    public GbTheFatBoy(String dataFilePath) {
        this.ui = new Ui();
        this.storage = new Storage(dataFilePath);
        try {
            this.taskList = new TaskList(this.storage.loadTasks());
        } catch (GbException e) {
            ui.showLoadingError();
            this.taskList = new TaskList();
        }
    }

    /**
     * Starts the main application loop.
     * Continuously reads and processes user commands until exit command is received.
     */
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
            } catch (GbException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Processes a single command and returns a response string for GUI mode.
     * This method is used by the JavaFX GUI to get responses without printing to console.
     *
     * @param input The user input command string.
     * @return The response string to display in the GUI.
     */
    public String getResponse(String input) {
        try {
            Command command = Parser.parse(input);
            String response = executeCommandForGui(command);

            // Save after each command (except BYE)
            if (!command.isExit()) {
                saveTasksToStorage();
            }

            return response;

        } catch (IllegalArgumentException ie) {
            return "Invalid command\n"
                + "Valid commands: todo, deadline, event, list, mark, unmark, delete, "
                             + "find-date, find, bye";
        } catch (GbException e) {
            return e.getMessage();
        }
    }

    /**
     * Executes a parsed command and returns the response string for GUI.
     * Similar to executeCommand but returns strings instead of using UI directly.
     *
     * @param command The command to execute.
     * @return The response string.
     * @throws GbException If there's an error executing the command.
     */
    private String executeCommandForGui(Command command) throws GbException {
        switch (command.getType()) {
        case TODO -> {
            try {
                Todo todo = Parser.parseTodo(command.getArguments());
                taskList.add(todo);
                return String.format("Got it. I've added this task:\n  %s\nNow you have %d tasks in the list.",
                        todo, taskList.getSize());
            } catch (GbException e) {
                if (e.getMessage().startsWith("Invalid Todo")) {
                    return "Task description cannot be empty!\n"
                            + "Input todo in format: todo <task>\n"
                            + "Example:\n"
                            + "todo borrow book";
                }
                throw e;
            }
        }
        case DEADLINE -> {
            try {
                Deadline deadline = Parser.parseDeadline(command.getArguments());
                taskList.add(deadline);
                return String.format("Got it. I've added this task:\n  %s\nNow you have %d tasks in the list.",
                        deadline, taskList.getSize());
            } catch (GbException e) {
                if (e.getMessage().equals("Invalid deadline format")) {
                    return "Input deadline in below format:\n"
                            + "deadline <task> /by <date> [time]\n"
                            + "Examples:\n"
                            + "deadline return book /by 2019-12-02\n"
                            + "deadline submit report /by 2/12/2019 1800\n"
                            + "deadline meeting /by 15/10/2019 2:30PM";
                } else if (e.getMessage().equals("Task description and deadline cannot be empty")) {
                    return "Task description and deadline cannot be empty!";
                } else if (e.getMessage().startsWith("Invalid date/time format")) {
                    return "Invalid date/time format: " + command.getArguments() + "\n"
                            + "Supported formats: yyyy-MM-dd, dd/MM/yyyy, MM/dd/yyyy\n"
                            + "Time formats: HHmm, HH:mm, h:mma, ha (optional)";
                } else {
                    throw e;
                }
            }
        }
        case EVENT -> {
            try {
                Event event = Parser.parseEvent(command.getArguments());
                taskList.add(event);
                return String.format("Got it. I've added this task:\n  %s\nNow you have %d tasks in the list.",
                        event, taskList.getSize());
            } catch (GbException e) {
                if (e.getMessage().equals("Invalid event format")) {
                    return "Input event in the below format:\n"
                            + "event <event name> /from <start date-time> /to <end date-time>\n"
                            + "Examples:\n"
                            + "event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600\n"
                            + "event conference /from 15/10/2019 2:00PM /to 17/10/2019 5:00PM";
                } else if (e.getMessage().equals("Event description and dates cannot be empty")) {
                    return "Event description and dates cannot be empty!";
                } else if (e.getMessage().equals("End date/time cannot be before start date/time")) {
                    return "End date/time cannot be before start date/time!";
                } else if (e.getMessage().startsWith("Invalid date/time format")) {
                    return "Invalid date/time format: " + command.getArguments() + "\n"
                            + "Supported formats: yyyy-MM-dd, dd/MM/yyyy, MM/dd/yyyy\n"
                            + "Time formats: HHmm, HH:mm, h:mma, ha (optional)";
                } else {
                    throw e;
                }
            }
        }
        case LIST -> {
            if (taskList.getTasks().isEmpty()) {
                return "You have no tasks in your list.";
            }
            StringBuilder response = new StringBuilder("Here are the tasks in your list:\n");
            for (int i = 0; i < taskList.getTasks().size(); i++) {
                response.append(String.format("%d. %s\n", i + 1, taskList.getTasks().get(i)));
            }
            return response.toString().trim();
        }
        case MARK -> {
            try {
                int index = Parser.parseTaskIndex(command.getArguments());
                taskList.mark(index);
                Task task = taskList.getTask(index);
                return String.format("Nice! I've marked this task as done:\n  %s", task);
            } catch (GbException e) {
                if (e.getMessage().equals("Invalid task index")) {
                    if (taskList.getSize() == 0) {
                        return "Invalid index!\nThere are no tasks in your list!";
                    } else {
                        return String.format("Invalid index!\nPlease enter a number from 1 to %d", taskList.getSize());
                    }
                } else {
                    return "Invalid number format for task index.\nEnter a whole number please";
                }
            }
        }
        case UNMARK -> {
            try {
                int index = Parser.parseTaskIndex(command.getArguments());
                taskList.unmark(index);
                Task task = taskList.getTask(index);
                return String.format("OK, I've marked this task as not done yet:\n  %s", task);
            } catch (GbException e) {
                if (e.getMessage().equals("Invalid task index")) {
                    if (taskList.getSize() == 0) {
                        return "Invalid index!\nThere are no tasks in your list!";
                    } else {
                        return String.format("Invalid index!\nPlease enter a number from 1 to %d", taskList.getSize());
                    }
                } else {
                    return "Invalid number format for task index.\nEnter a whole number please";
                }
            }
        }
        case DELETE -> {
            try {
                int index = Parser.parseTaskIndex(command.getArguments());
                Task deletedTask = taskList.delete(index);
                return String.format("Noted. I've removed this task:\n  %s\nNow you have %d tasks in the list.",
                        deletedTask, taskList.getSize());
            } catch (GbException e) {
                if (e.getMessage().equals("Invalid task index")) {
                    if (taskList.getSize() == 0) {
                        return "Invalid index!\nThere are no tasks in your list!";
                    } else {
                        return String.format("Invalid index!\nPlease enter a number from 1 to %d", taskList.getSize());
                    }
                } else {
                    return "Invalid number format for task index.\nEnter a whole number please";
                }
            }
        }
        case FIND_DATE -> {
            try {
                LocalDate targetDate = Parser.parseDate(command.getArguments());
                var tasks = taskList.findTasksByDate(targetDate);
                if (tasks.isEmpty()) {
                    return String.format("No tasks found on %s.", targetDate);
                }
                StringBuilder response = new StringBuilder(String.format("Tasks on %s:\n",
                        targetDate.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd yyyy"))));
                for (int i = 0; i < tasks.size(); i++) {
                    response.append(String.format("%d. %s\n", i + 1, tasks.get(i)));
                }
                return response.toString().trim();
            } catch (GbException e) {
                if (e.getMessage().startsWith("Invalid date format")
                        || e.getMessage().startsWith("Date cannot be empty")) {
                    return e.getMessage() + "\n"
                            + "Supported formats: yyyy-MM-dd, dd/MM/yyyy, MM/dd/yyyy\n"
                            + "Example: find-date 2019-12-02";
                }
                throw e;
            }
        }
        case FIND -> {
            String keyword = command.getArguments();
            var tasks = taskList.findTasksByKeyword(keyword);
            if (tasks.isEmpty()) {
                return "No tasks found on this date.";
            }
            StringBuilder response = new StringBuilder("Here are the matching tasks in your list:\n");
            for (int i = 0; i < tasks.size(); i++) {
                response.append(String.format("%d. %s\n", i + 1, tasks.get(i)));
            }
            return response.toString().trim();
        }
        case BYE -> {
            return "Bye. Hope to see you again soon!";
        }
        default -> {
            return "Unknown command: " + command.getType();
        }
        }
    }

    /**
     * Executes a parsed command by delegating to the appropriate handler.
     * Handles all command types and their specific error conditions.
     *
     * @param command The command to execute.
     * @throws GbException If there's an error executing the command.
     */
    private void executeCommand(Command command) throws GbException {
        switch (command.getType()) {
        case TODO -> {
            try {
                Todo todo = Parser.parseTodo(command.getArguments());
                taskList.add(todo);
                ui.showTaskAdded(todo, taskList.getSize());
            } catch (GbException e) {
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
            } catch (GbException e) {
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
            } catch (GbException e) {
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
            } catch (GbException e) {
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
            } catch (GbException e) {
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
            } catch (GbException e) {
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
            } catch (GbException e) {
                if (e.getMessage().startsWith("Invalid date format")) {
                    ui.showError(e.getMessage());
                    ui.showFindDateFormat();
                } else if (e.getMessage().startsWith("Date cannot be empty")) {
                    ui.showError(e.getMessage());
                    ui.showFindDateFormat();
                }
            }
        }
        case FIND -> {
            String keyword = command.getArguments();
            ui.showTasksWithKey(taskList.findTasksByKeyword(keyword), keyword);
        }
        case BYE -> {
            ui.showGoodbye();
        }
        default -> {
            System.out.println("unknown command: " + command.getType());
        }
        }
    }

    /**
     * Saves the current task list to persistent storage.
     * Displays an error message if saving fails.
     */
    private void saveTasksToStorage() {
        try {
            storage.saveTasks(taskList.getTasks());
        } catch (GbException e) {
            ui.showError("Failed to save tasks: " + e.getMessage());
        }
    }

    /**
     * Returns a welcome message for GUI mode.
     * @return Welcome message string.
     */
    public String getWelcomeMessage() {
        return "Hello! I'm GbTheFatBoy\nWhat can I do for you?";
    }

    /**
     * No longer the main entry point for the app with the implementation of JavaFx
     * The main entry point is now through Launcher.main.
     * This method is never called.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new GbTheFatBoy("./data/Gbot.txt").run();
    }

}
