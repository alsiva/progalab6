package commands;

public class RemoveByIdCommand {
    private final Long id;

    public RemoveByIdCommand(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
