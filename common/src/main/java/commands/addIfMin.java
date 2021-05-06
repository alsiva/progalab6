package commands;

import domain.StudyGroup;

import java.util.Set;

public class addIfMin {

    private final StudyGroup other;
    private final Set<StudyGroup> groups;

    public addIfMin(StudyGroup other, Set<StudyGroup> groups) {
        this.other = other;
        this.groups = groups;
    }

    public void execute() {
        StudyGroup min = null;
        for (StudyGroup studyGroup: groups) {
            if (min == null || studyGroup.getStudentsCount() < min.getStudentsCount()) {
                min = studyGroup;
            }
        }

        if (min == null || other.getStudentsCount() < min.getStudentsCount()) {
            groups.add(other);
        }
    }
}
