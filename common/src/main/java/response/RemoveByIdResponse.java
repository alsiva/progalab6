package response;

import java.io.Serializable;

public class RemoveByIdResponse implements Serializable, Response {
    private final boolean wasRemoved;

    public RemoveByIdResponse(boolean wasRemoved) {
        this.wasRemoved = wasRemoved;
    }

    public boolean getWasRemoved() {
        return wasRemoved;
    }
}
