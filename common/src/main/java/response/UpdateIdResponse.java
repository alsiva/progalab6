package response;

import domain.StudyGroup;

import java.io.Serializable;

public class UpdateIdResponse implements Serializable, Response {
    private StudyGroup group;

    public UpdateIdResponse(StudyGroup group) {
        this.group = group;
    }

    public StudyGroup getGroup() {
        return group;
    }
}
