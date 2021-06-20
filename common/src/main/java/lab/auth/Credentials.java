package lab.auth;

import lab.domain.FailedToParseException;

import java.io.Serializable;
import java.util.Objects;

public class Credentials implements Serializable {
    public final String username;
    public final String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credentials that = (Credentials) o;
        return username.equals(that.username) && password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    private static String readCredential(String fieldAsString, String credentialName) throws FailedToParseException {
        String username = fieldAsString.trim();

        if (username.trim().isEmpty()) {
            throw new FailedToParseException(credentialName + " should not be blank");
        }

        return username;
    }

    public static String readUsername(String fieldAsString) throws FailedToParseException {
        return readCredential(fieldAsString, "Username");
    }

    public static String readPassword(String fieldAsString) throws FailedToParseException {
        return readCredential(fieldAsString, "Password");
    }
}
