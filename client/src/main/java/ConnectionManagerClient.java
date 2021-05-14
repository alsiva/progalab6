import commands.Command;
import response.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ConnectionManagerClient {
    private final DatagramChannel channel;

    public ConnectionManagerClient(int port) throws IOException {
        channel = DatagramChannel.open();
        channel.connect(new InetSocketAddress("localhost", port));
    }

    public void sendCommand(Command command) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutput.writeObject(command);
        }

        byte[] serializedCommand = byteArrayOutputStream.toByteArray();

        ByteBuffer sendingBuffer = ByteBuffer.wrap(serializedCommand);
        channel.write(sendingBuffer);
    }

    public Response receiveResponse() throws IOException, ClassNotFoundException {
        ByteBuffer receivingBuffer = ByteBuffer.allocate(Constants.BUFFER_CAPACITY);
        channel.read(receivingBuffer);
        ObjectInputStream responseInBytes = new ObjectInputStream(new ByteArrayInputStream(receivingBuffer.array()));
        return (Response)responseInBytes.readObject();
    }
}