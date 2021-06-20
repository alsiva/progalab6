package lab.commands;

import lab.auth.Credentials;

import java.io.Serializable;

public class RegisterCommand implements Serializable, Command {
    private final Credentials credentials;

    public RegisterCommand(Credentials credentials) {
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public String toPrint() {
        return "Register user " + credentials.username;
    }
}
