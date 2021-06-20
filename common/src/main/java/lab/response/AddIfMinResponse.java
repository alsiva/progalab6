package lab.response;

import java.io.Serializable;

public class AddIfMinResponse implements Serializable, Response {
    private final Boolean wasAdded;

    public AddIfMinResponse(Boolean wasAdded) {
        this.wasAdded = wasAdded;
    }

    public Boolean getWasAdded() {
        return wasAdded;
    }
}
