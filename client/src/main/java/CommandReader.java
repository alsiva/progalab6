
import commands.*;
import domain.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * reads commands from user or script
 */
public class CommandReader {
    private final BufferedReader in;
    private final Stack<String> scriptStack;

    public CommandReader(BufferedReader in) {
        this(in, new Stack<>());
    }

    private CommandReader(BufferedReader in, Stack<String> scriptStack) {
        this.in = in;
        this.scriptStack = scriptStack;
    }

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

        if (command.equals(HELP)) {
            return new HelpCommand();
        } else if (command.equals(INFO)) {
            return new InfoCommand();
        } else if (command.equals(SHOW)) {
            return new ShowCommand();
        } else if (command.equals(ADD)) {
            return new AddCommand(readStudyGroup());
        } else if (command.startsWith(UPDATE)) {
            return new UpdateIdCommand(readStudyGroup());
        } else if (command.startsWith(REMOVE_BY_ID)) {
            String idAsStr = command.substring(REMOVE_BY_ID.length()).trim();
            long id;
            try {
                id = Long.parseLong(idAsStr);
            } catch (NumberFormatException e) {
                throw new IOException("Illegal argument for remove_by_id: " + idAsStr + " is not a long", e);
            }
            return new RemoveByIdCommand(id);
        } else if (command.equals(CLEAR)) {
            return new ClearCommand();
        } else if (command.equals(ADD_IF_MIN)) {
            return new AddIfMinCommand(readStudyGroup());
        } else if (command.equals(REMOVE_LOWER)) {
            return new RemoveLowerCommand(readStudyGroup());
        } else if (command.equals(HISTORY)) {
            return new HistoryCommand();
        } else if (command.startsWith(REMOVE_ALL_BY_STUDENTS_COUNT)) {
            String countAsStr = command.substring(REMOVE_ALL_BY_STUDENTS_COUNT.length()).trim();
            long count;
            try {
                count = Long.parseLong(countAsStr);
            } catch (NumberFormatException e) {
                throw new IOException("Illegal argument for remove_all_by_students_count: " + countAsStr + " is not long", e);
            }
            return new RemoveAllByStudentsCountCommand(count);
        } else if (command.equals(COUNT_BY_GROUP_ADMIN)) {
            Person groupAdmin = readGroupAdmin();
            if (groupAdmin == null) {
                throw new IOException("Illegal argument for " + COUNT_BY_GROUP_ADMIN + ": admin should not be null");
            }
            return new CountByGroupAdminCommand(groupAdmin);
        } else if (command.startsWith(FILTER_LESS_THAN_SEMESTER_ENUM)) {
            String semesterAsString = command.substring(FILTER_LESS_THAN_SEMESTER_ENUM.length()).trim();
            Semester semester;
            try {
                semester = Semester.valueOf(semesterAsString);
            } catch (IllegalArgumentException e) {
                throw new IOException("Illegal argument for filter_less_than_semester_enum: failed to parse semester", e);
            }

            return new FilterLessThanSemesterEnumCommand(semester);

        } else if (command.equals(EXIT)) {
            return new ExitCommand();
        }

        throw new IOException("command " + command + " not recognized");
    }

    private static final Random rng = new Random();



/**
     * @return study group with random id
     * @throws IOException if readUntilSuccess fails to read from standard input
     */

    private StudyGroup readStudyGroup() throws IOException {
        return readStudyGroup(rng.nextLong());
    }


    /**
     * @param id auto-generated id
     * @return study group with defined id
     * @throws IOException if readUntilSuccess fails to read from standard input
     */
    private StudyGroup readStudyGroup(long id) throws IOException {
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
                id,
                name,
                coordinates,
                creationDate,
                studentsCount,
                formOfEducation,
                semester,
                groupAdmin
        );
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

        return new Person(adminName, adminBirthday, passportId, location);
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

        return new Location(locationX, locationY, locationName);
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

    public static final DateTimeFormatter BIRTHDAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String HELP_CONTENTS = "" +
            "help : вывести справку по доступным командам\n" +
            "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
            "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
            "add {element} : добавить новый элемент в коллекцию\n" +
            "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
            "remove_by_id id : удалить элемент из коллекции по его id\n" +
            "clear : очистить коллекцию\n" +
            "save : сохранить коллекцию в файл\n" +
            "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
            "exit : завершить программу (без сохранения в файл)\n" +
            "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
            "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
            "history : вывести последние 10 команд (без их аргументов)\n" +
            "remove_all_by_students_count studentsCount : удалить из коллекции все элементы, значение поля studentsCount которого эквивалентно заданному\n" +
            "count_by_group_admin groupAdmin : вывести количество элементов, значение поля groupAdmin которых равно заданному\n" +
            "filter_less_than_semester_enum semesterEnum : вывести элементы, значение поля semesterEnum которых меньше заданного";
}
