package gbthefatboy.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline.
 * Extends Task to include deadline date and time functionality.
 */
public class Deadline extends Task {

    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern(
            "MMM dd yyyy, h:mma");
    private final LocalDateTime deadline;


    /**
     * Creates a new Deadline task with the specified description and deadline.
     *
     * @param description The task description.
     * @param deadline The deadline date and time.
     */
    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

    /**
     * Creates a new Deadline task with description, completion status, and deadline.
     *
     * @param desc The task description.
     * @param isDone The completion status of the task.
     * @param deadline The deadline date and time.
     */
    public Deadline(String desc, boolean isDone, LocalDateTime deadline) {
        super(desc, isDone);
        this.deadline = deadline;
    }

    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    public String getDeadlineString() {
        return this.deadline.format(OUTPUT_FORMAT);
    }

    public String getDeadlineForStorage() {
        return this.deadline.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), getDeadlineString());
    }
}
