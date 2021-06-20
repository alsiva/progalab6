package lab.response;

import java.io.Serializable;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class AddResponse implements Serializable, Response {
    private final Optional<Long> studyGroupId;

    public AddResponse(Optional<Long> studyGroupId) {
        this.studyGroupId = studyGroupId;
    }

    public Optional<Long> getStudyGroupId() {
        return studyGroupId;
    }
}
