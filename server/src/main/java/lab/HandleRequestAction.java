package lab;

import lab.commands.Request;
import lab.response.Response;

import java.io.IOException;
import java.util.concurrent.RecursiveAction;

public class HandleRequestAction extends RecursiveAction {
    private final ResponseHandler responseHandler;
    private final ConnectionManager connectionManager;
    private final ConnectionManager.RequestFromClient requestFromClient;

    public HandleRequestAction(ResponseHandler responseHandler, ConnectionManager connectionManager, ConnectionManager.RequestFromClient requestFromClient) {
        this.responseHandler = responseHandler;
        this.connectionManager = connectionManager;
        this.requestFromClient = requestFromClient;
    }

    @Override
    protected void compute() {
        Request request = requestFromClient.getRequest();

        Response response;
        try {
            response = responseHandler.getResponse(request.command, request.credentials);
        } catch (ResponseHandler.CommandNotRecognizedException e) {
            System.err.println("Command not recognized: " + e.getNotRecognizedCommand());
            return;
        }

        try {
            connectionManager.sendResponse(response, requestFromClient.getSenderAddress());
        } catch (IOException e) {
            System.err.println("Failed to send response");
        }
    }
}
