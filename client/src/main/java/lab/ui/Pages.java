package lab.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lab.ConnectionManagerClient;
import lab.auth.Credentials;
import lab.domain.StudyGroup;

import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Pages {

    private static <T> void openPage(Stage primaryStage, String pageResourcePath, Consumer<? super T> setup) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Pages.class.getResource(pageResourcePath));
        Parent root = fxmlLoader.load();

        setup.accept(fxmlLoader.<T>getController());

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void openLoginPage(Stage primaryStage, ConnectionManagerClient connectionManager) throws IOException  {
        openPage(primaryStage, "/LoginScene.fxml", (LoginController controller) -> {
            primaryStage.setTitle("Please log in or register");
            controller.setPrimaryStage(primaryStage);
            controller.setConnectionManager(connectionManager);
        });
    }

    public static void openCommandsPage(Stage primaryStage, ConnectionManagerClient connectionManager, Credentials credentials) throws IOException {
        openPage(primaryStage, "/CommandScene.fxml", (CommandController controller) -> {
            primaryStage.setTitle("Logged in as " + credentials.username);
            controller.setPrimaryStage(primaryStage);
            controller.setConnectionManager(connectionManager);
            controller.setCredentials(credentials);
        });
    }

    public static void openVisualizePage(Stage primaryStage, ConnectionManagerClient connectionManager, Credentials credentials) throws IOException {
        openPage(primaryStage, "/VisualizeGroup.fxml", (VisualizeController controller) -> {
            controller.setPrimaryStage(primaryStage);
            controller.setConnectionManager(connectionManager);
            controller.setCredentials(credentials);
        });
    }

    private static <T> void openModal(Window owner, String pageResourcePath, String title, BiConsumer<? super Parent, ? super T> setup) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Pages.class.getResource(pageResourcePath));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);

        setup.accept(root, fxmlLoader.<T>getController());

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        stage.show();
    }

    public static void openInfoModal(Window owner, String message) throws IOException {
        openModal(owner, "/InfoMessage.fxml", "Info", (root, controller) -> {
            Label label = (Label) root.lookup("#infoLabel");
            label.setText(message);
        });
    }

    public static void openStudyGroupsModal(Window owner, List<StudyGroup> groups) throws IOException {
        openModal(owner, "/StudyGroupScene.fxml", "Info", (Parent root, StudyGroupController controller) -> {
            controller.initData(groups);
        });
    }
}
