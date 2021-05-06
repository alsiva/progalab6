package response;

import domain.StudyGroup;

import java.io.Serializable;

public class AddIfMinResponse implements Serializable, Response {
    private final StudyGroup studyGroup;

    public AddIfMinResponse(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }
}
