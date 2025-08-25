import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TaskList {

    private ArrayList<Task> taskList = new ArrayList<>();

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.taskList = tasks;
    }

    public void add(Task task) throws GBException {
        if (task.getDescription().isEmpty()) {
            throw new GBException("Invalid description: task description cannot be " +
                    "empty!");
        }
        this.taskList.add(task);
    }

    public ArrayList<Task> getTasks() {
        return this.taskList;
    }

    public int getSize() {
        return this.taskList.size();
    }

    // helper that throws GBException instead of IndexOutOfBoundsException
    public Task getTask(int index) throws GBException {
        try {
            return this.taskList.get(index - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new GBException(e.getMessage());
        }
    }

    // exception handled by executeCommand
    public void mark(int index) throws GBException {
        getTask(index).mark();
    }

    public void unmark(int index) throws GBException {
        getTask(index).unmark();
    }

    public Task delete(int index) throws GBException {
        if (index < 1 || index > this.taskList.size()) {
            throw new GBException("Invalid task index");
        }
        return this.taskList.remove(index - 1);
    }

    public ArrayList<Task> findTasksByDate(LocalDate targetDate) {
        ArrayList<Task> tasksOnDate = new ArrayList<>();

        for (Task task : taskList) {
            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                if (deadline.getDeadline().toLocalDate().equals(targetDate)) tasksOnDate.add(deadline);
            } else if (task instanceof Event) {
                Event event = (Event) task;
                LocalDate eventStartDate = event.getStartDateTime().toLocalDate();
                LocalDate eventEndDate = event.getEndDateTime().toLocalDate();

                if (targetDate.equals(eventStartDate)
                        || targetDate.equals(eventEndDate)
                        || targetDate.isAfter(eventStartDate) && targetDate.isBefore(eventEndDate)) {
                    tasksOnDate.add(event);
                }
            }
        }

        if (tasksOnDate.isEmpty()) {
            System.out.println("No tasks found on this date");
            return new ArrayList<Task>();
        } else {
            return tasksOnDate;
        }

    }

}
