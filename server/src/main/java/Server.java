import response.Response;

import java.io.IOException;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Please specify input file name as program argument");
            return;
        }

        String filename = args[0];
        Administration administration = new Administration(filename);
        ResponseHandler responseHandler = new ResponseHandler(administration);

        ConnectionManager connectionManager;
        try {
            connectionManager = new ConnectionManager(Constants.SERVICE_PORT);
        } catch (IOException e) {
            System.err.println("Error creating connection: " + e.getMessage());
            return;
        }

        while (true) {
            System.out.println("Waiting for a packet from client");

            ConnectionManager.CommandFromClient commandFromClient;
            try {
                commandFromClient = connectionManager.receiveCommand();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Failed to receive command: " + e.getMessage());
                System.out.println("Press enter to try again or write \"exit\" to terminate the program");

                Scanner in = new Scanner(System.in);
                String inputString = in.nextLine();

                if (inputString.equals("exit")) {
                    administration.save();
                    break;
                } else {
                    continue;
                }
            }

            Response response;
            try {
                response = responseHandler.getResponse(commandFromClient.getCommand());
            } catch (ResponseHandler.CommandNotRecognizedException e) {
                System.err.println("Command not recognized: " + e.getNotRecognizedCommand());
                continue;
            }

            try {
                connectionManager.sendResponse(response, commandFromClient.getSenderAddress());
            } catch (IOException e) {
                System.err.println("Failed to send response");
            }
        }
    }
}
