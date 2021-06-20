package lab.auth;

import java.util.Optional;

public class AuthorizationManager {
    private Optional<Credentials> credentials = Optional.empty();

    public boolean isAuthorized() {
        return credentials.isPresent();
    }

    public void authorize(Credentials credentials) {
        this.credentials = Optional.of(credentials);
    }

    public Optional<Credentials> getCredentials() {
        return credentials;
    }

    public void logout() {
        credentials = Optional.empty();
    }
}
