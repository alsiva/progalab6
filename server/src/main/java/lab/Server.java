package lab;

import lab.auth.AuthorizationControlManager;

import java.util.HashMap;
import java.util.Map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.sql.SQLException;
import java.util.concurrent.ForkJoinPool;

public class Server {
    public static void main(String[] args) {
        Configuration configuration;
        try {
            configuration = Configuration.readConfiguration(args);
        } catch (Configuration.ConfigurationReadException e) {
            System.err.println(e.getMessage());
            return;
        }

        DatabaseManager databaseManager = new DatabaseManager(configuration);
        Administration administration;
        try {
            administration = new Administration(databaseManager);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            return;
        }

        AuthorizationControlManager authorizationManager = new AuthorizationControlManager(databaseManager);
        Notifier notifier;
        try {
            notifier = new Notifier();
        } catch (IOException e) {
            System.err.println("Failed to create Notifier");
            return;
        }

        ResponseHandler responseHandler = new ResponseHandler(administration, authorizationManager, notifier);

        DatagramChannel channel;
        try {
            channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(Constants.SERVICE_PORT));
        } catch (IOException e) {
            System.err.println("Error opening datagram channel or binding to " + Constants.SERVICE_PORT);
            return;
        }

        ConnectionManager connectionManager = new ConnectionManager(channel);

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ReceiveRequestsAction receiveRequestsAction = new ReceiveRequestsAction(connectionManager, responseHandler);
        forkJoinPool.invoke(receiveRequestsAction);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Please enter server command");
            String command;
            try {
                command = reader.readLine();
            } catch (IOException e) {
                System.err.println("Failed to read from input: " + e.getMessage());
                continue;
            }

            if (command.equalsIgnoreCase("exit")) {
                try {
                    channel.close();
                } catch (IOException e) {
                    break;
                }
                System.out.println("Waiting for requests to finish");
                receiveRequestsAction.join();
                break;
            }
        }
    }
}
