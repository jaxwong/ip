import java.util.Scanner;

public class GbTheFatBoy {

    public static final String NAME = "GbTheFatBoy";
    public static final String MESSAGE = "I'm smelly meow meow!";
    public static final String LINE = "_".repeat(60);

    public static void main(String[] args) {
        greet();
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        while (!command.equals("bye")) {
            echo(command);
            command = scanner.nextLine();
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
        System.out.println(command);
        System.out.println(LINE);
    }

    public static void bye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
