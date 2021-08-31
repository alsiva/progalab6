package lab.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InfoMessageController {
    @FXML
    public Label infoLabel;

    public void setMessage(String message) {
        infoLabel.setText(message);
    }
}
