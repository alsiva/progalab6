package commands;
import domain.StudyGroup;

import java.io.Serializable;

public class RemoveLowerCommand implements Serializable, Command {

    private final StudyGroup studyGroup;

    public RemoveLowerCommand(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

}