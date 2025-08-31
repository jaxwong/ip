package gbthefatboy.storage;

import java.time.LocalDate;
import java.util.ArrayList;

import gbthefatboy.exception.GbException;
import gbthefatboy.task.Deadline;
import gbthefatboy.task.Event;
import gbthefatboy.task.Task;

/**
 * Manages a collection of tasks with operations for adding, retrieving, marking, and deleting tasks.
 * Provides functionality to search for tasks by date.
 */
public class TaskList {

    private ArrayList<Task> taskList = new ArrayList<>();

    /**
     * Creates an empty TaskList.
     */
    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Creates a TaskList with the provided list of tasks.
     *
     * @param tasks The initial list of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.taskList = tasks;
    }

    /**
     * Adds a task to the task list.
     *
     * @param task The task to add.
     * @throws GbException If the task description is empty.
     */
    public void add(Task task) throws GbException {
        if (task.getDescription().isEmpty()) {
            throw new GbException("Invalid description: task description cannot be "
                    + "empty!");
        }
        this.taskList.add(task);
    }

    public ArrayList<Task> getTasks() {
        return this.taskList;
    }

    public int getSize() {
        return this.taskList.size();
    }

    /**
     * Retrieves a task at the specified index (1-based indexing).
     *
     * @param index The 1-based index of the task to retrieve.
     * @return The task at the specified index.
     * @throws GbException If the index is out of bounds.
     */
    public Task getTask(int index) throws GbException {
        try {
            return this.taskList.get(index - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new GbException(e.getMessage());
        }
    }

    /**
     * Marks a task as done at the specified index (1-based indexing).
     *
     * @param index The 1-based index of the task to mark.
     * @throws GbException If the index is out of bounds.
     */
    public void mark(int index) throws GbException {
        getTask(index).mark();
    }

    /**
     * Unmarks a task (sets as not done) at the specified index (1-based indexing).
     *
     * @param index The 1-based index of the task to unmark.
     * @throws GbException If the index is out of bounds.
     */
    public void unmark(int index) throws GbException {
        getTask(index).unmark();
    }

    /**
     * Deletes a task at the specified index (1-based indexing).
     *
     * @param index The 1-based index of the task to delete.
     * @return The deleted task.
     * @throws GbException If the index is invalid or out of bounds.
     */
    public Task delete(int index) throws GbException {
        if (index < 1 || index > this.taskList.size()) {
            throw new GbException("Invalid task index");
        }
        return this.taskList.remove(index - 1);
    }

    /**
     * Finds and returns all tasks that occur on the specified date.
     * For Deadline tasks, matches if the deadline date equals the target date.
     * For Event tasks, matches if the target date falls within the event's date range.
     *
     * @param targetDate The date to search for tasks.
     * @return A list of tasks that occur on the specified date.
     */
    public ArrayList<Task> findTasksByDate(LocalDate targetDate) {
        ArrayList<Task> tasksOnDate = new ArrayList<>();

        for (Task task : taskList) {
            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                if (deadline.getDeadline().toLocalDate().equals(targetDate)) {
                    tasksOnDate.add(deadline);
                }
            } else if (task instanceof Event) {
                Event event = (Event) task;
                LocalDate eventStartDate = event.getStartDateTime().toLocalDate();
                LocalDate eventEndDate = event.getEndDateTime().toLocalDate();

                if (targetDate.equals(eventStartDate)
                        || targetDate.equals(eventEndDate)
                        || targetDate.isAfter(eventStartDate) && targetDate.isBefore(eventEndDate)) {
                    tasksOnDate.add(event);
                }
            }
        }

        if (tasksOnDate.isEmpty()) {
            System.out.println("No tasks found on this date");
            return new ArrayList<Task>();
        } else {
            return tasksOnDate;
        }

    }

    /**
     * Finds and returns all tasks that contain the specific key word
     *
     * @param keyword The key to search for in tasks.
     * @return A list of tasks that contain the specific keyword.
     */
    public ArrayList<Task> findTasksByKeyword(String keyword) {
        ArrayList<Task> tasksWithKey = new ArrayList<>();

        for (Task task : taskList) {
            if (task.getDescription().contains(keyword)) {
                tasksWithKey.add(task);
            }
        }

        if (tasksWithKey.isEmpty()) {
            System.out.println("No tasks found on this date");
            return new ArrayList<Task>();
        } else {
            return tasksWithKey;
        }

    }

}
