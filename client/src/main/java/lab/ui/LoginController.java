package lab.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lab.ConnectionManagerClient;
import lab.auth.Credentials;
import lab.commands.CheckCredentialsCommand;
import lab.commands.RegisterCommand;
import lab.commands.Request;
import lab.response.CheckCredentialsResponse;
import lab.response.Response;

import java.io.IOException;

public class LoginController {
    private Stage primaryStage;
    private ConnectionManagerClient connectionManager;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setConnectionManager(ConnectionManagerClient connectionManager) {
        this.connectionManager = connectionManager;
    }

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    public void logIn() throws IOException, ClassNotFoundException {
        Credentials credentials = getCredentials();
        CheckCredentialsCommand loginCommand = new CheckCredentialsCommand(credentials);
        connectionManager.sendRequest(new Request(loginCommand, credentials));
        Response response = connectionManager.receiveResponse();

        if (response instanceof CheckCredentialsResponse) {
            CheckCredentialsResponse checkCredentialsResponse = (CheckCredentialsResponse) response;

            if (checkCredentialsResponse.isAuthorized()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CommandScene.fxml"));
                Parent root = fxmlLoader.load();
                CommandController controller = fxmlLoader.getController();

                controller.setCredentials(credentials);
                primaryStage.setScene(new Scene(root));
                primaryStage.show();

            } else {
                // todo: display error message in ui
            }
        }
    }

    private Credentials getCredentials() {
        return new Credentials(usernameField.getText(), passwordField.getText());
    }

    public void register() throws IOException {
        Credentials credentials = getCredentials();
        RegisterCommand loginCommand = new RegisterCommand(credentials);
        connectionManager.sendRequest(new Request(loginCommand, credentials));
    }
}
