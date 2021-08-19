package lab.ui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    public void logIn() {
        System.out.println("username = " + usernameField.getText() + " password is " + passwordField.getText());
    }

    public void register() {
        System.out.println("username = " + usernameField.getText() + " password is " + passwordField.getText());
    }
}
