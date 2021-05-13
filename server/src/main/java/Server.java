import commands.*;
import domain.StudyGroup;
import response.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class Server {
    // Серверный UDP-сокет запущен на этом порту
    public final static int SERVICE_PORT = 50001; // todo: get from constructor


    static class ResponseHandler { // todo: move to separate file
        private final Administration administration;

        public ResponseHandler(Administration administration) {
            this.administration = administration;
        }

        public Response getResponse(Command command) {
            if (command instanceof HelpCommand) {
                String help = administration.help();
                return new HelpResponse(help);

            } else if(command instanceof InfoCommand) {
                String info = administration.info();
                return new InfoResponse(info);

            } else if(command instanceof ShowCommand) {
                Set<StudyGroup> groups = administration.show();
                return new ShowResponse(groups);

            } else if(command instanceof AddCommand) {
                StudyGroup group = administration.add(((AddCommand) command).getGroup());
                return new AddResponse(group);

            } else if(command instanceof UpdateIdCommand) {
                Optional<StudyGroup> group = administration.updateId(((UpdateIdCommand) command).getGroup());
                return new UpdateIdResponse(group);

            } else if(command instanceof RemoveByIdCommand) {
                Optional<StudyGroup> studyGroup = administration.removeById(((RemoveByIdCommand) command).getId());
                return new RemoveByIdResponse(studyGroup);

            } else if(command instanceof ClearCommand) {
                administration.clear();
                return new ClearResponse();

            } else if(command instanceof ExecuteScriptCommand) {
                return null; //todo command response for script execution
            } else if(command instanceof AddIfMinCommand) {
                Boolean wasAdded = administration.addIfMin(((AddIfMinCommand) command).getStudyGroup());
                return new AddIfMinResponse(wasAdded);

            } else if(command instanceof RemoveLowerCommand) {
                Set<StudyGroup> removedGroups = administration.removeLower(((RemoveLowerCommand) command).getStudyGroup());
                return new RemoveLowerResponse(removedGroups);

            } else if(command instanceof RemoveAllByStudentsCountCommand) {
                Set<StudyGroup> removedGroups = administration.removeAllByStudentsCount(((RemoveAllByStudentsCountCommand) command).getCount());
                return new RemoveAllByStudentsCountResponse(removedGroups);

            } else if(command instanceof CountByGroupAdminCommand) {
                long count = administration.countByGroupAdmin(((CountByGroupAdminCommand) command).getGroupAdmin());
                return new CountByGroupAdminResponse(count);

            } else if(command instanceof FilterLessThanSemesterEnumCommand) {
                Set<StudyGroup> studyGroupsWithLessEnum = administration.filterLessThanSemesterEnum(((FilterLessThanSemesterEnumCommand) command).getSemesterEnum());
                return new FilterLessThanSemesterEnumResponse(studyGroupsWithLessEnum);
            }

            System.err.println("Command not supported");
            return null;
        }

        public void sendResponse(Response response, DatagramPacket inputPacket, DatagramSocket serverSocket) throws IOException {
            // сериализцаия Response
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream)) {
                objectOutput.writeObject(response);
            }
            byteArrayOutputStream.flush();

            byte[] serializedResponse = byteArrayOutputStream.toByteArray();

            // Получите IP-адрес и порт клиента
            InetAddress senderAddress = inputPacket.getAddress();
            int senderPort = inputPacket.getPort();

            // Создайте новый UDP-пакет с данными, чтобы отправить их клиенту
            DatagramPacket outputPacket = new DatagramPacket(
                    serializedResponse, serializedResponse.length,
                    senderAddress, senderPort
            );

            // Отправьте пакет клиенту
            serverSocket.send(outputPacket);

        }
    }

    static class ConnectionManager {  // todo: move to separate file
        public DatagramSocket getServerSocket() {
            try {
                return new DatagramSocket(SERVICE_PORT);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            return null;
        }

        public DatagramPacket getInputPacket(DatagramSocket serverSocket) throws IOException {

            byte[] receivingDataBuffer = new byte[1024];

            DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
            System.out.println("Waiting for a packet from client");

            serverSocket.receive(inputPacket);
            return inputPacket;
        }

        public Command getCommand(DatagramPacket inputPacket) {
            try (ObjectInputStream commandInBytes = new ObjectInputStream(new ByteArrayInputStream(inputPacket.getData()))) {
                return (Command) commandInBytes.readObject();
            } catch (IOException | ClassNotFoundException e) {
                return null; // todo: optional
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Please specify input file name as program argument");
            return;
        }

        // todo: обрабатывать команду save

        String filename = args[0];
        Administration administration = new Administration(filename);
        ResponseHandler responseHandler = new ResponseHandler(administration);
        ConnectionManager connectionManager = new ConnectionManager();
        DatagramSocket serverSocket = connectionManager.getServerSocket();

        while (true) {
            DatagramPacket inputPacket = connectionManager.getInputPacket(serverSocket);
            Command command = connectionManager.getCommand(inputPacket); // todo: null check (optional)

            Response response = responseHandler.getResponse(command);
            responseHandler.sendResponse(response, inputPacket, serverSocket);
        }
    }
}
