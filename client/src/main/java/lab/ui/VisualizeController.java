package lab.ui;

import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import lab.commands.ShowCommand;
import lab.domain.StudyGroup;
import lab.response.Response;
import lab.response.ShowResponse;

import java.io.IOException;
import java.util.List;

public class VisualizeController extends AbstractCommandController {

    @FXML
    public void initialize() throws IOException {
        Response response = getResponse(new ShowCommand());
        if (!(response instanceof ShowResponse)) {
            return;
        }

        List<StudyGroup> studyGroups = ((ShowResponse) response).getGroups();

        for (StudyGroup group: studyGroups) {
            double x = group.getCoordinateX();
            double y = group.getCoordinateY();
            int count = group.getStudentsCount();

            Rectangle rectangle = new Rectangle();

            rectangle.setX(group.getCoordinateX());
            rectangle.setY(group.getCoordinateY());
        }
    }

    public void exit() throws IOException {
        Pages.openCommandsPage(primaryStage, connectionManager, credentials);
    }
}
