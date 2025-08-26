package GbTheFatBoy.task;

public class Task {

    private final String description;
    private boolean isDone = false;

    public Task(String description) {
        this.description = description;
    }

    public Task(String desc, boolean isDone) {
        this.description = desc;
        this.isDone = isDone;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        String check = isDone ? "X" : " ";
        return String.format("[%s] %s", check, this.description);
    }
}
