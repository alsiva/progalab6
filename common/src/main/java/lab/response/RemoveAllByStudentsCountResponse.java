package lab.response;

import lab.domain.StudyGroup;

import java.io.Serializable;
import java.util.List;

public class RemoveAllByStudentsCountResponse implements Serializable, Response {
    private final List<StudyGroup> removedGroups;

    public RemoveAllByStudentsCountResponse(List<StudyGroup> removedGroups) {
        this.removedGroups = removedGroups;
    }

    public List<StudyGroup> getRemovedGroups() {
        return removedGroups;
    }
}
