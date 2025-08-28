package GbTheFatBoy.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TodoTest {   // must be public
    @Test
    public void testConstructorSetsDescription() {
        Todo todo = new Todo("Buy milk");
        assertEquals("Buy milk", todo.getDescription());
        assertFalse(todo.isDone()); // default should be false
    }

    @Test
    public void testConstructorWithIsDone() {
        Todo todo = new Todo("Do homework", true);
        assertEquals("Do homework", todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    public void testMarkAsDone() {
        Todo todo = new Todo("Walk the dog");
        todo.mark();
        assertTrue(todo.isDone());
    }

    @Test
    public void testToStringNotDone() {
        Todo todo = new Todo("Read book");
        // Should show "[T][ ] Read book" if Task.toString() follows "[ ] description"
        assertEquals("[T][ ] Read book", todo.toString());
    }

    @Test
    public void testToStringDone() {
        Todo todo = new Todo("Read book", true);
        // Should show "[T][X] Read book" if Task.toString() shows "X" for done
        assertEquals("[T][X] Read book", todo.toString());
    }
}

