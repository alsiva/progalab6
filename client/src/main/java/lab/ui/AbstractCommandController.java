package lab.ui;

import javafx.stage.Stage;
import lab.ConnectionManagerClient;
import lab.auth.Credentials;
import lab.commands.Command;
import lab.commands.Request;
import lab.response.Response;

import java.io.IOException;

public abstract class AbstractCommandController {

    protected Credentials credentials;
    protected ConnectionManagerClient connectionManager;
    protected Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

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

        Response response;
        try {
            response = connectionManager.receiveResponse();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            Pages.openInfoModal(primaryStage, "Failed to get response from server");
            return null;
        }
    }
}
