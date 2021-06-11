import domain.*;

import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Group administration; responsible for all operations with study groups
 */
public class Administration {
    private Set<StudyGroup> groups;
    private final DatabaseManager databaseManager;

    public Administration(DatabaseManager databaseManager) throws SQLException {
        this.databaseManager = databaseManager;
        this.groups = this.databaseManager.getStudyGroups();
    }

    public Optional<Long> add(StudyGroup studyGroup) {
        Optional<Long> idOptional;
        try {
            idOptional = this.databaseManager.addGroup(studyGroup);
        } catch (SQLException e) {
            System.err.println("Failed to add group: " + e.getMessage());
            return Optional.empty();
        }

        if (idOptional.isPresent()) {
            this.groups.add(studyGroup);
        }
        return idOptional;
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
        Optional<StudyGroup> groupToUpdateOptional = groups.stream()
                .filter(studyGroup -> studyGroup.getId().equals(other.getId()))
                .findFirst();

        if (!groupToUpdateOptional.isPresent()) {
            return false;
        }
        StudyGroup groupToUpdate = groupToUpdateOptional.get();

        try {
            boolean wasUpdated = databaseManager.updateGroup(other);
            if (!wasUpdated) {
                return false;
            }
            groups = groups.stream()
                    .map(studyGroup -> studyGroup == groupToUpdate
                            ? other
                            : studyGroup
                    )
                    .collect(Collectors.toSet());

            return true;
        } catch (SQLException e) {
            System.err.println("Error while saving group to db: " + e.getMessage());
            return false;
        }

    }

    /**
     * removes study group in collection by it's id
     * @param id
     * @return true if group was removed
     */
    public boolean removeById(long id) {
        Optional<StudyGroup> groupToRemoveOptional = groups.stream()
                .filter(studyGroup -> studyGroup.getId().equals(id))
                .findFirst();

        if (!groupToRemoveOptional.isPresent()) {
            return false;
        }

        StudyGroup groupToRemove = groupToRemoveOptional.get();

        try {
            if (!databaseManager.removeStudyGroup(id)) {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error while removing group from db: " + e.getMessage());
            return false;
        }

        groups = groups.stream()
                .filter(studyGroup -> studyGroup != groupToRemove)
                .collect(Collectors.toSet());

        return true;
    }

    /**
     * clears collection from all study groups
     * @return
     */
    public int clear() {
        try {
            int rowCount = databaseManager.clearStudyGroups();

            if (rowCount > 0) {
                groups.clear();
            }
            return rowCount;
        } catch (SQLException e) {
            System.err.println("Error while clearing collection: " + e.getMessage());
            return 0;
        }
    }

    /**
     * adds other study group if it has lower students count than old one
     * @param other study group
     */
    public boolean addIfMin(StudyGroup other) {//добавить группу, если в ней меньше студентов, чем в других группах
        OptionalInt min = groups.stream()
                .mapToInt(StudyGroup::getStudentsCount)
                .min();

        if (min.isPresent() && other.getStudentsCount() >= min.getAsInt()) {
            return false;
        }

        try {
            if (!databaseManager.addGroup(other).isPresent()) {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error while adding group (if it has lowest student count): " + e.getMessage());
            return false;
        }

        groups.add(other);
        return true;
    }

    /**
     * removes all study groups that have lower students count than old one
     * @param other study group
     * @return true if element was removed
     */
    public Set<StudyGroup> removeLower(StudyGroup other) {
        return removeGroups(studyGroup -> studyGroup.getStudentsCount() < other.getStudentsCount());
    }

    /**
     * removes study group that student count is equal to count in params
     * @param count
     */
    public Set<StudyGroup> removeAllByStudentsCount(long count) {
        return this.removeGroups(studyGroup -> studyGroup.getStudentsCount() == count);
    }

    private Set<StudyGroup> removeGroups(Predicate<StudyGroup> condition) {
        Set<StudyGroup> groupsToRemove = groups.stream()
                .filter(condition)
                .collect(Collectors.toSet());

        Set<Long> studyGroupIds = groupsToRemove.stream()
                .map(StudyGroup::getId)
                .collect(Collectors.toSet());

        try {
            if (!databaseManager.removeGroups(studyGroupIds)) {
                return Collections.emptySet();
            }
        } catch (SQLException e) {
            System.out.println("Failed to remove groups: " + e.getMessage());
            return Collections.emptySet();
        }

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
