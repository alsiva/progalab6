import commands.Command;
import response.Response;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ConnectionManager {
    private final DatagramSocket socket;
    private final byte[] buffer = new byte[Constants.BUFFER_CAPACITY];

    public ConnectionManager(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }

    public CommandFromClient receiveCommand() throws IOException, ClassNotFoundException {
        DatagramPacket inputPacket = new DatagramPacket(buffer, buffer.length);

        socket.receive(inputPacket);

        try (ObjectInputStream commandInBytes = new ObjectInputStream(new ByteArrayInputStream(inputPacket.getData()))) {
            Command command = (Command) commandInBytes.readObject();
            return new CommandFromClient(command, ClientInfo.fromPacket(inputPacket));
        }
    }

    public void sendResponse(Response response, ClientInfo clientInfo) throws IOException {
        // сериализцаия Response
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutput.writeObject(response);
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] serializedResponse = byteArrayOutputStream.toByteArray();

        // Создайте новый UDP-пакет с данными, чтобы отправить их клиенту
        DatagramPacket outputPacket = new DatagramPacket(
                serializedResponse,
                serializedResponse.length,
                clientInfo.senderAddress,
                clientInfo.senderPort
        );

        // Отправьте пакет клиенту
        socket.send(outputPacket);
    }

    public static class ClientInfo {
        private final InetAddress senderAddress;
        private final int senderPort;

        private ClientInfo(InetAddress senderAddress, int senderPort) {
            this.senderAddress = senderAddress;
            this.senderPort = senderPort;
        }

        private static ClientInfo fromPacket(DatagramPacket inputPacket) {
            return new ClientInfo(inputPacket.getAddress(), inputPacket.getPort());
        }
    }

    public static class CommandFromClient {
        private final Command command;
        private final ClientInfo clientInfo;

        public CommandFromClient(Command command, ClientInfo clientInfo) {
            this.command = command;
            this.clientInfo = clientInfo;
        }

        public Command getCommand() {
            return command;
        }

        public ClientInfo getClientInfo() {
            return clientInfo;
        }
    }
}