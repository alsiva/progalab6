package lab.ui;

import lab.ConnectionManagerClient;
import lab.auth.Credentials;
import lab.commands.Command;
import lab.commands.Request;
import lab.response.Response;

import java.io.IOException;

public abstract class AbstractCommandController {

    protected Credentials credentials;
    protected ConnectionManagerClient connectionManager;

    public void setConnectionManager(ConnectionManagerClient connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    protected Response getResponse(Command command) throws IOException {
        try {
            connectionManager.sendRequest(new Request(command, credentials));
        } catch (IOException e) {
            System.err.println("Failed to send command " + e.getMessage());
        }

        try {
            return connectionManager.receiveResponse();
        } catch (ClassNotFoundException e) {
            throw new IOException("Failed to deserialize response", e);
        }
    }
}
