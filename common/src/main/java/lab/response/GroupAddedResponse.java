package lab.response;

import lab.domain.StudyGroup;

import java.io.Serializable;

public class GroupAddedResponse implements Response, Serializable {

    private final StudyGroup studyGroup;

    public GroupAddedResponse(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }
}
