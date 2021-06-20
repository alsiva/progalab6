package lab.response;

import lab.auth.Credentials;

import java.io.Serializable;

public class CheckCredentialsResponse implements Serializable, Response {
    private final boolean isAuthorized;
    private final Credentials credentials;

    public CheckCredentialsResponse(boolean isAuthorized, Credentials credentials) {
        this.isAuthorized = isAuthorized;
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }
}
