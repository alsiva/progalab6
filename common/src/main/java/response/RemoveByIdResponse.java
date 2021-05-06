package response;

import domain.StudyGroup;

import java.io.Serializable;

public class RemoveByIdResponse implements Serializable, Response {
    private final StudyGroup studyGroup;

    public RemoveByIdResponse(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }
}
