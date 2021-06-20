package lab.commands;

import lab.auth.Credentials;

import java.io.Serializable;

public class CheckCredentialsCommand implements Serializable, Command {
    private final Credentials credentials;

    public CheckCredentialsCommand(Credentials credentials) {
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public String toPrint() {
        return "Log user " + credentials.username + " in";
    }
}
