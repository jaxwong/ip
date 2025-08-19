import java.util.Scanner;

public class GbTheFatBoy {

    public static final String NAME = "GbTheFatBoy";
    public static final String MESSAGE = "I'm smelly meow meow!";
    public static final String LINE = "_".repeat(60);

    public static void main(String[] args) {
        greet();
        TaskList taskList = new TaskList();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String description = scanner.nextLine();
            if (description.equals("bye")) break;
            else if (description.equals("list")) taskList.print();
            else if (description.startsWith("mark")) {
                String[] parts = description.split(" ");
                String command = parts[0];
                int index = Integer.parseInt(parts[1]);
                if (command.equalsIgnoreCase("mark") && index >= 1) {
                    taskList.mark(index);
                }
            } else if (description.startsWith("unmark")) {
                String[] parts = description.split(" ");
                String command = parts[0];
                int index = Integer.parseInt(parts[1]);
                if (command.equalsIgnoreCase("unmark") && index >= 1) {
                    taskList.unmark(index);
                }
            } else {
                taskList.add(new Task(description));
            }
        }
        bye();
    }

    public static void greet() {
        System.out.println(LINE);
        System.out.println("Hello! I'm " + NAME);
        System.out.println(MESSAGE);
        System.out.println("What can I do for you?");
        System.out.println(LINE);
        System.out.println();
    }

    public static void bye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

}
