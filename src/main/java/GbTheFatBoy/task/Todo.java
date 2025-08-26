package GbTheFatBoy.task;

public class Todo extends Task {

    public Todo(String description) {
        super(description);
    }

    public Todo(String desc, boolean isDone) {
        super(desc, isDone);
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }
}
