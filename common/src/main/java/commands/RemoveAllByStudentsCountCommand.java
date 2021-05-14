package commands;

import java.io.Serializable;

public class RemoveAllByStudentsCountCommand implements Serializable, Command {
    private final Long count;

    public RemoveAllByStudentsCountCommand(Long count) {
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    @Override
    public String toPrint() {
        return "Remove all groups that have " + count + " students";
    }
}
