package lab.commands;

import lab.auth.Credentials;

import java.io.Serializable;

public class LogoutCommand implements Serializable, Command {

    private final Credentials credentials;

    public LogoutCommand(Credentials credentials) {this.credentials = credentials;}

    public Credentials getCredentials() {return credentials;}

    @Override
    public String toPrint() {
        return "Update studyGroups to other clients";
    }
}
