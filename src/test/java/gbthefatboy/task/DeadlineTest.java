package gbthefatboy.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DeadlineTest { // must be public
    @Test
    public void testConstructorSetsDescriptionAndDeadline() {
        LocalDateTime dt = LocalDateTime.of(2025, 8, 28, 15, 30);
        Deadline deadline = new Deadline("Submit report", dt);

        assertEquals("Submit report", deadline.getDescription());
        assertFalse(deadline.isDone());
        assertEquals(dt, deadline.getDeadline());
    }

    @Test
    public void testConstructorWithIsDone() {
        LocalDateTime dt = LocalDateTime.of(2025, 8, 28, 15, 30);
        Deadline deadline = new Deadline("Submit report", true, dt);

        assertEquals("Submit report", deadline.getDescription());
        assertTrue(deadline.isDone());
        assertEquals(dt, deadline.getDeadline());
    }

    @Test
    public void testGetDeadlineString() {
        LocalDateTime dt = LocalDateTime.of(2025, 8, 28, 15, 30);
        Deadline deadline = new Deadline("Submit report", dt);

        String expected = dt.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"));
        assertEquals(expected, deadline.getDeadlineString());
    }

    @Test
    public void testGetDeadlineForStorage() {
        LocalDateTime dt = LocalDateTime.of(2025, 8, 28, 15, 30);
        Deadline deadline = new Deadline("Submit report", dt);

        String expected = dt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        assertEquals(expected, deadline.getDeadlineForStorage());
    }

    @Test
    public void testToStringNotDone() {
        LocalDateTime dt = LocalDateTime.of(2025, 8, 28, 15, 30);
        Deadline deadline = new Deadline("Submit report", dt);

        String expected = String.format("[D][ ] Submit report (by: %s)",
                dt.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma")));
        assertEquals(expected, deadline.toString());
    }

    @Test
    public void testToStringDone() {
        LocalDateTime dt = LocalDateTime.of(2025, 8, 28, 15, 30);
        Deadline deadline = new Deadline("Submit report", true, dt);

        String expected = String.format("[D][X] Submit report (by: %s)",
                dt.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma")));
        assertEquals(expected, deadline.toString());
    }
}
