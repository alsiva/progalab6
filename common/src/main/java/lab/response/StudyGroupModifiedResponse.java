package lab.response;

import lab.domain.StudyGroup;

import java.io.Serializable;

public class StudyGroupModifiedResponse implements Serializable, Response {

    private final StudyGroup studyGroup;

    public StudyGroupModifiedResponse(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }
}
