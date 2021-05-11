package response;

import domain.StudyGroup;

import java.io.Serializable;
import java.util.Optional;

public class RemoveByIdResponse implements Serializable, Response {
    private final Optional<StudyGroup> studyGroup;

    public RemoveByIdResponse(Optional<StudyGroup> studyGroup) {
        this.studyGroup = studyGroup;
    }

    public Optional<StudyGroup> getStudyGroup() {
        return studyGroup;
    }
}
