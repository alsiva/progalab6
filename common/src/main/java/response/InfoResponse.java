package response;

import java.io.Serializable;

public class InfoResponse implements Serializable, Response {
    private final String response;

    public InfoResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
