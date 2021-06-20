package lab.response;

import java.io.Serializable;

public class ClearResponse implements Serializable, Response {
    private final int elementsRemovedCount;

    public ClearResponse(int elementsRemovedCount) {
        this.elementsRemovedCount = elementsRemovedCount;
    }

    public int getElementsRemovedCount() {
        return elementsRemovedCount;
    }
}
