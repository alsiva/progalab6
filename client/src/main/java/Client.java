import commands.Command;
import domain.PrintRepresentation;
import domain.StudyGroup;
import response.ShowResponse;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Set;

public class Client {
    /* Порт сервера, к которому собирается
  подключиться клиентский сокет */
    public final static int SERVICE_PORT = 50001;

    public static void main(String[] args) {

        CommandReader commandReader = new CommandReader(new BufferedReader(new InputStreamReader(System.in)));
        PrintRepresentation printRepresentation = new PrintRepresentation();

        /* Создайте экземпляр клиентского сокета. Нет необходимости в привязке к определенному порту */
        try (DatagramSocket clientSocket = new DatagramSocket()) {

            // Получите IP-адрес сервера
            InetAddress IPAddress = InetAddress.getByName("localhost");

            while (true) {
                Command command = commandReader.readCommand();

                if (command == null) {
                    break;
                }

                //Сериализация команды
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try (ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream)) {
                    objectOutput.writeObject(command);
                }

                byte[] serializedCommand = byteArrayOutputStream.toByteArray();

                // Создайте UDP-пакет
                DatagramPacket sendingPacket = new DatagramPacket(serializedCommand, serializedCommand.length, IPAddress, SERVICE_PORT);

                // Отправьте UDP-пакет серверу
                clientSocket.send(sendingPacket);

                byte[] receivingDataBuffer = new byte[1024 * 1024];

                // Получите ответ от сервера
                DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
                clientSocket.receive(receivingPacket);

                Object response = null;//todo это сделать нормально
                try (ObjectInputStream responseInBytes = new ObjectInputStream(new ByteArrayInputStream(receivingPacket.getData()))) {
                    response = responseInBytes.readObject();
                } catch (ClassNotFoundException e) {//todo мне кажется что здесь не очень, но пока не уверен что
                    e.printStackTrace();
                }
                //дохущиа elseif для получения нужной строки от response
                if (response instanceof ShowResponse) {
                    Set<StudyGroup> groups = ((ShowResponse) response).getGroups();
                    for (StudyGroup studyGroup: groups) {
                        printRepresentation.toPrint(studyGroup);
                    }
                }

                // Выведите на экране полученные данные

                System.out.println("Sent from the server: ");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}