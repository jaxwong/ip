import java.util.Scanner;
import java.util.ArrayList;

public class GbTheFatBoy {

    public static final String NAME = "GbTheFatBoy";
    public static final String MESSAGE = "I'm smelly meow meow!";
    public static final String LINE = "_".repeat(60);
    public static final ArrayList<String> taskList = new ArrayList<String>();

    public static void main(String[] args) {
        greet();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String command = scanner.nextLine();
            if (command.equals("bye")) break;
            else if (command.equals("list")) printList(taskList);
            else {
                taskList.add(command);
                echo(command);
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
    }

    public static void echo(String command) {
        System.out.println(LINE);
        System.out.println("added: " + command);
        System.out.println(LINE);
    }

    public static void bye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    public static void printList(ArrayList<String> taskList) {
        System.out.println(LINE);
        int length = taskList.size();
        for (int i = 0; i < length; i++) {
            System.out.println((i+1) + ". " + taskList.get(i));
        }
        System.out.println(LINE);
    }
}
