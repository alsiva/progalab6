package response;

import java.io.Serializable;

public class HelpResponse implements Serializable, Response {

    private final String response;

    public HelpResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

}
