package lab.commands;

import java.io.Serializable;

public class RemoveAllByStudentsCountCommand implements Serializable, Command {
    private final long count;

    public RemoveAllByStudentsCountCommand(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    @Override
    public String toPrint() {
        return "Remove all groups that have " + count + " students";
    }
}
