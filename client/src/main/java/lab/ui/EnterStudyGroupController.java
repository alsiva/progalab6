package lab.ui;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lab.domain.*;
import java.util.Date;
import java.util.ResourceBundle;

public class EnterStudyGroupController extends AbstractCommandController implements LocalizedController {

    //todo catch Exceptions with incorrect user data
    @FXML
    TextField name, coordinatesX, coordinatesY, studentsCount;
    @FXML
    ComboBox<FormOfEducation> formOfEducation;
    @FXML
    ComboBox<Semester> semester;
    @FXML
    Label generalInfoLabel, coordinates;

    @FXML
    private Node enterPerson;
    @FXML
    private EnterPersonController enterPersonController;

    @Override
    public void updateLanguage(ResourceBundle bundle) {
        name.setPromptText(bundle.getString("EnterGroupPage.name"));
        studentsCount.setPromptText(bundle.getString("EnterGroupPage.studentsCount"));
        formOfEducation.setPromptText(bundle.getString("EnterGroupPage.formOfEducation"));
        semester.setPromptText(bundle.getString("EnterGroupPage.semester"));
        generalInfoLabel.setText(bundle.getString("generalInfoLabel"));
        coordinates.setText(bundle.getString("coordinatesLabel"));
        enterPersonController.updateLanguage(bundle);
    }

    @FXML
    public void initialize() {
        formOfEducation.getItems().setAll(FormOfEducation.values());
        formOfEducation.setValue(FormOfEducation.FULL_TIME_EDUCATION);
        semester.getItems().setAll(Semester.values());
        semester.setValue(Semester.SECOND);
    }

    private Coordinates readCoordinates() throws FailedToParseException {
        String xField = coordinatesX.getText();
        float x = Coordinates.readX(xField);

        String yField = coordinatesY.getText();
        int y = Coordinates.readY(yField);

        return new Coordinates(x, y);
    }


    public StudyGroup getGroup() throws FailedToParseException {
        String nameField = name.getText();
        String name = StudyGroup.readName(nameField);

        Coordinates coordinates = readCoordinates();

        Date creationDate = new Date();

        String studentsCountField = studentsCount.getText();
        int studentsCount = StudyGroup.readStudentsCount(studentsCountField);

        return new StudyGroup(
                null,
                name,
                coordinates,
                creationDate,
                studentsCount,
                formOfEducation.getValue(),
                semester.getValue(),
                enterPersonController.getPerson(),
                null
        );

    }

    public void setGroup(StudyGroup studyGroup) {
        name.setText(studyGroup.getName());
        coordinatesX.setText(String.valueOf(studyGroup.getCoordinateX()));
        coordinatesY.setText(String.valueOf(studyGroup.getCoordinateY()));
        studentsCount.setText(String.valueOf(studyGroup.getStudentsCount()));

        formOfEducation.setValue(studyGroup.getFormOfEducation());
        semester.setValue(studyGroup.getSemesterEnum());


        Person admin = studyGroup.getAdmin();
        if (admin != null) {
            enterPersonController.setPerson(admin);
        }
    }
}
