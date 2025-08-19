public class Task {

    private final String description;
    private boolean isDone = false;

    public Task(String description) {
        this.description = description;
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
