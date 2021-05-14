package response;

import java.io.Serializable;

public class UpdateIdResponse implements Serializable, Response {
    private final boolean wasUpdated;

    public UpdateIdResponse(boolean wasUpdated) {
        this.wasUpdated = wasUpdated;
    }

    public boolean getWasUpdated() {
        return wasUpdated;
    }
}
