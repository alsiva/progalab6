package lab;

import lab.commands.Request;
import lab.response.Response;

import java.io.IOException;
import java.util.concurrent.RecursiveAction;

public class ReceiveRequestsAction extends RecursiveAction {
    private final ConnectionManager connectionManager;
    private final ResponseHandler responseHandler;

    public ReceiveRequestsAction(ConnectionManager connectionManager, ResponseHandler responseHandler) {
        this.connectionManager = connectionManager;
        this.responseHandler = responseHandler;
    }

    @Override
    protected void compute() {
        while (true) {
            System.out.println("Waiting for a packet from client");

            ConnectionManager.RequestFromClient requestFromClient;
            try {
                requestFromClient = connectionManager.receiveRequest();
            } catch (IOException e) {
                // socket was closed from server thread
                System.out.println("Socket closed, stop waiting for requests");
                return;
            } catch (ClassNotFoundException e) {
                System.err.println("Failed to decode command: " + e.getMessage());
                System.out.println("Trying again");
                continue;
            }

            Response response;
            try {
                Request request = requestFromClient.getRequest();
                response = responseHandler.getResponse(request.command, request.credentials);
            } catch (ResponseHandler.CommandNotRecognizedException e) {
                System.err.println("Command not recognized: " + e.getNotRecognizedCommand());
                continue;
            }

            try {
                connectionManager.sendResponse(response, requestFromClient.getSenderAddress());
            } catch (IOException e) {
                System.err.println("Failed to send response");
            }
        }
    }
}
