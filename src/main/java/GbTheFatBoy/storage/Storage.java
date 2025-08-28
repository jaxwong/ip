package GbTheFatBoy.storage;

import GbTheFatBoy.task.Task;
import GbTheFatBoy.task.Todo;
import GbTheFatBoy.task.Deadline;
import GbTheFatBoy.task.Event;

import GbTheFatBoy.exception.GBException;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

/**
 * Handles file-based storage and retrieval of tasks.
 * Manages reading from and writing to a persistent storage file.
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a new Storage instance with the specified file path.
     * Creates the directory structure if it doesn't exist.
     *
     * @param filePath The file path where tasks will be stored.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
        createDirectoryIfNotExists();
    }

    private void createDirectoryIfNotExists() {
        try {
            Path path = Paths.get(filePath);
            Path parentDir = path.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
        } catch (IOException e) {
            System.err.println("Error creating directory: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the storage file.
     * If the file doesn't exist, returns an empty task list.
     *
     * @return An ArrayList containing all loaded tasks.
     * @throws GBException If there's an error reading from the file.
     */
    public ArrayList<Task> loadTasks() throws GBException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("TodoList file not found! Starting with empty task list");
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            System.out.println("Loaded " + tasks.size() + " tasks from storage");
        } catch (IOException e) {
            throw new GBException("Error reading from file: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Saves the provided list of tasks to the storage file.
     * Overwrites the existing file content.
     *
     * @param tasks The list of tasks to save.
     * @throws GBException If there's an error writing to the file.
     */
    public void saveTasks(ArrayList<Task> tasks) throws GBException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
            for (Task task: tasks) {
                writer.write(formatTask(task));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new GBException("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Formats the provided task into a standard task format for interpretation by parser
     *
     * @param task The task object to format
     * @return the formatted task string
     */
    private String formatTask(Task task) {
        String isDone = task.isDone() ? "1" : "0";

        if (task instanceof Todo) {
            return String.format("T | %s | %s", isDone, task.getDescription());
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.format("D | %s | %s | %s", isDone, deadline.getDescription(),
                    deadline.getDeadlineForStorage());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return String.format("E | %s | %s | %s | %s", isDone,
                    event.getDescription(), event.getStartDateTimeForStorage(),
                    event.getEndDateTimeForStorage());
        }

        return "";
    }

    private Task parseTask(String line) {
        try {
            if (line.isEmpty()) return null;
            String[] parts = line.split(" \\| ");

            if (parts.length < 3) {
                System.err.println("Corrupted data line: " + line);
                return null;
            }

            String type = parts[0].trim();
            boolean isDone = parts[1].trim().equals("1");
            String description = parts[2].trim();

            Task task = null;
            switch(type) {
            case "T":
                if (parts.length == 3) {
                    task = new Todo(description, isDone);
                }
                break;
            case "D":
                if (parts.length == 4) {
                    String dateTimeStr = parts[3].trim();
                    try {
                        LocalDateTime deadline = LocalDateTime.parse(dateTimeStr,
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        task = new Deadline(description, isDone, deadline);
                    } catch (Exception e) {
                        System.err.println("Error parsing deadline date: " + dateTimeStr);
                        return null;
                    }
                }
                break;
            case "E":
                if (parts.length == 5) {
                    String startDateStr = parts[3].trim();
                    String endDateStr = parts[4].trim();
                    try {
                        LocalDateTime startDateTime = LocalDateTime.parse(startDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        LocalDateTime endDateTime = LocalDateTime.parse(endDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        task = new Event(description, isDone, startDateTime,
                                endDateTime);
                    } catch (Exception e) {
                        System.err.println("Error parsing event dates: " + startDateStr + ", " + endDateStr);
                        return null;
                    }
                }
                break;
            default:
                System.err.println("Unknown task type: " + type);
                return null;
            }
            return task;
        } catch (Exception e) {
            System.err.println("Error parsing task: " + line + " - " + e.getMessage());
            return null;
        }
    }
}
