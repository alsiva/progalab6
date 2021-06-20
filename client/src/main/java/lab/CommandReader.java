package lab;

import lab.auth.Credentials;
import lab.commands.*;
import lab.domain.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * reads commands from user or script
 */
public class CommandReader {
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    private static final String LOGIN = "login";
    private static final String REGISTER = "register";
    private static final String HELP = "help";
    private static final String INFO = "info";
    private static final String SHOW = "show";
    private static final String ADD = "add";
    private final static String UPDATE = "update";
    private final static String REMOVE_BY_ID = "remove_by_id";
    private static final String CLEAR = "clear";
    private static final String ADD_IF_MIN = "add_if_min";
    private static final String REMOVE_LOWER = "remove_lower";
    private static final String HISTORY = "history";
    private static final String REMOVE_ALL_BY_STUDENTS_COUNT = "remove_all_by_students_count";
    private static final String COUNT_BY_GROUP_ADMIN = "count_by_group_admin";
    private static final String FILTER_LESS_THAN_SEMESTER_ENUM = "filter_less_than_semester_enum";
    private static final String EXIT = "exit";

    /**
     * @throws IOException
     * method that reads string commands from user or script
     */
    public Command readCommand() throws IOException {
        String command = in.readLine();
        if (command == null) {
            throw new IOException("Command not entered");
        }

        if (command.equals(EXIT)) {
            return new ExitCommand();
        }
        if (command.equals(HELP)) {
            return new HelpCommand();
        }
        if (command.equals(HISTORY)) {
            return new HistoryCommand();
        }
        if (command.equals(REGISTER)) {
            return new RegisterCommand(readCredentials());
        }
        if (command.equals(LOGIN)) {
            return new CheckCredentialsCommand(readCredentials());
        }

        // all further commands require authorization
        if (command.equals(INFO)) {
            return new InfoCommand();
        }
        if (command.equals(SHOW)) {
            return new ShowCommand();
        }
        if (command.equals(ADD)) {
            return new AddCommand(readStudyGroup());
        }
        if (command.equals(UPDATE)) {
            return new UpdateIdCommand(readStudyGroupWithId());
        }
        if (command.startsWith(REMOVE_BY_ID)) {
            String idAsStr = command.substring(REMOVE_BY_ID.length()).trim();
            long id;
            try {
                id = Long.parseLong(idAsStr);
            } catch (NumberFormatException e) {
                throw new IOException("Illegal argument for remove_by_id: " + idAsStr + " is not a long", e);
            }
            return new RemoveByIdCommand(id);
        }
        if (command.equals(CLEAR)) {
            return new ClearCommand();
        }
        if (command.equals(ADD_IF_MIN)) {
            return new AddIfMinCommand(readStudyGroup());
        }
        if (command.equals(REMOVE_LOWER)) {
            return new RemoveLowerCommand(readStudyGroup());
        }
        if (command.startsWith(REMOVE_ALL_BY_STUDENTS_COUNT)) {
            String countAsStr = command.substring(REMOVE_ALL_BY_STUDENTS_COUNT.length()).trim();
            long count;
            try {
                count = Long.parseLong(countAsStr);
            } catch (NumberFormatException e) {
                throw new IOException("Illegal argument for remove_all_by_students_count: " + countAsStr + " is not long", e);
            }
            return new RemoveAllByStudentsCountCommand(count);
        }
        if (command.equals(COUNT_BY_GROUP_ADMIN)) {
            Person groupAdmin = readGroupAdmin();
            if (groupAdmin == null) {
                throw new IOException("Illegal argument for " + COUNT_BY_GROUP_ADMIN + ": admin should not be null");
            }
            return new CountByGroupAdminCommand(groupAdmin);
        }
        if (command.startsWith(FILTER_LESS_THAN_SEMESTER_ENUM)) {
            String semesterAsString = command.substring(FILTER_LESS_THAN_SEMESTER_ENUM.length()).trim();
            Semester semester;
            try {
                semester = Semester.valueOf(semesterAsString);
            } catch (IllegalArgumentException e) {
                throw new IOException("Illegal argument for filter_less_than_semester_enum: failed to parse semester", e);
            }

            return new FilterLessThanSemesterEnumCommand(semester);

        }

        throw new IOException("command " + command + " not recognized");
    }

    private StudyGroup readStudyGroupWithId() throws IOException {
        System.out.println("Please enter id");
        long id = readUntilSuccess(StudyGroup::readId);

        StudyGroup studyGroup = readStudyGroup();
        studyGroup.setId(id);

        return studyGroup;
    }

    /**
     *
     * @return study group with defined id
     * @throws IOException if readUntilSuccess fails to read from standard input
     */
    private StudyGroup readStudyGroup() throws IOException {
        System.out.println("Please enter name");
        String name = readUntilSuccess(StudyGroup::readName);

        Coordinates coordinates = readCoordinates();

        Date creationDate = new Date(); // creation date is now

        System.out.println("Please enter students count");
        int studentsCount = readUntilSuccess(StudyGroup::readStudentsCount);

        List<String> forms = new ArrayList<>();
        for (FormOfEducation value : FormOfEducation.values()) {
            forms.add(value.toString());
        }

        System.out.println("Please enter form of education (" + String.join(", ", forms) + "), leave empty to skip");
        FormOfEducation formOfEducation = readUntilSuccess(StudyGroup::readFormOfEducation);

        List<String> semesters = new ArrayList<>();
        for (Semester semester : Semester.values()) {
            semesters.add(semester.toString());
        }

        System.out.println("Please enter semester (" + String.join(", ", semesters) + "), leave empty to skip");
        Semester semester = readUntilSuccess(StudyGroup::readSemester);

        Person groupAdmin = readGroupAdmin();

        return new StudyGroup(
                null,
                name,
                coordinates,
                creationDate,
                studentsCount,
                formOfEducation,
                semester,
                groupAdmin,
                null
        );
    }

    private Credentials readCredentials() throws IOException {
        System.out.println("Please enter username");
        String username = readUntilSuccess(Credentials::readUsername);

        System.out.println("Please enter password");
        String password = readUntilSuccess(Credentials::readPassword);

        return new Credentials(username, password);
    }

    /**
     * reads coordinates from user
     * @return coordinates
     * @throws IOException if readUntilSuccess fails to read from standard input
     */
    private Coordinates readCoordinates() throws IOException {
        System.out.println("Please enter coordinate x");
        float x = readUntilSuccess(Coordinates::readX);

        System.out.println("Please enter coordinate y");
        int y = readUntilSuccess(Coordinates::readY);

        return new Coordinates(x, y);
    }



    /**
     * reads group admin from user
     * @return group admin
     * @throws IOException if readUntilSuccess fails to read from standard input
     */
    private Person readGroupAdmin() throws IOException {
        System.out.println("Please enter admin name, leave empty to skip");
        String adminName = readUntilSuccess();
        if (adminName.isEmpty()) {
            return null;
        }

        System.out.println("Please enter admin birthday");
        LocalDate adminBirthday = readUntilSuccess(Person::readAdminBirthday);

        System.out.println("Please enter passport id");
        String passportId = readUntilSuccess(Person::readPassportID);

        Location location = readLocation();

        return new Person(null, adminName, adminBirthday, passportId, location);
    }

    /**
     * reads location from user
     * @return location
     * @throws IOException if readUntilSuccess fails to read from standard input
     */
    private Location readLocation() throws IOException {
        System.out.println("Please enter location x, leave empty to skip");
        Integer locationX = readUntilSuccess(Location::readX);

        if (locationX == null) {
            return null;
        }

        System.out.println("Please enter location y");
        int locationY = readUntilSuccess(Location::readY);

        System.out.println("Please enter location name");
        String locationName = readUntilSuccess();

        return new Location(null, locationX, locationY, locationName);
    }

    private <T> T readUntilSuccess(Parser<T> parser) throws IOException {
        while (true) {
            try {
                String field = in.readLine().trim();
                return parser.parse(field);
            } catch (FailedToParseException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private String readUntilSuccess() throws IOException {
        return readUntilSuccess(str -> str);
    }

    public static final String HELP_CONTENTS = "" +
            HELP + ": вывести справку по доступным командам\n" +
            REGISTER + ": зарегистрировать нового пользователя\n" +
            LOGIN + ": войти под существующим пользователем\n" +
            INFO + ": вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
            SHOW + ": вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
            ADD + " {element}: добавить новый элемент в коллекцию\n" +
            UPDATE + " {element}: обновить значение элемента коллекции, id которого равен заданному\n" +
            REMOVE_BY_ID + "{id}: удалить элемент из коллекции по его id\n" +
            CLEAR + ": очистить коллекцию\n" +
            EXIT + ": завершить программу\n" +
            ADD_IF_MIN + " {element}: добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
            REMOVE_LOWER + " {element}: удалить из коллекции все элементы, меньшие, чем заданный\n" +
            HISTORY + ": вывести последние 10 команд (без их аргументов)\n" +
            REMOVE_ALL_BY_STUDENTS_COUNT + " {studentsCount}: удалить из коллекции все элементы, значение поля studentsCount которого эквивалентно заданному\n" +
            COUNT_BY_GROUP_ADMIN + " {groupAdmin}: вывести количество элементов, значение поля groupAdmin которых равно заданному\n" +
            FILTER_LESS_THAN_SEMESTER_ENUM + " {semester}: вывести элементы, значение поля semesterEnum которых меньше заданного";
}
