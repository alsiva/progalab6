package commands;

import domain.StudyGroup;

import java.io.Serializable;

public class AddIfMinCommand implements Serializable, Command {
    private final StudyGroup studyGroup;

    public AddIfMinCommand(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }
}
