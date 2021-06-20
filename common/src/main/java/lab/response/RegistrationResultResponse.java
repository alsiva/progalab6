package lab.response;

import lab.auth.Credentials;

import java.io.Serializable;

public class RegistrationResultResponse implements Serializable, Response {
    private final boolean isSuccessful;
    private final Credentials credentials;

    public RegistrationResultResponse(boolean isSuccessful, Credentials credentials) {
        this.isSuccessful = isSuccessful;
        this.credentials = credentials;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }


    public Credentials getCredentials() {
        return credentials;
    }
}
