package lab;

import lab.commands.Request;
import lab.response.Response;

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

    public RequestFromClient receiveRequest() throws IOException, ClassNotFoundException {
        ByteBuffer inputBuffer = ByteBuffer.allocate(Constants.BUFFER_CAPACITY);
        SocketAddress senderAddress = channel.receive(inputBuffer);

        try (ObjectInputStream commandInBytes = new ObjectInputStream(new ByteArrayInputStream(inputBuffer.array()))) {
            Request request = (Request) commandInBytes.readObject();

            return new RequestFromClient(request, senderAddress);
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

    public static class RequestFromClient {
        private final Request request;
        private final SocketAddress senderAddress;

        public RequestFromClient(Request request, SocketAddress senderAddress) {
            this.request = request;
            this.senderAddress = senderAddress;
        }

        public Request getRequest() {
            return request;
        }

        public SocketAddress getSenderAddress() {
            return senderAddress;
        }
    }
}