import java.util.ArrayList;

public class TaskList {

    private ArrayList<Task> taskList = new ArrayList<>();

    public void add(Task task) {
        System.out.println(GbTheFatBoy.LINE);
        this.taskList.add(task);
        System.out.println("added: "+ task.getDescription());
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
        this.taskList.get(index - 1).mark();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(this.taskList.get(index - 1));
        System.out.println(GbTheFatBoy.LINE);
    }

    public void unmark(int index) {
        System.out.println(GbTheFatBoy.LINE);
        this.taskList.get(index - 1).unmark();
        System.out.println("Ok, I've marked this task as not done yet:");
        System.out.println(this.taskList.get(index - 1));
        System.out.println(GbTheFatBoy.LINE);
    }

}
