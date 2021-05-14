package commands;

import java.io.Serializable;

public class RemoveByIdCommand implements Serializable, Command {
    private final Long id;

    public RemoveByIdCommand(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toPrint() {
        return "Remove group with id " + id;
    }
}
