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
import java.util.Random;

public class VisualizeController extends AbstractCommandController {

    protected Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

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

        int rectSize = 30;

        Random generator = new Random(7032863866410503392L);
        for (StudyGroup group : studyGroups) {
            int x = generator.nextInt((int) canvas.getWidth() - rectSize);
            int y = generator.nextInt((int) canvas.getHeight() - rectSize);

            int count = group.getStudentsCount();

            Rectangle rectangle = new Rectangle();

            rectangle.setX(x);
            rectangle.setY(y);
            rectangle.setWidth(30);
            rectangle.setHeight(30);

            String name = group.getAdminName();


            Random colorRandom = new Random(name == null ? "group-with-empty-admin".hashCode() : name.hashCode());
            byte[] rgb = new byte[3];
            colorRandom.nextBytes(rgb);

            gc.setFill(Color.rgb(rgb[0] + 128, rgb[1] + 128, rgb[2] + 128));
            gc.fillRect(x, y, 30, 30);

            squares.add(new Square(x, y, 30, 30, group));
        }

        canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            for (Square square : squares) {
                if ((e.getX() - square.x <= square.width) && (e.getX() >= square.x)) {
                    if ((e.getY() - square.y <= square.height) && (e.getY() >= square.y)) {
                        try {
                            Pages.openEditGroupModal(primaryStage, connectionManager, credentials, square.getStudyGroup(), group -> {});
                            return;
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
        });
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