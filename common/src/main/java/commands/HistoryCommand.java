package commands;

import java.io.Serializable;

public class HistoryCommand implements Serializable, Command {
    @Override
    public String toPrint() {
        return "Display command history";
    }
}
