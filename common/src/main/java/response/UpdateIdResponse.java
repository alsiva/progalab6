package response;

import domain.StudyGroup;

import java.io.Serializable;
import java.util.Optional;

public class UpdateIdResponse implements Serializable, Response {
    private Optional<StudyGroup> group;

    public UpdateIdResponse(Optional<StudyGroup> group) {
        this.group = group;
    }

    public Optional<StudyGroup> getGroup() {
        return group;
    }
}
