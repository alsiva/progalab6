package commands;

public class RemoveAllByStudentsCountCommand {
    private final Long count;

    public RemoveAllByStudentsCountCommand(Long count) {
        this.count = count;
    }

    public Long getCount() {
        return count;
    }
}
