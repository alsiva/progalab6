package lab.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lab.domain.FailedToParseException;
import lab.domain.Location;
import lab.domain.Person;

import java.time.LocalDate;
import java.util.ResourceBundle;

public class EnterPersonController implements LocalizedController {
    @FXML
    TextField personName, passport;

    @FXML
    DatePicker birthdayPicker;

    @FXML
    Node enterLocation;

    @FXML
    EnterLocationController enterLocationController;
    @FXML
    Label groupAdminLabel;

    @Override
    public void updateLanguage(ResourceBundle bundle) {
        personName.setPromptText(bundle.getString("EnterPersonController.personName"));
        passport.setPromptText(bundle.getString("EnterPersonController.passport"));
        birthdayPicker.setPromptText(bundle.getString("EnterPersonController.birthdayPicker"));
        groupAdminLabel.setText(bundle.getString("EnterPersonController.groupAdminLable"));
        enterLocationController.updateLanguage(bundle);
    }

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

    public void setPerson(Person admin) {
        personName.setText(admin.getName());
        birthdayPicker.setValue(admin.getBirthday());
        String passportID = admin.getPassportID();
        if (passportID != null) {
            passport.setText(passportID);
        }

        Location location = admin.getLocation();
        if (location != null) {
            enterLocationController.setLocation(location);
        }
    }
}
