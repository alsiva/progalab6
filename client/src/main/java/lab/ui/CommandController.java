package lab.ui;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lab.ConnectionManagerClient;
import lab.auth.Credentials;
import lab.commands.ClearCommand;
import lab.commands.Command;
import lab.commands.Request;
import lab.response.ClearResponse;
import lab.response.Response;

import java.io.IOException;

public class CommandController {
    private Credentials credentials;
    private ConnectionManagerClient connectionManager;
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    public void setConnectionManager(ConnectionManagerClient connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void clear() throws IOException {
        Command command = new ClearCommand();
        try {
            connectionManager.sendRequest(new Request(command, credentials));
        } catch (IOException e) {
            System.err.println("Failed to send command " + e.getMessage());
        }

        Response response;
        try {
            response = connectionManager.receiveResponse();
        } catch (IOException | ClassNotFoundException e) {
            Pages.showModal(primaryStage.getOwner(), "Failed to get response from server");
            return;
        }

        if (!(response instanceof ClearResponse)) {
            Pages.showModal(primaryStage.getOwner(), "Unknown command from server");
            return;
        }

        int removedCount = ((ClearResponse) response).getElementsRemovedCount();
        String message = "Collection was cleared; " + removedCount + " elements were removed";

        Pages.showModal(primaryStage, message);
    }

    public void logOut() throws IOException {
        Pages.openLoginPage(primaryStage, connectionManager);
    }
}
