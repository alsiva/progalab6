package commands;

import java.io.Serializable;

public class ClearCommand implements Serializable, Command {
    @Override
    public String toPrint() {
        return "Clear all groups";
    }
}
