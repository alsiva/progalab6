import com.opencsv.exceptions.CsvValidationException;
import domain.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Group administration; responsible for all operations with study groups
 */
public class Administration {
    private Set<StudyGroup> groups;
    private final FileStorage fileStorage;

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

    private static final Random rng = new Random();

    public long add(StudyGroup studyGroup) {
        Set<Long> existingIds = new HashSet<>();

        for (StudyGroup group: groups) {
            existingIds.add(group.getId());
        }

        long id;
        do {
            id = rng.nextLong();
        } while (existingIds.contains(id));

        studyGroup.setId(id);
        groups.add(studyGroup);
        return id;
    }

    /**
     * @return study groups
     */
    public Set<StudyGroup> getGroups() {
        return groups;
    }

    public String help() {
        return HELP_CONTENTS;
    }

    /**
     * replaces old study group with other if they have the same id
     * @param other study group
     */
    public boolean updateId(StudyGroup other) {
        groups = groups.stream()
                .map(studyGroup -> studyGroup.getId().equals(other.getId())
                        ? other
                        : studyGroup
                )
                .collect(Collectors.toSet());

        return groups.contains(other);

    }

    /**
     * removes study group in collection by it's id
     * @param id
     * @return true if group was removed
     */
    public boolean removeById(Long id) {
        Optional<StudyGroup> removedGroup = groups.stream()
                .filter(studyGroup -> studyGroup.getId().equals(id))
                .findFirst();

        if (removedGroup.isPresent()) {
            groups.remove(removedGroup.get());
            return true;
        }

        return false;
    }

    /**
     * clears collection from all study groups
     * @return
     */
    public int clear() {
        int size = groups.size();
        groups.clear();
        return size;
    }

    /**
     * adds other study group if it has lower students count than old one
     * @param other study group
     */
    public boolean addIfMin(StudyGroup other) {//добавить группу, если в ней меньше студентов, чем в других группах
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
    public Set<StudyGroup> removeLower(StudyGroup other) {
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
    public Set<StudyGroup> filterLessThanSemesterEnum(Semester semester) {
        return groups.stream()
                .filter(studyGroup -> studyGroup.getSemesterEnum().ordinal() < semester.ordinal())
                .collect(Collectors.toSet());
    }

    public void save() {
        fileStorage.writeCsv(groups);
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
