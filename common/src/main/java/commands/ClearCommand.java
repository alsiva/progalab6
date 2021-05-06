package commands;

import domain.StudyGroup;

import java.util.Set;

public class ClearCommand {

    private final Set<StudyGroup> groups;

    public ClearCommand(Set<StudyGroup> groups) {
        this.groups = groups;
    }

    public void execute() {
        groups.clear();
    }
}
