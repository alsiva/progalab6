package lab.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

    @FXML
    Label errorLabel;

    public void clearError() {
        errorLabel.setText("");
    }

    public void logIn() throws IOException {
        Credentials credentials = getCredentials();
        CheckCredentialsCommand loginCommand = new CheckCredentialsCommand(credentials);
        try {
            connectionManager.sendRequest(new Request(loginCommand, credentials));
        } catch (Exception e) {
            errorLabel.setText("Failed to send request to server");
        }

        Response response;
        try {
            response = connectionManager.receiveResponse();
        } catch (Exception e) {
            errorLabel.setText("Failed to login, did you start server?");
            return;
        }


        if (!(response instanceof CheckCredentialsResponse)) {
            errorLabel.setText("Unknown response from server");
            return;
        }

        CheckCredentialsResponse checkCredentialsResponse = (CheckCredentialsResponse) response;

        if (!checkCredentialsResponse.isAuthorized()) {
            errorLabel.setText("Wrong credentials");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CommandScene.fxml"));
        Parent root = fxmlLoader.load();
        CommandController controller = fxmlLoader.getController();

        controller.setCredentials(credentials);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

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
