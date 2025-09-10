package gbthefatboy.command;

public class Tag {
    private final int index;
    private final String tagMessage;

    public Tag(int index, String message) {
        this.index = index;
        this.tagMessage = message;
    }

    public int getIndex() {
        return this.index;
    }

    public String getTagMessage() {
        return this.tagMessage;
    }
}
