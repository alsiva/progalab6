package response;

import domain.StudyGroup;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class RemoveAllByStudentsCountResponse implements Serializable, Response {
    private final List<StudyGroup> removedGroups;

    public RemoveAllByStudentsCountResponse(List<StudyGroup> removedGroups) {
        this.removedGroups = removedGroups;
    }

    public List<StudyGroup> getRemovedGroups() {
        return removedGroups;
    }
}
