package lab.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lab.domain.FailedToParseException;
import lab.domain.Location;

public class EnterLocationController {
    @FXML
    TextField name, x, y;

    public Location getLocation() throws FailedToParseException {
        Integer locationX = Location.readX(x.getText().trim());

        if (locationX == null) {
            return null;
        }

        int locationY = Location.readY(y.getText().trim());

        String locationName = name.getText().trim();

        return new Location(null, locationX, locationY, locationName);
    }
}
