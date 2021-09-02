package lab.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ResourceBundle;

public class InfoMessageController implements LocalizedController {
    @FXML
    public Label infoLabel;

    public void setMessage(String message) {
        infoLabel.setText(message);
    }

    @Override
    public void updateLanguage(ResourceBundle bundle) {
        // no ui
    }
}
