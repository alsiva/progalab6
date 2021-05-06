package response;

import java.io.Serializable;

public class CountByGroupAdminResponse implements Serializable, Response {
    private int count;

    public CountByGroupAdminResponse(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
