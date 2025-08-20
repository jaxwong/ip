import java.util.ArrayList;

public class TaskList {

    private ArrayList<Task> taskList = new ArrayList<>();

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
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid index!");
            if (this.taskList.isEmpty()) System.out.println("There are no tasks in your" +
                    " list!");
            else System.out.println("Please enter a number from 1 to " + this.taskList.size() + " to unmark");
        }

        System.out.println(GbTheFatBoy.LINE);
    }

}
