package gbthefatboy.task;

/**
 * Represents a simple todo task without any date/time constraints.
 * Extends Task to provide todo-specific formatting.
 */
public class Todo extends Task {

    /**
     * Creates a new Todo task with the specified description.
     *
     * @param description The task description.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Creates a new Todo task with description and completion status.
     *
     * @param desc The task description.
     * @param isDone The completion status of the task.
     */
    public Todo(String desc, boolean isDone) {
        super(desc, isDone);
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }
}
