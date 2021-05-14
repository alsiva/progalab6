import commands.Command;
import commands.ExitCommand;
import commands.HistoryCommand;
import response.Response;

import java.io.*;
import java.net.PortUnreachableException;

public class Client {
    private static final CommandManager commandManager = new CommandManager();
    private static final ResponseHandlerClient responseHandler = new ResponseHandlerClient();


    public static void main(String[] args) {
        CommandReader commandReader = new CommandReader(new BufferedReader(new InputStreamReader(System.in)));

        ConnectionManagerClient connectionManager;
        try {
            connectionManager = new ConnectionManagerClient(Constants.SERVICE_PORT);
        } catch (IOException e) {
            System.err.println("Error creating connection " + e.getMessage());
            return;
        }

        while (true) {
            Command command;
            try {
                System.out.println("Please enter command:");
                command = commandReader.readCommand();
            } catch (IOException e) {
                System.err.println("Failed to read command " + e.getMessage());
                continue;
            }

            if (command == null) {
                continue;
            }

            if (command instanceof HistoryCommand) {
                for (Command recentCommand : commandManager.getLastCommands()) {
                    System.out.println(recentCommand);
                }
                continue;
            }
            commandManager.add(command);

            if (command instanceof ExitCommand) {
                System.out.println("Received exit command; terminating.");
                break;
            }

            try {
                connectionManager.sendCommand(command);
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
        }
    }
}