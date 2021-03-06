package lab;

import lab.auth.NotAuthorizedException;
import lab.domain.*;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Group administration; responsible for all operations with study groups
 */
public class Administration {
    private Set<StudyGroup> groups;
    private final DatabaseManager databaseManager;
    private final ReentrantLock locker = new ReentrantLock();

    public Administration(DatabaseManager databaseManager) throws SQLException {
        this.databaseManager = databaseManager;
        this.groups = this.databaseManager.getStudyGroups();
    }

    public Optional<Long> add(StudyGroup studyGroup, String creator) {
        long id;
        try {
            id = this.databaseManager.addGroup(studyGroup, creator);
            locker.lock();
            this.groups.add(studyGroup);
        } catch (SQLException e) {
            System.err.println("Failed to add group: " + e.getMessage());
            return Optional.empty();
        } finally {
            locker.unlock();
        }



        return Optional.of(id);
    }

    /**
     * @return study groups
     */
    public Set<StudyGroup> getGroups() {
        return groups;
    }

    /**
     * replaces old study group with other if they have the same id
     * @param other study group
     */
    public boolean updateId(StudyGroup other, String username) throws NotAuthorizedException {
        Optional<StudyGroup> groupToUpdateOptional = groups.stream()
                .filter(studyGroup -> studyGroup.getId().equals(other.getId()))
                .findFirst();

        if (!groupToUpdateOptional.isPresent()) {
            return false;
        }
        StudyGroup groupToUpdate = groupToUpdateOptional.get();

        if (!username.equals(groupToUpdate.getCreator())) {
            throw new NotAuthorizedException();
        }

        try {
            boolean wasUpdated = databaseManager.updateGroup(other);
            if (!wasUpdated) {
                return false;
            }

            locker.lock();
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
        } finally {
            locker.unlock();
        }

    }

    /**
     * removes study group in collection by it's id
     * @param id group id to remove
     * @param username of user that performs operation
     * @return true if group was removed
     */
    public boolean removeById(long id, String username) throws NotAuthorizedException {
        Optional<StudyGroup> groupToRemoveOptional = groups.stream()
                .filter(studyGroup -> studyGroup.getId().equals(id))
                .findFirst();

        if (!groupToRemoveOptional.isPresent()) {
            return false;
        }

        StudyGroup groupToRemove = groupToRemoveOptional.get();

        if (!username.equals(groupToRemove.getCreator())) {
            throw new NotAuthorizedException();
        }

        try {
            if (!databaseManager.removeStudyGroup(id)) {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error while removing group from db: " + e.getMessage());
            return false;
        }


        locker.lock();
        groups = groups.stream()
                .filter(studyGroup -> studyGroup != groupToRemove)
                .collect(Collectors.toSet());
        locker.unlock();
        return true;
    }

    /**
     * clears collection from all study groups
     * @return number of affected rows
     */
    public int clear(String username) {
        try {
            int rowCount = databaseManager.clearStudyGroups(username);

            if (rowCount > 0) {
                locker.lock();
                try {
                    groups.clear();
                } finally {
                    locker.unlock();
                }
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
    public boolean addIfMin(StudyGroup other, String creator) {//???????????????? ????????????, ???????? ?? ?????? ???????????? ??????????????????, ?????? ?? ???????????? ??????????????
        OptionalInt min = groups.stream()
                .mapToInt(StudyGroup::getStudentsCount)
                .min();

        if (min.isPresent() && other.getStudentsCount() >= min.getAsInt()) {
            return false;
        }

        try {
            databaseManager.addGroup(other, creator);
        } catch (SQLException e) {
            System.err.println("Error while adding group (if it has lowest student count): " + e.getMessage());
            return false;
        }

        locker.lock();
        groups.add(other);
        locker.unlock();
        return true;
    }

    /**
     * removes all study groups that have lower students count than old one
     * @param other study group
     * @param creator user that invokes this command
     * @return true if element was removed
     */
    public Set<StudyGroup> removeLower(StudyGroup other, String creator) {
        return removeGroups(studyGroup -> studyGroup.getStudentsCount() < other.getStudentsCount() && creator.equals(studyGroup.getCreator()));
    }

    /**
     * removes study group that student count is equal to count in params
     */
    public Set<StudyGroup> removeAllByStudentsCount(long count, String creator) {
        return removeGroups(studyGroup -> studyGroup.getStudentsCount() == count && creator.equals(studyGroup.getCreator()));
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

        locker.lock();
        groups.removeAll(groupsToRemove);
        locker.unlock();

        return groupsToRemove;

    }

    /**
     * returns the amount of groups that have group admin from params
     * @return count
     */
    public long countByGroupAdmin(Person groupAdmin) {
        return groupAdmin == null
                ? 0
                : groups.stream().filter(studyGroup -> groupAdmin.equals(studyGroup.getGroupAdmin())).count();
    }

    /**
     * prints groups that have lower semester than in params
     */
    public Set<StudyGroup> filterLessThanSemesterEnum(Semester semester) {
        return groups.stream()
                .filter(studyGroup -> studyGroup.getSemesterEnum().ordinal() < semester.ordinal())
                .collect(Collectors.toSet());
    }
}
