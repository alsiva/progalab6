package lab.response;

import lab.domain.StudyGroup;

import java.io.Serializable;

public class GroupChangedResponse implements Response, Serializable {

    private final StudyGroup studyGroup;

    public GroupChangedResponse(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }
}
