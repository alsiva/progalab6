package lab;

import lab.commands.Request;
import lab.response.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ConnectionManagerClient implements AutoCloseable {
    private final DatagramChannel channel;

    public ConnectionManagerClient(int port) throws IOException {
        channel = DatagramChannel.open();
        channel.connect(new InetSocketAddress("localhost", port));
    }

    public void sendRequest(Request request) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutput.writeObject(request);
        }

        byte[] serializedCommand = byteArrayOutputStream.toByteArray();

        ByteBuffer sendingBuffer = ByteBuffer.wrap(serializedCommand);
        channel.write(sendingBuffer);
    }

    public Response receiveResponse() throws IOException, ClassNotFoundException {
        ByteBuffer receivingBuffer = ByteBuffer.allocate(Constants.BUFFER_CAPACITY);
        channel.read(receivingBuffer);
        try (ObjectInputStream responseInBytes = new ObjectInputStream(new ByteArrayInputStream(receivingBuffer.array()))) {
            return (Response) responseInBytes.readObject();
        }
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}