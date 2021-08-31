package lab.ui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lab.commands.ShowCommand;
import lab.domain.StudyGroup;
import lab.response.Response;
import lab.response.ShowResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VisualizeController extends AbstractCommandController {

    protected Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    @FXML
    Canvas canvas;

    public void getDataForVisualisation() {
        Response response;
        try {
            response = getResponse(new ShowCommand());
        } catch (IOException e) {
            return;
        }

        if (!(response instanceof ShowResponse)) {
            return;
        }

        List<StudyGroup> studyGroups = ((ShowResponse) response).getGroups();
        List<Square> squares = new ArrayList<>();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (StudyGroup group: studyGroups) {
            double x = group.getCoordinateX() * 10;
            double y = group.getCoordinateY() * 10;
            int count = group.getStudentsCount();

            Rectangle rectangle = new Rectangle();
            

            rectangle.setX(x);
            rectangle.setY(y);
            rectangle.setWidth(30);
            rectangle.setHeight(30);
            rectangle.setFill(Color.LIMEGREEN);


            gc.fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());

            squares.add(new Square(x, y, 30, 30, group));
        }

        EventHandler<MouseEvent> eventHandler = e -> {

            for (Square square: squares) {
                if ((e.getX() - square.x <= square.width) && (e.getX() >= square.x))  {
                    if ((e.getY() - square.y <= square.height) && (e.getY() >= square.y)) {
                        try {
                            Pages.openStudyGroupsModal(primaryStage, connectionManager, credentials,Collections.singletonList(square.getStudyGroup()));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
        };

        canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

        }

    public void exit() throws IOException {
        Pages.openCommandsPage(primaryStage, connectionManager, credentials);
    }

    }

    class Square {
        double x;
        double y;
        double width;
        double height;
        StudyGroup studyGroup;

        public Square(double x, double y, double width, double height, StudyGroup studyGroup) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.studyGroup = studyGroup;
        }

        public StudyGroup getStudyGroup() {
            return studyGroup;
        }

    }