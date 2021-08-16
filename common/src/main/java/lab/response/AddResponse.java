package lab.response;

import java.io.Serializable;
import java.util.Optional;

public class AddResponse implements Serializable, Response {
    private final Long studyGroupId;

    public AddResponse(Long studyGroupId) {
        this.studyGroupId = studyGroupId;
    }

    public Optional<Long> getStudyGroupId() {
        return Optional.ofNullable(studyGroupId);
    }
}
