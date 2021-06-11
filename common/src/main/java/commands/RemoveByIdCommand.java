package commands;

import java.io.Serializable;

public class RemoveByIdCommand implements Serializable, Command {
    private final long id;

    public RemoveByIdCommand(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toPrint() {
        return "Remove group with id " + id;
    }
}
