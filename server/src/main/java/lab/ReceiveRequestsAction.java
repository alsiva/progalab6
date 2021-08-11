package lab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class ReceiveRequestsAction extends RecursiveAction {
    private final ConnectionManager connectionManager;
    private final ResponseHandler responseHandler;

    private final List<HandleRequestAction> launchedActions = new ArrayList<>();

    public ReceiveRequestsAction(ConnectionManager connectionManager, ResponseHandler responseHandler) {
        this.connectionManager = connectionManager;
        this.responseHandler = responseHandler;
    }

    private void waitForAllLaunchedActions() {
        for (HandleRequestAction launchedAction : launchedActions) {
            launchedAction.join();
        }
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
                System.out.println("Socket closed, stop waiting for requests, wait for requests to finish handling");
                waitForAllLaunchedActions();
                return;
            } catch (ClassNotFoundException e) {
                System.err.println("Failed to decode command: " + e.getMessage());
                System.out.println("Trying again");
                continue;
            }

            HandleRequestAction handleRequestAction = new HandleRequestAction(responseHandler, connectionManager, requestFromClient);
            handleRequestAction.fork();
            launchedActions.add(handleRequestAction);
        }
    }
}
