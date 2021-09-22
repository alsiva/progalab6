package lab.response;


import java.io.Serializable;
import java.util.Set;

public class GroupRemovedResponse implements Response, Serializable {

    private final Set<Long> removedGroups;

    public GroupRemovedResponse(Set<Long> removedGroups) {
        this.removedGroups = removedGroups;
    }

    public Set<Long> getRemovedGroups() {
        return removedGroups;
    }
}
