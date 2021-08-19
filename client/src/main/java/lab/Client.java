package lab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lab.auth.AuthorizationManager;
import lab.auth.Credentials;
import lab.commands.*;
import lab.response.Response;
import lab.ui.LoginController;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.util.Optional;

public class Client extends Application {
    private static final CommandManager commandManager = new CommandManager();

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

        CommandReader commandReader = new CommandReader();
        AuthorizationManager authorizationManager = new AuthorizationManager();
        ResponseHandlerClient responseHandler = new ResponseHandlerClient(authorizationManager);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginScene.fxml"));
        Parent root = fxmlLoader.load();
        LoginController controller = fxmlLoader.getController();

        controller.setPrimaryStage(primaryStage);
        controller.setConnectionManager(connectionManager);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        /*while (true) {
            Command command;
            try {
                System.out.println(authorizationManager.isAuthorized()
                        ? "Please enter command:"
                        : "Please login or register new user:"
                );

                command = commandReader.readCommand();
            } catch (IOException e) {
                System.err.println("Failed to read command " + e.getMessage());
                continue;
            }

            if (command instanceof HistoryCommand) {
                for (Command recentCommand : commandManager.getLastCommands()) {
                    System.out.println(recentCommand.toPrint());
                }
                continue;
            }
            commandManager.add(command);

            if (command instanceof ExitCommand) {
                System.out.println("Received exit command; terminating.");
                break;
            }

            if (command instanceof HelpCommand) {
                System.out.println(CommandReader.HELP_CONTENTS);
                continue;
            }

            try {
                Optional<Credentials> credentialsOptional;
                if (command instanceof CheckCredentialsCommand) {
                    credentialsOptional = Optional.of(((CheckCredentialsCommand) command).getCredentials());
                } else if (command instanceof RegisterCommand) {
                    credentialsOptional = Optional.of(((RegisterCommand) command).getCredentials());
                } else {
                    credentialsOptional = authorizationManager.getCredentials();
                }

                if (!credentialsOptional.isPresent()) {
                    System.out.println("You're not authorized to run this command: " + command.toPrint());
                    continue;
                }

                connectionManager.sendRequest(new Request(command, credentialsOptional.get()));

            } catch (IOException e) {
                System.err.println("Failed to send command " + e.getMessage());
                continue;
            }

            Response response;
            try {
                response = connectionManager.receiveResponse();
            } catch (PortUnreachableException e) {
                System.err.println("Server unreachable, try again later");
                continue;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Failed to receive response " + e.getMessage());
                continue;
            }

            responseHandler.handleResponse(response);
        }*/
    }
}