package GbTheFatBoy.entry;

import GbTheFatBoy.ui.Ui;

import GbTheFatBoy.storage.Storage;
import GbTheFatBoy.storage.TaskList;

import GbTheFatBoy.command.Command;
import GbTheFatBoy.command.CommandType;

import GbTheFatBoy.exception.GBException;

import GbTheFatBoy.parser.Parser;

import GbTheFatBoy.task.Task;
import GbTheFatBoy.task.Todo;
import GbTheFatBoy.task.Deadline;
import GbTheFatBoy.task.Event;

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

}
