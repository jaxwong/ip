import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TaskList {

    private ArrayList<Task> taskList = new ArrayList<>();
    private final Storage storage;

    public TaskList(Storage storage) {
        this.storage = storage;
        this.taskList = storage.loadTasks();
    }

    public void add(Task task) {
        System.out.println(GbTheFatBoy.LINE);
        if (task.getDescription().isEmpty()) {
            System.out.println("Task description " +
                "cannot be empty!");
            System.out.println(GbTheFatBoy.LINE);
            System.out.println();
            return;
        }
        this.taskList.add(task);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println(String.format("Now you have %d tasks in the list.",
                taskList.size()));
        System.out.println(GbTheFatBoy.LINE);
        System.out.println();

        storage.saveTasks(taskList);
    }

    public void print() {
        System.out.println(GbTheFatBoy.LINE);
        System.out.println("Here are the tasks in your list:");
        int length = taskList.size();
        for (int i = 0; i < length; i++) {
            System.out.println((i+1) + ". " + taskList.get(i));
        }
        System.out.println(GbTheFatBoy.LINE);
    }

    public void mark(int index) {
        System.out.println(GbTheFatBoy.LINE);
        try {
            this.taskList.get(index - 1).mark();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println(this.taskList.get(index - 1));

            storage.saveTasks(taskList);

        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid index!");
            if (this.taskList.isEmpty()) System.out.println("There are no tasks in your" +
                    " list!");
            else System.out.println("Please enter a number from 1 to " + this.taskList.size() + " to mark");
        }
        System.out.println(GbTheFatBoy.LINE);

    }

    public void unmark(int index) {
        System.out.println(GbTheFatBoy.LINE);
        try {
            this.taskList.get(index - 1).unmark();
            System.out.println("Ok, I've marked this task as not done yet:");
            System.out.println(this.taskList.get(index - 1));

            storage.saveTasks(taskList);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid index!");
            if (this.taskList.isEmpty()) System.out.println("There are no tasks in your" +
                    " list!");
            else System.out.println("Please enter a number from 1 to " + this.taskList.size() + " to unmark");
        }

        System.out.println(GbTheFatBoy.LINE);
    }

    public void delete(int index) {
        System.out.println(GbTheFatBoy.LINE);
        try {
            Task removed = this.taskList.remove(index - 1);
            System.out.println("Noted. I've removed this task:");
            System.out.println(removed);
            System.out.println("Now you have " + this.taskList.size() + " tasks in the " +
                    "list.");

            storage.saveTasks(taskList);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid index!");
            if (this.taskList.isEmpty()) System.out.println("There are no tasks in your" +
                    " list!");
            else System.out.println("Please enter a number from 1 to " + this.taskList.size() + " to delete");
        }
    }

    public void findTasksByDate(LocalDate targetDate) {
        System.out.println(GbTheFatBoy.LINE);
        System.out.println("Tasks on " + targetDate.format(DateTimeFormatter.ofPattern(
                "MMM dd yyyy")) + ":");

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
        } else {
            for (int i = 0; i < tasksOnDate.size(); i++) {
                System.out.println(String.format("%d. %s", (i+1),
                        tasksOnDate.get(i)));
            }
        }

        System.out.println(GbTheFatBoy.LINE);
    }

}
