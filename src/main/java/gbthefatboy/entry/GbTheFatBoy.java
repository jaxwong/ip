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
     * Main entry point for the application.
     * Creates and runs a new GbTheFatBoy instance with default data file path.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new GbTheFatBoy("./data/Gbot.txt").run();
    }

}
