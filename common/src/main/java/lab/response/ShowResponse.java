package lab.response;

import lab.domain.StudyGroup;

import java.io.Serializable;
import java.util.List;

public class ShowResponse implements Serializable, Response {
    private final List<StudyGroup> groups;

    public ShowResponse(List<StudyGroup> groups) {
        this.groups = groups;
    }

    public List<StudyGroup> getGroups() {
        return groups;
    }
}
