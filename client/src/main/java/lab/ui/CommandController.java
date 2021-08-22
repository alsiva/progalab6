package lab.ui;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lab.ConnectionManagerClient;
import lab.auth.Credentials;
import lab.commands.ClearCommand;
import lab.commands.Command;
import lab.commands.Request;
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

    public void clear() {
        Command command = new ClearCommand();
        try {
            connectionManager.sendRequest(new Request(command, credentials));
        } catch (IOException e) {
            System.err.println("Failed to send command " + e.getMessage());
        }

        try {
            Response response = connectionManager.receiveResponse();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to receive response " + e.getMessage());
        }

        Stage answer = new Stage();
        answer.initModality(Modality.APPLICATION_MODAL);
        answer.showAndWait();

    }

    public void logOut() throws IOException {
        Pages.openLoginPage(primaryStage, connectionManager);
    }
}
