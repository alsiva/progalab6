import commands.Command;
import commands.ExitCommand;
import domain.PrintRepresentation;
import domain.StudyGroup;
import response.*;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Optional;
import java.util.Set;

public class Client {
    /* Порт сервера, к которому собирается подключиться клиентский сокет */
    public final static int SERVICE_PORT = 50001;

    public static void main(String[] args) {
        CommandReader commandReader = new CommandReader(new BufferedReader(new InputStreamReader(System.in)));
        PrintRepresentation printRepresentation = new PrintRepresentation();

        /* Создайте экземпляр клиентского сокета. Нет необходимости в привязке к определенному порту */
        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.connect(new InetSocketAddress("localhost", SERVICE_PORT));

            while (true) {
                Command command = commandReader.readCommand();

                if (command == null) {
                    continue;
                }

                if (command instanceof ExitCommand) {
                    System.out.println("Received exit command; terminating.");
                    break;
                }

                //Сериализация команды
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try (ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream)) {
                    objectOutput.writeObject(command);
                }

                byte[] serializedCommand = byteArrayOutputStream.toByteArray();


                ByteBuffer sendingBuffer = ByteBuffer.wrap(serializedCommand);
                channel.write(sendingBuffer);

                // Send data to server

                //Receive data from server
                ByteBuffer receivingBuffer = ByteBuffer.allocate(1024*1024);
                channel.read(receivingBuffer);

                Object response = null;//todo это сделать нормально
                try (ObjectInputStream responseInBytes = new ObjectInputStream(new ByteArrayInputStream(receivingBuffer.array()))) {
                    response = responseInBytes.readObject();
                } catch (ClassNotFoundException e) {//todo мне кажется что здесь не очень, но пока не уверен что
                    e.printStackTrace();
                }

                //response
                if (response instanceof HelpResponse) {
                    System.out.println(((HelpResponse) response).getResponse());
                } else if (response instanceof InfoResponse) {
                    System.out.println(((InfoResponse) response).getResponse());
                } else if (response instanceof ShowResponse) {
                    Set<StudyGroup> groups = ((ShowResponse) response).getGroups();
                    for (StudyGroup studyGroup: groups) {
                        System.out.println(printRepresentation.toPrint(studyGroup));
                    }
                } else if (response instanceof AddResponse) {
                    System.out.println("Added \n" + printRepresentation.toPrint(((AddResponse) response).getGroup()));
                } else if (response instanceof UpdateIdResponse) {
                    if (((UpdateIdResponse) response).getGroup().isPresent()) {
                        System.out.println("Added \n" + printRepresentation.toPrint(((UpdateIdResponse) response).getGroup().get()));
                    } else  {
                        System.out.println("No element with this id");
                    }
                } else if (response instanceof RemoveByIdResponse) {
                    Optional<StudyGroup> removedStudyGroup = ((RemoveByIdResponse) response).getStudyGroup();
                    if (removedStudyGroup.isPresent()) {
                        System.out.println("Removed \n" + printRepresentation.toPrint(removedStudyGroup.get()));
                    } else {
                        System.out.println("Nothing was removed");
                    }
                } else if (response instanceof ClearResponse) {
                    System.out.println("Collection was cleared");
                } else if (response instanceof ExecuteScriptResponse) {
                    //todo add executeScriptResponse
                } else if (response instanceof AddIfMinResponse) {
                    Boolean wasAdded = ((AddIfMinResponse) response).getWasAdded();
                    if (wasAdded) {
                        System.out.println("Group was added");
                    } else {
                        System.out.println("Group wasn't added");
                    }
                } else if (response instanceof RemoveLowerResponse) {
                    Set<StudyGroup> groups = ((RemoveLowerResponse) response).getGroups();
                    System.out.println("Removed");
                    for (StudyGroup studyGroup: groups) {
                        System.out.println(printRepresentation.toPrint(studyGroup));
                    }
                } else if (response instanceof RemoveAllByStudentsCountResponse) {
                    Set<StudyGroup> groups = ((RemoveAllByStudentsCountResponse) response).getRemovedGroups();
                    System.out.println("Removed");
                    for (StudyGroup studyGroup: groups) {
                        System.out.println(printRepresentation.toPrint(studyGroup));
                    }
                } else if (response instanceof CountByGroupAdminResponse) {
                    long count = ((CountByGroupAdminResponse) response).getCount();
                    System.out.println("Groups with this admin " + count);
                } else if (response instanceof FilterLessThanSemesterEnumResponse) {
                    Set<StudyGroup> groups = ((FilterLessThanSemesterEnumResponse) response).getStudyGroupWithLessSemester();
                    System.out.println("Groups with lessEnum");
                    for (StudyGroup studyGroup: groups) {
                        System.out.println(printRepresentation.toPrint(studyGroup));
                    }
                }

            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}