package response;
import domain.StudyGroup;

import java.io.Serializable;
import java.util.Set;

public class ShowResponse implements Serializable, Response {
    private final Set<StudyGroup> groups;

    public ShowResponse(Set<StudyGroup> groups) {
        this.groups = groups;
    }

    public Set<StudyGroup> getGroups() {
        return groups;
    }
}
