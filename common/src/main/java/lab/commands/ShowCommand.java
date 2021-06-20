package lab.commands;

import java.io.Serializable;

public class ShowCommand implements Serializable, Command {
    @Override
    public String toPrint() {
        return "Display study groups";
    }
}
