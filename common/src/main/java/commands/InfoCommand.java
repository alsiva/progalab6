package commands;

import java.io.Serializable;

public class InfoCommand implements Command, Serializable {
    @Override
    public String toPrint() {
        return "Display info about server collection";
    }
}
