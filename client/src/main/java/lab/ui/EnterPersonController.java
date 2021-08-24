package lab.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lab.domain.FailedToParseException;
import lab.domain.Location;
import lab.domain.Person;

import java.time.LocalDate;

public class EnterPersonController {
    @FXML
    TextField personName, passport;

    @FXML
    DatePicker birthdayPicker;

    @FXML
    Node enterLocation;

    @FXML
    EnterLocationController enterLocationController;

    public Person getPerson() throws FailedToParseException {
        String adminName = personName.getText().trim();
        if (adminName.isEmpty()) {
            return null;
        }

        LocalDate adminBirthday = birthdayPicker.getValue();

        String passportId = Person.readPassportID(passport.getText());

        Location location = enterLocationController.getLocation();

        return new Person(null, adminName, adminBirthday, passportId, location);
    }
}
