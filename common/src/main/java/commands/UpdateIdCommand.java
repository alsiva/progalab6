package commands;

import domain.StudyGroup;

import java.io.Serializable;

public class UpdateIdCommand implements Serializable, Command {
    private final StudyGroup group;

    public UpdateIdCommand(StudyGroup group) {
        this.group = group;
    }

    public StudyGroup getGroup() {
        return group;
    }

    @Override
    public String toPrint() {
        return "Update study group with id " + group.getId();
    }
}
