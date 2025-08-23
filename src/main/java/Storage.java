import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Storage {
    private final String filePath;

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

    public ArrayList<Task> loadTasks() {
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
                if (task != null) tasks.add(task);
            }
            System.out.println("Loaded " + tasks.size() + " tasks from storage");
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        return tasks;
    }

    public void saveTasks(ArrayList<Task> tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
            for (Task task: tasks) {
                writer.write(formatTask(task));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private String formatTask(Task task) {
        String isDone = task.isDone() ? "1" : "0";

        if (task instanceof Todo) {
            return String.format("T | %s | %s", isDone, task.getDescription());
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.format("D | %s | %s | %s", isDone, deadline.getDescription(),
                    deadline.getDate());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return String.format("E | %s | %s | %s | %s", isDone,
                    event.getDescription(), event.getStartDate(), event.getEndDate());
        }

        return "";
    }

    private Task parseTask(String line) {
        try {
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
                        String date = parts[3].trim();
                        task = new Deadline(description, isDone, date);
                    }
                    break;
                case "E":
                    if (parts.length == 5) {
                        String startDate = parts[3].trim();
                        String endDate = parts[4].trim();
                        task = new Event(description, isDone, startDate, endDate);
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
