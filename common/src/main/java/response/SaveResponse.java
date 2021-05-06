package response;

import domain.StudyGroup;

import java.io.Serializable;
import java.util.Set;

public class SaveResponse implements Serializable, Response {
    private final Set<StudyGroup> groups;

    public SaveResponse(Set<StudyGroup> groups) {
        this.groups = groups;
    }

    public Set<StudyGroup> getGroups() {
        return groups;
    }
}
