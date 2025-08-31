package gbthefatboy.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that occurs over a specific time period.
 * Extends Task to include start and end date/time functionality.
 */
public class Event extends Task {

    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern(
            "MMM dd yyyy, h:mma");
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;


    /**
     * Creates a new Event task with description and date/time range.
     *
     * @param description The event description.
     * @param startDate The start date and time of the event.
     * @param endDate The end date and time of the event.
     */
    public Event(String description, LocalDateTime startDate,
                 LocalDateTime endDate) {
        super(description);
        this.startDateTime = startDate;
        this.endDateTime = endDate;
    }

    /**
     * Creates a new Event task with description, completion status, and date/time range.
     *
     * @param desc The event description.
     * @param isDone The completion status of the event.
     * @param startDate The start date and time of the event.
     * @param endDate The end date and time of the event.
     */
    public Event(String desc, boolean isDone, LocalDateTime startDate,
                 LocalDateTime endDate) {
        super(desc, isDone);
        this.startDateTime = startDate;
        this.endDateTime = endDate;
    }

    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }

    public String getStartDateTimeString() {
        return this.startDateTime.format(OUTPUT_FORMAT);
    }

    public LocalDateTime getEndDateTime() {
        return this.endDateTime;
    }

    public String getEndDateTimeString() {
        return this.endDateTime.format(OUTPUT_FORMAT);
    }

    public String getStartDateTimeForStorage() {
        return this.startDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public String getEndDateTimeForStorage() {
        return this.endDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(),
                getStartDateTimeString(), getEndDateTimeString());
    }

}
