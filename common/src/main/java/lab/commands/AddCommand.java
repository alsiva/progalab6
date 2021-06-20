package lab.commands;

import lab.domain.StudyGroup;

import java.io.Serializable;

public class AddCommand implements Serializable, Command {
    private final StudyGroup group;

    public AddCommand(StudyGroup group) {
        this.group = group;
    }

    public StudyGroup getGroup() {
        return group;
    }

    @Override
    public String toPrint() {
        return "Add study group";
    }
}
