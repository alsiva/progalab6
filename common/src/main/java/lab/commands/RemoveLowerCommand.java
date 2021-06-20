package lab.commands;
import lab.domain.StudyGroup;

import java.io.Serializable;

public class RemoveLowerCommand implements Serializable, Command {

    private final StudyGroup studyGroup;

    public RemoveLowerCommand(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    @Override
    public String toPrint() {
        return "Remove study groups that have less students then " + studyGroup.getStudentsCount();
    }
}