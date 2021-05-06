package response;

import domain.StudyGroup;

import java.io.Serializable;

public class AddResponse implements Serializable, Response {
    private final StudyGroup group;

    public AddResponse(StudyGroup group) {
        this.group = group;
    }

    public StudyGroup getGroup() {
        return group;
    }
}
