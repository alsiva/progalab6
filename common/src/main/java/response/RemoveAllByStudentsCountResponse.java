package response;

import domain.StudyGroup;

import java.io.Serializable;
import java.util.Set;

public class RemoveAllByStudentsCountResponse implements Serializable, Response {
    private final Set<StudyGroup> removedGroups;

    public RemoveAllByStudentsCountResponse(Set<StudyGroup> removedGroups) {
        this.removedGroups = removedGroups;
    }

    public Set<StudyGroup> getRemovedGroups() {
        return removedGroups;
    }
}
