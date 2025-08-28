package GbTheFatBoy.task;

/**
 * Represents a basic task with a description and completion status.
 * Serves as the base class for specific task types.
 */
public class Task {

    private final String description;
    private boolean isDone = false;

    /**
     * Creates a new Task with the specified description.
     * The task is initially marked as not done.
     *
     * @param description The task description.
     */
    public Task(String description) {
        this.description = description;
    }

    /**
     * Creates a new Task with description and completion status.
     *
     * @param desc The task description.
     * @param isDone The completion status of the task.
     */
    public Task(String desc, boolean isDone) {
        this.description = desc;
        this.isDone = isDone;
    }

    public boolean isDone() {
        return this.isDone;
    }

    /**
     * Marks the task as completed.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Unmarks the task (sets as not completed).
     */
    public void unmark() {
        this.isDone = false;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        String check = isDone ? "X" : " ";
        return String.format("[%s] %s", check, this.description);
    }
}
