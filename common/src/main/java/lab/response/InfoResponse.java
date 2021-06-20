package lab.response;

import java.io.Serializable;
import java.time.Instant;

public class InfoResponse implements Serializable, Response {
    private final Class<?> collectionType;
    private final int collectionSize;
    private final Instant creationDate = Instant.now();

    public InfoResponse(Class<?> collectionType, int collectionSize) {
        this.collectionType = collectionType;
        this.collectionSize = collectionSize;
    }

    public Class<?> getCollectionType() {
        return collectionType;
    }

    public int getCollectionSize() {
        return collectionSize;
    }

    public Instant getCreationDate() {
        return creationDate;
    }
}
