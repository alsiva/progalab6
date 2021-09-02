package lab.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lab.ConnectionManagerClient;
import lab.auth.Credentials;
import lab.domain.StudyGroup;
import lab.languages.UiLanguage;

import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Pages {
    private static <T extends LocalizedController> void openPage(Stage primaryStage, String pageResourcePath, Consumer<T> setup) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Pages.class.getResource(pageResourcePath));
        Parent root = fxmlLoader.load();

        T controller = fxmlLoader.getController();
        setup.accept(controller);
        primaryStage.setScene(new Scene(root));
        controller.updateLanguage(UiLanguage.getLanguageBundle());
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

            controller.getDataForVisualisation();
        });
    }

    public static void openEditGroupModal(
        Window owner,
        ConnectionManagerClient connectionManager,
        Credentials credentials,
        StudyGroup studyGroup,
        Consumer<StudyGroup> onSuccess
    ) throws IOException {
        openModal(owner, "/EditStudyGroup.fxml", "Edit group", (Stage stage, EditGroupController controller) -> {
            controller.setOwner(owner);
            controller.setConnectionManager(connectionManager);
            controller.setCredentials(credentials);
            controller.setStudyGroup(studyGroup);
            controller.setOnSuccess(onSuccess);
        } );
    }

    private static <T extends LocalizedController> void openModal(Window owner, String pageResourcePath, String title, BiConsumer<? super Stage, T> setup) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Pages.class.getResource(pageResourcePath));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        T controller = fxmlLoader.getController();
        setup.accept(stage, controller);

        controller.updateLanguage(UiLanguage.getLanguageBundle());
        stage.show();
    }

    public static void openInfoModal(Window owner, String message) throws IOException {
        openModal(owner, "/InfoMessage.fxml", "Info", (Stage stage, InfoMessageController controller) -> {
            controller.setMessage(message);
        });
    }

    public static void openStudyGroupsModal(Stage primaryStage, ConnectionManagerClient connectionManager, Credentials credentials ,List<StudyGroup> groups) throws IOException {
        openModal(primaryStage, "/StudyGroupScene.fxml", "Info", (Stage stage, StudyGroupController controller) -> {
            controller.setStage(stage);
            controller.setConnectionManager(connectionManager);
            controller.setCredentials(credentials);

            controller.initData(groups);
        });
    }
}
