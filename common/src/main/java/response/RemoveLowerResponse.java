package response;

import domain.StudyGroup;

import java.io.Serializable;
import java.util.List;

public class RemoveLowerResponse implements Serializable, Response {
    private final List<StudyGroup> groups;

    public RemoveLowerResponse(List<StudyGroup> groups) {
        this.groups = groups;
    }

    public List<StudyGroup> getGroups() {
        return groups;
    }
}
