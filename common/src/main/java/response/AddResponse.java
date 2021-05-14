package response;

import java.io.Serializable;

public class AddResponse implements Serializable, Response {
    private final long studyGroupId;

    public AddResponse(long studyGroupId) {
        this.studyGroupId = studyGroupId;
    }

    public long getStudyGroupId() {
        return studyGroupId;
    }
}
