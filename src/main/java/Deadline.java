import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {

    private final LocalDateTime deadline;
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern(
            "MMM dd yyyy, h:mma");

    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

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
