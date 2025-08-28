package GbTheFatBoy.storage;

import GbTheFatBoy.exception.GBException;
import GbTheFatBoy.task.Deadline;
import GbTheFatBoy.task.Event;
import GbTheFatBoy.task.Task;
import GbTheFatBoy.task.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {

    private TaskList taskList;
    private Todo todo;
    private Deadline deadline;
    private Event event;

    @BeforeEach
    public void setup() {
        taskList = new TaskList();
        todo = new Todo("Buy groceries");
        deadline = new Deadline("Submit report", LocalDateTime.of(2025, 8, 28, 23, 59));
        event = new Event("Team meeting", LocalDateTime.of(2025, 8, 28, 10, 0),
                LocalDateTime.of(2025, 8, 28, 12, 0));
    }

    @Test
    public void testAddAndGetTasks() throws GBException {
        taskList.add(todo);
        taskList.add(deadline);
        taskList.add(event);

        assertEquals(3, taskList.getSize());
        assertEquals(todo, taskList.getTask(1));
        assertEquals(deadline, taskList.getTask(2));
        assertEquals(event, taskList.getTask(3));
    }

    @Test
    public void testAddEmptyDescriptionThrows() {
        Todo emptyTodo = new Todo("");
        GBException exception = assertThrows(GBException.class, () -> taskList.add(emptyTodo));
        assertEquals("Invalid description: task description cannot be empty!", exception.getMessage());
    }

    @Test
    public void testGetTaskInvalidIndexThrows() {
        GBException exception = assertThrows(GBException.class, () -> taskList.getTask(1));
        assertTrue(exception.getMessage().contains("Index"));
    }

    @Test
    public void testMarkAndUnmark() throws GBException {
        taskList.add(todo);

        taskList.mark(1);
        assertTrue(todo.isDone());

        taskList.unmark(1);
        assertFalse(todo.isDone());
    }

    @Test
    public void testDeleteTask() throws GBException {
        taskList.add(todo);
        taskList.add(deadline);

        Task removed = taskList.delete(1);
        assertEquals(todo, removed);
        assertEquals(1, taskList.getSize());
        assertEquals(deadline, taskList.getTask(1));
    }

    @Test
    public void testDeleteInvalidIndexThrows() {
        GBException exception = assertThrows(GBException.class, () -> taskList.delete(1));
        assertEquals("Invalid task index", exception.getMessage());
    }

    @Test
    public void testFindTasksByDate() throws GBException {
        taskList.add(todo);
        taskList.add(deadline);
        taskList.add(event);

        LocalDate target = LocalDate.of(2025, 8, 28);
        ArrayList<Task> tasksOnDate = taskList.findTasksByDate(target);

        assertEquals(2, tasksOnDate.size());
        assertTrue(tasksOnDate.contains(deadline));
        assertTrue(tasksOnDate.contains(event));
        assertFalse(tasksOnDate.contains(todo));
    }

    @Test
    public void testFindTasksByDateNoTasks() throws GBException {
        taskList.add(todo);
        LocalDate target = LocalDate.of(2025, 8, 28);

        ArrayList<Task> tasksOnDate = taskList.findTasksByDate(target);
        assertTrue(tasksOnDate.isEmpty());
    }
}

