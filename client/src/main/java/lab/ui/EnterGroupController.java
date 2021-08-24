package lab.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lab.ConnectionManagerClient;
import lab.commands.AddCommand;
import lab.domain.*;
import lab.response.AddResponse;
import lab.response.Response;

import java.io.IOException;
import java.util.Date;

public class EnterGroupController extends AbstractCommandController {

    //todo catch Exceptions with incorrect user data
    @FXML
    TextField name, coordinatesX, coordinatesY, studentsCount;
    @FXML
    ComboBox<FormOfEducation> formOfEducation;
    @FXML
    ComboBox<Semester> semester;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setConnectionManager(ConnectionManagerClient connectionManager) {
        this.connectionManager = connectionManager;
    }

    @FXML
    public void initialize() {
        formOfEducation.getItems().setAll(FormOfEducation.values());
        formOfEducation.setValue(FormOfEducation.FULL_TIME_EDUCATION);
        semester.getItems().setAll(Semester.values());
        semester.setValue(Semester.SECOND);
    }

    public void add() throws FailedToParseException, IOException {
        Response response = getResponse(new AddCommand(readStudyGroup()));

        if (!(response instanceof AddResponse)) {
            return;
        }

        Pages.openInfoModal(primaryStage, "Группа была добавлена успешна");
    }


    private StudyGroup readStudyGroup() throws FailedToParseException {
        String nameField = name.getText();
        String name = StudyGroup.readName(nameField);

        Coordinates coordinates = readCoordinates();

        Date creationDate = new Date();

        String studentsCountField = studentsCount.getText();
        int studentsCount = StudyGroup.readStudentsCount(studentsCountField);

        //todo readGroupAdmin

        return new StudyGroup(
                null,
                name,
                coordinates,
                creationDate,
                studentsCount,
                formOfEducation.getValue(),
                semester.getValue(),
                null,
                null
        );
    }

    private Coordinates readCoordinates() throws FailedToParseException {
        String xField = coordinatesX.getText();
        float x = Coordinates.readX(xField);

        String yField = coordinatesY.getText();
        int y = Coordinates.readY(yField);

        return new Coordinates(x,y);
    }

    private Person readGroupAdmin() {
        //todo readGroupAdmin
        return null;
    }

}
