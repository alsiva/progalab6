package lab.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import java.util.ResourceBundle;

public class LoginController implements LocalizedController {
    private Stage primaryStage;
    private ConnectionManagerClient connectionManager;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public void setConnectionManager(ConnectionManagerClient connectionManager) {
        this.connectionManager = connectionManager;
    }

    @FXML
    Node changeLanguage;

    @FXML
    ChangeLanguageController changeLanguageController;

    @FXML
    void initialize() {
        changeLanguageController.setChangeLanguageCallback(this::updateLanguage);
    }


    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    @FXML
    Button loginButton, registerButton;

    @FXML
    Label enterLabel;

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

        Pages.openCommandsPage(primaryStage, connectionManager, credentials);

    }

    private Credentials getCredentials() {
        return new Credentials(usernameField.getText(), passwordField.getText());
    }

    public void register() throws IOException {
        Credentials credentials = getCredentials();
        RegisterCommand loginCommand = new RegisterCommand(credentials);
        connectionManager.sendRequest(new Request(loginCommand, credentials));
    }

    @Override
    public void updateLanguage(ResourceBundle bundle) {
        usernameField.setPromptText(bundle.getString("LoginPage.username"));
        passwordField.setPromptText(bundle.getString("LoginPage.password"));
        loginButton.setText(bundle.getString("LoginPage.login"));
        registerButton.setText(bundle.getString("LoginPage.register"));
        enterLabel.setText(bundle.getString("LoginPage.enterPanel"));

        changeLanguageController.updateLanguage(bundle);
    }
}
