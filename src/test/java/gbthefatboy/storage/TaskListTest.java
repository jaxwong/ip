package gbthefatboy.storage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gbthefatboy.exception.GbException;
import gbthefatboy.task.Deadline;
import gbthefatboy.task.Event;
import gbthefatboy.task.Task;
import gbthefatboy.task.Todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void testAddAndGetTasks() throws GbException {
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
        GbException exception = assertThrows(GbException.class, () -> taskList.add(emptyTodo));
        assertEquals("Invalid description: task description cannot be empty!", exception.getMessage());
    }

    @Test
    public void testGetTaskInvalidIndexThrows() {
        GbException exception = assertThrows(GbException.class, () -> taskList.getTask(1));
        assertTrue(exception.getMessage().contains("Index"));
    }

    @Test
    public void testMarkAndUnmark() throws GbException {
        taskList.add(todo);

        taskList.mark(1);
        assertTrue(todo.isDone());

        taskList.unmark(1);
        assertFalse(todo.isDone());
    }

    @Test
    public void testDeleteTask() throws GbException {
        taskList.add(todo);
        taskList.add(deadline);

        Task removed = taskList.delete(1);
        assertEquals(todo, removed);
        assertEquals(1, taskList.getSize());
        assertEquals(deadline, taskList.getTask(1));
    }

    @Test
    public void testDeleteInvalidIndexThrows() {
        GbException exception = assertThrows(GbException.class, () -> taskList.delete(1));
        assertEquals("Invalid task index", exception.getMessage());
    }

    @Test
    public void testFindTasksByDate() throws GbException {
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
    public void testFindTasksByDateNoTasks() throws GbException {
        taskList.add(todo);
        LocalDate target = LocalDate.of(2025, 8, 28);

        ArrayList<Task> tasksOnDate = taskList.findTasksByDate(target);
        assertTrue(tasksOnDate.isEmpty());
    }
}

