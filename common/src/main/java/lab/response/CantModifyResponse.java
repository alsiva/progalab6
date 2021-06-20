package lab.response;

import java.io.Serializable;

public class CantModifyResponse implements Serializable, Response {
    private final long studyGroupId;

    public CantModifyResponse(long studyGroupId) {
        this.studyGroupId = studyGroupId;
    }

    public long getStudyGroupId() {
        return studyGroupId;
    }
}
