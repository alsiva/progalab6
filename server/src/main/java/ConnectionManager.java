import commands.Command;
import response.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ConnectionManager {
    private final DatagramChannel channel;

    public ConnectionManager(int port) throws IOException {
        //creates datagramChannel
        channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(port));
    }

    public CommandFromClient receiveCommand() throws IOException, ClassNotFoundException {
        ByteBuffer inputBuffer = ByteBuffer.allocate(Constants.BUFFER_CAPACITY);
        SocketAddress senderAddress = channel.receive(inputBuffer);

        try (ObjectInputStream commandInBytes = new ObjectInputStream(new ByteArrayInputStream(inputBuffer.array()))) {
            Command command = (Command) commandInBytes.readObject();

            return new CommandFromClient(command, senderAddress);
        }
    }

    public void sendResponse(Response response, SocketAddress socketAddress) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutput.writeObject(response);
            byteArrayOutputStream.flush();
        }

        byte[] serializedResponse = byteArrayOutputStream.toByteArray();
        ByteBuffer sendingBuffer = ByteBuffer.wrap(serializedResponse);

        channel.send(sendingBuffer, socketAddress);
    }

    public static class CommandFromClient {
        private final Command command;
        private final SocketAddress senderAddress;

        public CommandFromClient(Command command, SocketAddress senderAddress) {
            this.command = command;
            this.senderAddress = senderAddress;
        }

        public Command getCommand() {
            return command;
        }

        public SocketAddress getSenderAddress() {
            return senderAddress;
        }
    }
}