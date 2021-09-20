package lab.commands;

import java.io.Serializable;

public class UpdateGroupsToOtherClientsCommand implements Serializable, Command {
    @Override
    public String toPrint() {
        return "Update studyGroups to other clients";
    }
}
