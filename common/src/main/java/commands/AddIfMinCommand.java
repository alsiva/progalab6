package commands;

import domain.StudyGroup;

public class AddIfMinCommand {
    private final StudyGroup studyGroup;

    public AddIfMinCommand(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }
}
