package lab.response;

import lab.auth.Credentials;
import lab.commands.Command;

import java.io.Serializable;

public class AuthorizationFailedResponse implements Serializable, Response {
    private final Command command;
    private final Credentials credentials;

    public AuthorizationFailedResponse(Command command, Credentials credentials) {
        this.command = command;
        this.credentials = credentials;
    }

    public Command getCommand() {
        return command;
    }

    public Credentials getCredentials() {
        return credentials;
    }
}
