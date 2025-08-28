package GbTheFatBoy.task;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EventTest {
    @Test
    public void testConstructorSetsDescriptionAndDates() {
        LocalDateTime start = LocalDateTime.of(2025, 8, 28, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 8, 28, 12, 0);
        Event event = new Event("Team meeting", start, end);

        assertEquals("Team meeting", event.getDescription());
        assertFalse(event.isDone());
        assertEquals(start, event.getStartDateTime());
        assertEquals(end, event.getEndDateTime());
    }

    @Test
    public void testConstructorWithIsDone() {
        LocalDateTime start = LocalDateTime.of(2025, 8, 28, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 8, 28, 12, 0);
        Event event = new Event("Team meeting", true, start, end);

        assertEquals("Team meeting", event.getDescription());
        assertTrue(event.isDone());
        assertEquals(start, event.getStartDateTime());
        assertEquals(end, event.getEndDateTime());
    }

    @Test
    public void testGetStartAndEndDateTimeString() {
        LocalDateTime start = LocalDateTime.of(2025, 8, 28, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 8, 28, 12, 0);
        Event event = new Event("Team meeting", start, end);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");
        assertEquals(start.format(formatter), event.getStartDateTimeString());
        assertEquals(end.format(formatter), event.getEndDateTimeString());
    }

    @Test
    public void testGetStartAndEndDateTimeForStorage() {
        LocalDateTime start = LocalDateTime.of(2025, 8, 28, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 8, 28, 12, 0);
        Event event = new Event("Team meeting", start, end);

        assertEquals(start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), event.getStartDateTimeForStorage());
        assertEquals(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), event.getEndDateTimeForStorage());
    }

    @Test
    public void testToStringNotDone() {
        LocalDateTime start = LocalDateTime.of(2025, 8, 28, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 8, 28, 12, 0);
        Event event = new Event("Team meeting", start, end);

        String expected = String.format("[E][ ] Team meeting (from: %s to: %s)",
                start.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma")),
                end.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma")));
        assertEquals(expected, event.toString());
    }

    @Test
    public void testToStringDone() {
        LocalDateTime start = LocalDateTime.of(2025, 8, 28, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 8, 28, 12, 0);
        Event event = new Event("Team meeting", true, start, end);

        String expected = String.format("[E][X] Team meeting (from: %s to: %s)",
                start.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma")),
                end.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma")));
        assertEquals(expected, event.toString());
    }
}
