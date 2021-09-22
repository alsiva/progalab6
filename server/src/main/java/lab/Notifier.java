package lab;

import lab.commands.Request;
import lab.commands.SubscribeForUpdatesCommand;
import lab.domain.StudyGroup;
import lab.response.GroupAddedResponse;
import lab.response.GroupChangedResponse;
import lab.response.GroupRemovedResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.HashSet;

import java.util.Set;

public class Notifier {

    private final ConnectionManager connectionManager;
    private final Set<SocketAddress> subscribers = new HashSet<>();

    public Notifier() throws IOException {
        DatagramChannel channel;

        channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(Constants.UPDATE_PORT));

        this.connectionManager = new ConnectionManager(channel);
        startListening();
    }

    private void startListening() {
        Thread listeningThread = new Thread(() -> {
            while (true) {

                ConnectionManager.RequestFromClient requestFromClient;

                try {
                    requestFromClient = this.connectionManager.receiveRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    continue;
                }


                Request request = requestFromClient.getRequest();
                if (request.command instanceof SubscribeForUpdatesCommand) {
                    SubscribeForUpdatesCommand subscribeForUpdatesCommand = (SubscribeForUpdatesCommand) request.command;
                    if (subscribeForUpdatesCommand.shouldSubscribe) {
                        subscribers.add(requestFromClient.getSenderAddress());
                    } else {
                        subscribers.remove(requestFromClient.getSenderAddress());
                    }
                }

            }
        });
        listeningThread.start();
    }

    public void notifyAboutGroupChange(StudyGroup studyGroup, SocketAddress senderAddress) {
        for (SocketAddress subscriberAddress : subscribers) {
            if (!(senderAddress.equals(subscriberAddress))) {
                try {
                    connectionManager.sendResponse(new GroupChangedResponse(studyGroup), subscriberAddress);
                } catch (IOException e) {
                    System.err.println("Failed to send GroupChangedResponse");
                }
            }
        }
    }

    public void notifyAboutAddingGroup(StudyGroup studyGroup, SocketAddress senderAddress) {
        for (SocketAddress subscriberAddress : subscribers) {
            if (!(senderAddress.equals(subscriberAddress))) {
                try {
                    connectionManager.sendResponse(new GroupAddedResponse(studyGroup), subscriberAddress);
                } catch (IOException e) {
                    System.err.println("Failed to send GroupAddedResponse");
                }
            }
        }
    }

    public void notifyAboutRemovingGroup(Set<Long> removedIds, SocketAddress senderAddress) {
        for (SocketAddress subscriberAddress: subscribers) {
            if (!(senderAddress.equals(subscriberAddress))) {
                try {
                    connectionManager.sendResponse(new GroupRemovedResponse(removedIds), subscriberAddress);
                } catch (IOException e) {
                    System.err.println("Failed to send GroupRemovedResponse");
                }
            }
        }
    }
}
