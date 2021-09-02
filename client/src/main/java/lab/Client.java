package lab;

import javafx.application.Application;
import javafx.stage.Stage;
import lab.ui.Pages;

import java.io.IOException;

public class Client extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ConnectionManagerClient connectionManager;
        try {
            connectionManager = new ConnectionManagerClient(Constants.SERVICE_PORT);
        } catch (IOException e) {
            System.err.println("Error creating connection " + e.getMessage());
            return;
        }

        Pages.openLoginPage(primaryStage, connectionManager);
    }


}