import commands.*;
import domain.Person;
import domain.StudyGroup;
import response.*;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Set;

public class Server {
    // Серверный UDP-сокет запущен на этом порту
    public final static int SERVICE_PORT = 50001;


    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Please specify input file name as program argument");
            return;
        }

        String filename = args[0];
        Administration administration = new Administration(filename);

        // Создайте новый экземпляр DatagramSocket, чтобы получать ответы от клиента
        try (DatagramSocket serverSocket = new DatagramSocket(SERVICE_PORT)) {

            /* Создайте буферы для хранения отправляемых и получаемых данных.
Они временно хранят данные в случае задержек связи */
            byte[] receivingDataBuffer = new byte[1024];

            /* Создайте экземпляр UDP-пакета для хранения клиентских данных с использованием буфера для полученных данных */
            DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
            System.out.println("Waiting for a client to connect...");


            while (true) {
                // сохранение данных от клиента в inputPacket
                serverSocket.receive(inputPacket);


                // десереализация команды из байт в объект
                Command command;
                try (ObjectInputStream commandInBytes = new ObjectInputStream(new ByteArrayInputStream(inputPacket.getData()))) {
                    command = (Command) commandInBytes.readObject();
                }

                //вывожу описание комманды
                System.out.println(command.toString());

                Response response = null; //todo notExistingCommandResponse

                if (command instanceof HelpCommand) {
                    String help = administration.help();
                    response = new HelpResponse(help);
                } else if(command instanceof InfoCommand) {
                    String info = administration.info();
                    response = new InfoResponse(info);
                } else if(command instanceof ShowCommand) {
                    Set<StudyGroup> groups = administration.show();
                    response = new ShowResponse(groups);
                } else if(command instanceof AddCommand) {
                    StudyGroup group = administration.add(((AddCommand) command).getGroup());
                    response = new AddResponse(group);
                } else if(command instanceof UpdateIdCommand) {
                    StudyGroup group = administration.updateId(((UpdateIdCommand) command).getGroup());
                    response = new UpdateIdResponse(group);
                } else if(command instanceof RemoveByIdCommand) {
                    StudyGroup studyGroup = administration.removeById(((RemoveByIdCommand) command).getId());
                    response = new RemoveByIdResponse(studyGroup);
                } else if(command instanceof ClearCommand) {
                    response = new ClearResponse();
                } else if(command instanceof SaveCommand) {
                    Set<StudyGroup> groups = administration.save();
                    response = new SaveResponse(groups);
                } else if(command instanceof ExecuteScriptCommand) {
                    //todo command response for script execution
                } else if(command instanceof ExitCommand) {
                    //todo command response for exit
                } else if(command instanceof AddIfMinCommand) {
                    StudyGroup studyGroup = administration.addIfMin(((AddIfMinCommand) command).getStudyGroup());
                    response = new AddIfMinResponse(studyGroup);
                } else if(command instanceof RemoveLowerCommand) {
                    Set<StudyGroup> removedGroups = administration.removeLower(((RemoveLowerCommand) command).getStudyGroup());
                    response = new RemoveLowerResponse(removedGroups);
                } else if(command instanceof HistoryCommand) {
                    //todo command response for history
                } else if(command instanceof RemoveAllByStudentsCountCommand) {
                    Set<StudyGroup> removedGroups = administration.removeAllByStudentsCount(((RemoveAllByStudentsCountCommand) command).getCount());
                    response = new RemoveAllByStudentsCountResponse(removedGroups);
                } else if(command instanceof CountByGroupAdminCommand) {
                    int count = administration.countByGroupAdmin(((CountByGroupAdminCommand) command).getGroupAdmin());
                    response = new CountByGroupAdminResponse(count);
                } else if(command instanceof FilterLessThanSemesterEnumCommand) {
                    Set<StudyGroup> studyGroupsWithLessEnum = administration.filterLessThanSemesterEnum(((FilterLessThanSemesterEnumCommand) command).getSemesterEnum());
                    response = new FilterLessThanSemesterEnumResponse(studyGroupsWithLessEnum);
                } else {
                    System.err.println("Command not supported");
                    continue;
                }

                
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


        } catch (SocketException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}