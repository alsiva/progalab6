package commands;

import java.io.Serializable;

public class HelpCommand implements Serializable, Command {
    @Override
    public String toPrint() {
        return "Display help";
    }
}
