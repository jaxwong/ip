package GbTheFatBoy.ui;

import GbTheFatBoy.task.Task;

import java.util.Scanner;

import java.util.ArrayList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Handles all user interface interactions including input/output operations.
 * Manages display of messages, prompts, and formatted output to the console.
 */
public class Ui {
    private static final String NAME = "GbTheFatBoy";
    private static final String MESSAGE = "I'm smelly meow meow!";
    private static final String LINE = "_".repeat(60);

    private final Scanner scanner;

    /**
     * Creates a new Ui instance and initializes the scanner for user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a command line from user input.
     *
     * @return The command string entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the welcome message when the application starts.
     */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println("Hello! I'm " + NAME);
        System.out.println(MESSAGE);
        System.out.println("What can I do for you?");
        System.out.println(LINE);
        System.out.println();
    }

    /**
     * Displays the goodbye message when the application exits.
     */
    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Displays a separator line for formatting output.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Displays an error message when tasks cannot be loaded from storage.
     */
    public void showLoadingError() {
        System.out.println("Error loading tasks from file. Starting with empty task list.");
    }

    /**
     * Displays a formatted error message.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println("Error: " + message);
        System.out.println(LINE);
    }

    /**
     * Displays confirmation that a task has been added.
     *
     * @param task The task that was added.
     * @param totalTasks The total number of tasks after adding.
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println(String.format("Now you have %d tasks in the list.", totalTasks));
        System.out.println(LINE);
        System.out.println();
    }

    /**
     * Displays all tasks in the task list with numbered formatting.
     *
     * @param tasks The list of tasks to display.
     */
    public void showTaskList(ArrayList<Task> tasks) {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Displays confirmation that a task has been marked as done.
     *
     * @param task The task that was marked.
     */
    public void showTaskMarked(Task task) {
        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
        System.out.println(LINE);
    }

    /**
     * Displays confirmation that a task has been unmarked (set as not done).
     *
     * @param task The task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(LINE);
        System.out.println("Ok, I've marked this task as not done yet:");
        System.out.println(task);
        System.out.println(LINE);
    }

    /**
     * Displays confirmation that a task has been deleted.
     *
     * @param task The task that was deleted.
     * @param totalTasks The total number of tasks after deletion.
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Displays all tasks occurring on a specific date.
     *
     * @param tasks The list of tasks occurring on the specified date.
     * @param date The date to display tasks for.
     */
    public void showTasksOnDate(ArrayList<Task> tasks, LocalDate date) {
        System.out.println(LINE);
        System.out.println("Tasks on " + date.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");

        if (tasks.isEmpty()) {
            System.out.println("No tasks found on this date.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
        System.out.println(LINE);
    }

    /**
     * Displays all tasks containing a specific keyword.
     *
     * @param tasks The list of tasks occurring on the specified date.
     * @param key The date to display tasks for.
     */
    public void showTasksWithKey(ArrayList<Task> tasks, String key) {
        System.out.println(LINE);
        System.out.println("Here are the matching tasks in your list:");

        if (tasks.isEmpty()) {
            System.out.println("No tasks found on this date.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
        System.out.println(LINE);
    }



    /**
     * Displays an error message for invalid task indices.
     *
     * @param totalTasks The total number of tasks in the list.
     */
    public void showInvalidIndex(int totalTasks) {
        System.out.println(LINE);
        System.out.println("Invalid index!");
        if (totalTasks == 0) {
            System.out.println("There are no tasks in your list!");
        } else {
            System.out.println("Please enter a number from 1 to " + totalTasks);
        }
        System.out.println(LINE);
    }

    /**
     * Displays an error message and help text for empty task descriptions.
     */
    public void showEmptyDescription() {
        System.out.println(LINE);
        System.out.println("Task description cannot be empty!");
        System.out.println("Input todo in format: todo <task>");
        System.out.println("Example: ");
        System.out.println("todo borrow book");
        System.out.println(LINE);
        System.out.println();
    }

    /**
     * Displays an error message for invalid commands and lists valid commands.
     */
    public void showInvalidCommand() {
        System.out.println("Invalid command");
        System.out.println("Valid commands: todo, deadline, event, list, mark, unmark, delete, find-date, bye");
    }

    /**
     * Displays an error message for invalid number format in task indices.
     */
    public void showInvalidNumberFormat() {
        System.out.println("Invalid number format for task index.");
        System.out.println("Enter a whole number please");
    }

    /**
     * Displays help text and examples for deadline command format.
     */
    public void showDeadlineFormat() {
        System.out.println("Input deadline in below format:");
        System.out.println("deadline <task> /by <date> [time]");
        System.out.println("Examples:");
        System.out.println("deadline return book /by 2019-12-02");
        System.out.println("deadline submit report /by 2/12/2019 1800");
        System.out.println("deadline meeting /by 15/10/2019 2:30PM");
    }

    /**
     * Displays help text and examples for event command format.
     */
    public void showEventFormat() {
        System.out.println("Input event in the below format:");
        System.out.println("event <event name> /from <start date-time> /to <end date-time>");
        System.out.println("Examples:");
        System.out.println("event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
        System.out.println("event conference /from 15/10/2019 2:00PM /to 17/10/2019 5:00PM");
    }

    /**
     * Displays an error message for invalid date/time format with supported formats.
     *
     * @param invalidInput The invalid input that caused the error.
     */
    public void showDateTimeFormatError(String invalidInput) {
        System.out.println("Invalid date/time format: " + invalidInput);
        System.out.println("Supported formats: yyyy-MM-dd, dd/MM/yyyy, MM/dd/yyyy");
        System.out.println("Time formats: HHmm, HH:mm, h:mma, ha (optional)");
    }

    /**
     * Displays help text for find-date command format.
     */
    public void showFindDateFormat() {
        System.out.println("Supported formats: yyyy-MM-dd, dd/MM/yyyy, MM/dd/yyyy");
        System.out.println("Example: find-date 2019-12-02");
    }

    /**
     * Displays an error message when end time is before start time in events.
     */
    public void showEndTimeBeforeStartTime() {
        System.out.println("End date/time cannot be before start date/time!");
    }

    /**
     * Displays an error message when task description or deadline is empty.
     */
    public void showEmptyTaskDetails() {
        System.out.println("Task description and deadline cannot be empty!");
    }

    /**
     * Displays an error message when event description or dates are empty.
     */
    public void showEmptyEventDetails() {
        System.out.println("Event description and dates cannot be empty!");
    }

}
