package lab.response;

import java.io.Serializable;

public class CountByGroupAdminResponse implements Serializable, Response {
    private final long count;

    public CountByGroupAdminResponse(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }
}
