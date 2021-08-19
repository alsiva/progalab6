package lab.auth;

import java.util.Optional;

public class AuthorizationManager {
    private Credentials credentials = null;

    public boolean isAuthorized() {
        return credentials != null;
    }

    public void authorize(Credentials credentials) {
        this.credentials = credentials;
    }

    public Optional<Credentials> getCredentials() {
        return Optional.ofNullable(credentials);
    }

    public void logout() {
        credentials = null;
    }
}
