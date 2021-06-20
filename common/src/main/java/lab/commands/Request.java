package lab.commands;

import lab.auth.Credentials;

import java.io.Serializable;

public class Request implements Serializable {
    public final Command command;
    public final Credentials credentials;

    public Request(Command command, Credentials credentials) {
        this.command = command;
        this.credentials = credentials;
    }
}
