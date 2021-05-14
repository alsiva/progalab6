package response;

import domain.StudyGroup;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

public class InfoResponse implements Serializable, Response {
    private final Set<StudyGroup> groups;
    private final Instant creationDate = Instant.now();

    public InfoResponse(Set<StudyGroup> groups) {
        this.groups = groups;
    }

    public Set<StudyGroup> getGroups() {
        return groups;
    }

    public Instant getCreationDate() {
        return creationDate;
    }
}
