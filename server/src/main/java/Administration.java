import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import domain.*;

import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Group administration; responsible for all operations with study groups
 */
public class Administration {
    private final Set<StudyGroup> groups;
    private final Instant creationDate = Instant.now();
    private final PrintRepresentation printRepresentation = new PrintRepresentation(); // todo: move to client
    private final FileStorage fileStorage;
    private static final DateTimeFormatter BIRTHDAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Set<StudyGroup> getGroups() {
        return groups;
    }

    public Administration(String filename) {
        this.fileStorage = new FileStorage(filename);
        this.groups = readFromFile();
    }

    private Set<StudyGroup> readFromFile() {
        try {
            return fileStorage.readCSV();
        } catch (FileNotFoundException e) {
            System.err.println("File not found (" + e.getMessage() + ")");
        } catch (IOException e) {
            System.err.println("Error while reading from file: " + e.getMessage());
        } catch (CsvValidationException e) {
            System.err.println("Failed to read file: " + e.getMessage());
        } catch (FailedToParseException e) {
            System.err.println("Failed to parse file: " + e.getMessage());
        }

        return Collections.emptySet();

    }

    public StudyGroup add(StudyGroup studyGroup) {

        // todo: генерерировать id для studyGroup
        groups.add(studyGroup);
        return studyGroup;
    }

    /**
     * returns information about collection
     */
    public String info() {
        // todo: move this data to separate class and print in client
        System.out.println("Collection type: " + groups.getClass().toString());
        System.out.println("Collection creation time: " + printRepresentation.toPrint(creationDate));
        System.out.println("Elements in collection: " + groups.size());
        return "Collection type: " + groups.getClass().toString() + "\n" +
                "Collection creation time: " + printRepresentation.toPrint(creationDate) + "\n" +
                "Elements in collection: " + groups.size();
    }

    /**
     * shows all study groups in collection
     * @return study groups in collection
     */
    public Set<StudyGroup> show() {
        return groups;
    }

    public String help() {
        return HELP_CONTENTS;
    }

    /**
     * replaces old study group with other if they have the same id
     * @param other study group
     */
    public Optional<StudyGroup> updateId(StudyGroup other) {
        // fixme
        return groups.stream()
                .filter(studyGroup -> studyGroup.getId().equals(other.getId()))
                .findFirst();
    }

    /**
     * removes study group in collection by it's id
     * @param id
     * @return
     */
    public Optional<StudyGroup> removeById(Long id) {
        Optional<StudyGroup> removedGroup = groups.stream()
                .filter(studyGroup -> studyGroup.getId().equals(id))
                .findFirst();

        if (removedGroup.isPresent()) {
            groups.remove(removedGroup.get());
            return removedGroup;
        }

        return Optional.empty();
    }

    /**
     * clears collection from all study groups
     */
    public void clear() {
        groups.clear();
    }

    /**
     * adds other study group if it has lower students count than old one
     * @param other study group
     */
    public Boolean addIfMin(StudyGroup other) {//добавить группу, если в ней меньше студентов, чем в других группах
        OptionalInt min = groups.stream()
                .mapToInt(StudyGroup::getStudentsCount)
                .min();

        if (!min.isPresent() || other.getStudentsCount() < min.getAsInt()) {
            groups.add(other);
            return true;
        }

        return false;
    }

    /**
     * removes all study groups that have lower students count than old one
     * @param other study group
     * @return true if element was removed
     */
    public Set<StudyGroup> removeLower(StudyGroup other) { //удаляет из коллекции все элементы, в которых кол-во студентов меньше чем в other
        Set<StudyGroup> removedGroups = groups
                .stream()
                .filter(studyGroup -> studyGroup.getStudentsCount() < other.getStudentsCount())
                .collect(Collectors.toSet());

        groups.removeAll(removedGroups);

        return removedGroups;
    }

    /**
     * removes study group that student count is equal to count in params
     * @param count
     */
    public Set<StudyGroup> removeAllByStudentsCount(long count) {
        Set<StudyGroup> groupsToRemove = groups.stream()
                .filter(studyGroup -> studyGroup.getStudentsCount() == count)
                .collect(Collectors.toSet());

        groups.removeAll(groupsToRemove);

        return groupsToRemove;
    }

    /**
     * returns the amount of groups that have group admin from params
     * @param groupAdmin
     * @return count
     */
    public long countByGroupAdmin(Person groupAdmin) {
        return groups.stream().filter(studyGroup -> studyGroup.getGroupAdmin().equals(groupAdmin)).count();
    }

    /**
     * prints groups that have lower semester than in params
     * @param semester
     */
    public Set<StudyGroup> filterLessThanSemesterEnum(Semester semester) {//группы у которых семестер инам меньше заданного
        return groups.stream()
                .filter(studyGroup -> studyGroup.getSemesterEnum().ordinal() < semester.ordinal())
                .collect(Collectors.toSet());
    }

    /**
     * returnes true if collection has study group with id from params
     * @param id
     * @return true or false
     */
    public boolean hasElementWithId(Long id) {
        Optional<StudyGroup> groupWithId = groups.stream().filter(studyGroup -> studyGroup.getId().equals(id)).findFirst();
        return groupWithId.isPresent();
    }

    public Set<StudyGroup> save() {
        fileStorage.writeCsv(groups);
        return groups;
    }

    /**
     * class responsible for reading/writing csv file
     */
    public static class FileStorage {
        private final String filename;
        private String[] headerLine = new String[] {"id","name","coordinate.x","coordinate.y","creationDate","studentsCount","formOfEducation","semesterEnum","groupAdmin.name","groupAdmin.birthday","groupAdmin.passportId","groupAdmin.location.x","groupAdmin.location.y","groupAdmin.location.name"};

        public FileStorage(String filename) {
            this.filename = filename;
        }

        public Set<StudyGroup> readCSV() throws IOException, CsvValidationException, FailedToParseException {
            LinkedHashSet<StudyGroup> set = new LinkedHashSet<>();
            Set<Long> alreadyAddedIds = new HashSet<>();

            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filename)));
            headerLine = reader.readNext();
            if (headerLine == null) {
                // empty file
                return set;
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                long id = StudyGroup.readId(line[0]);

                if (!alreadyAddedIds.add(id)) {
                    System.err.println("id " + id + " already exists; skipping");
                    continue;
                }

                String name = StudyGroup.readName(line[1]);

                float x = Coordinates.readX(line[2]);
                int y = Coordinates.readY(line[3]);

                Coordinates coordinates = new Coordinates(x, y);

                java.util.Date creationDate = StudyGroup.readCreationDate(line[4]);

                int studentCount = StudyGroup.readStudentsCount(line[5]);

                FormOfEducation formOfEducation = StudyGroup.readFormOfEducation(line[6]);

                Semester semester = StudyGroup.readSemester(line[7]);

                Person groupAdmin = readGroupAdmin(line);

                StudyGroup studyGroup = new StudyGroup(
                        id,
                        name,
                        coordinates,
                        creationDate,
                        studentCount,
                        formOfEducation,
                        semester,
                        groupAdmin
                );

                set.add(studyGroup);
            }

            return set;
        }

        private Person readGroupAdmin(String[] line) throws FailedToParseException {
            String adminName = line[8];
            if (adminName.isEmpty()) {
                throw new FailedToParseException("admin name could not be null in a file");
            }

            LocalDate birthday = Person.readAdminBirthday(line[9]);

            String passportId = Person.readPassportID(line[10]);
            Location location = readLocation(line);

            return new Person(adminName, birthday, passportId, location);
        }

        private Location readLocation(String[] line) throws FailedToParseException {
            Integer adminLocationX = Location.readX(line[11]);
            if (adminLocationX == null) {
                throw new FailedToParseException("admin location x could not be skipped in file");
            }

            int adminLocationY = Location.readY(line[12]);

            String locName = line[13];

            return new Location(adminLocationX, adminLocationY, locName);
        }

        public void writeCsv(Set<StudyGroup> set) {
            try {
                CSVWriter writer = new CSVWriter(new PrintWriter(filename));

                if (headerLine != null) {
                    writer.writeNext(headerLine, false);
                }

                for (StudyGroup studyGroup: set) {
                    String[] line = new String[14];

                    line[0] = studyGroup.getId().toString();
                    line[1] = studyGroup.getName();

                    Coordinates coordinates = studyGroup.getCoordinates();
                    float x = coordinates.getX();
                    int y = coordinates.getY();
                    line[2] = Float.toString(x);
                    line[3] = Integer.toString(y);

                    java.util.Date creationDate = studyGroup.getCreationDate();
                    line[4] = Long.toString(creationDate.getTime());

                    int studentsCount = studyGroup.getStudentsCount();
                    line[5] = Integer.toString(studentsCount);

                    FormOfEducation formOfEducation = studyGroup.getFormOfEducation();
                    line[6] = formOfEducation.toString();

                    Semester semester = studyGroup.getSemesterEnum();
                    line[7] = semester.toString();

                    Person admin = studyGroup.getGroupAdmin();

                    if (admin != null) {
                        String adminName = admin.getName();
                        line[8] = adminName;

                        line[9] = admin.getBirthday().format(BIRTHDAY_FORMATTER);

                        String passportID = admin.getPassportID();
                        line[10] = passportID;

                        Location location = admin.getLocation();

                        if (location != null) {
                            int xL = location.getX();
                            line[11] = Integer.toString(xL);

                            int yL = location.getY();
                            line[12] = Integer.toString(yL);

                            String locName = location.getLocationName();
                            line[13] = locName;
                        }
                    }


                    writer.writeNext(line, false);
                }

                writer.close();

            } catch (FileNotFoundException e) {
                System.err.println("Unable to save file");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

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
