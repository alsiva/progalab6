package lab.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lab.ConnectionManagerClient;
import lab.auth.Credentials;

import java.io.IOException;
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
            controller.setPrimaryStage(primaryStage);
            controller.setConnectionManager(connectionManager);
        });
    }

    public static void openCommandsPage(Stage primaryStage, ConnectionManagerClient connectionManager, Credentials credentials) throws IOException {
        openPage(primaryStage, "/CommandScene.fxml", (CommandController controller) -> {
            controller.setPrimaryStage(primaryStage);
            controller.setConnectionManager(connectionManager);
            controller.setCredentials(credentials);
        });
    }
}
