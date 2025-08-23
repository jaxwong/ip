public class Deadline extends Task {

    private String date;

    public Deadline(String description, String date) {
        super(description);
        this.date = date;
    }

    public Deadline(String desc, boolean isDone, String date) {
        super(desc, isDone);
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.date);
    }
}
