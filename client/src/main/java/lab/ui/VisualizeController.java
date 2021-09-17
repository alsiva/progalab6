package lab.ui;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import lab.commands.ShowCommand;
import lab.domain.StudyGroup;
import lab.response.Response;
import lab.response.ShowResponse;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class VisualizeController extends AbstractCommandController implements LocalizedController{

    protected Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    Canvas canvas;

    @FXML
    ImageView polyakov;

    @FXML
    public void initialize() throws MalformedURLException {
        //translate
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(polyakov);
        translate.setDuration(Duration.millis(2000));
        translate.setCycleCount(4);
        translate.setByX(300);
        translate.setByY(-200);
        translate.setAutoReverse(true);
        translate.play();

        //rotate
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(polyakov);
        rotate.setDuration(Duration.millis(2000));
        rotate.setCycleCount(4);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setByAngle(360);
        rotate.play();

        //fade
        FadeTransition fade = new FadeTransition();
        fade.setNode(polyakov);
        fade.setDuration(Duration.millis(10000));
        fade.setCycleCount(1);
        fade.setInterpolator(Interpolator.LINEAR);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.play();

    }



    @Override
    public void updateLanguage(ResourceBundle bundle) {

    }

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