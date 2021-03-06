package lab.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.function.Function;

/**
 * Class that defines study group
 */
public class StudyGroup implements Comparable<StudyGroup>, Serializable {
    /**
     * Может быть null только при генерации на клиенте
     * Значение поля должно быть уникальным, больше 0 и генерироваться автоматически в БД
     */
    private Long id;
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    private final java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final int studentsCount; //Значение поля должно быть больше 0, Поле не может быть null
    private final FormOfEducation formOfEducation; //Поле не может быть null
    private final Semester semesterEnum; //Поле не может быть null
    private final Person groupAdmin; //Поле может быть null
    private String creator; //Пользователь, создавший группу. Может быть null только при генерации на клиенте

    public StudyGroup(
            Long id,
            String name,
            Coordinates coordinates,
            Date creationDate,
            Integer studentsCount,
            FormOfEducation formOfEducation,
            Semester semesterEnum,
            Person groupAdmin,
            String creator
    ) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.studentsCount = studentsCount;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.groupAdmin = groupAdmin;
        this.creator = creator;
    }

    /**
     *
     * @return id of group
     */
    public Long getId() {
        return this.id;
    }

    /**
     * @return group name
     */
    public String getName() { return this.name; }

    /**
     * @return amount of students in group
     */
    public int getStudentsCount() { return this.studentsCount; }

    /**
     *
     * @return coordinates that contains x and y
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    public float getCoordinateX() {
        return coordinates.getX();
    }

    public int getCoordinateY() {
        return coordinates.getY();
    }

    public Person getAdmin() {
        return groupAdmin;
    }

    public String getAdminName() {
        return getGroupAdminField(Person::getName);
    }

    public LocalDate getAdminBirthday() {
        return getGroupAdminField(Person::getBirthday);
    }

    public String getAdminPassportId() {
        return getGroupAdminField(Person::getPassportID);
    }

    public Integer getLocationX() {
        return getLocationField(Location::getX);
    }

    public Integer getLocationY() {
        return getLocationField(Location::getY);
    }

    private <T> T getGroupAdminField(Function<Person, T> mapper) {
        return groupAdmin == null
            ? null
            : mapper.apply(groupAdmin);
    }

    private <T> T getLocationField(Function<Location, T> mapper) {
        Location location = getGroupAdminField(Person::getLocation);
        return location == null
            ? null
            : mapper.apply(location);
    }

    public String getLocationName() {
        return getLocationField(Location::getLocationName);
    }

    /**
     * @return creation date of study group
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @return group form of education
     */
    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    /**
     * @return group semester
     */
    public Semester getSemesterEnum() {
        return semesterEnum;
    }

    /**
     * @return group admin
     */
    public Person getGroupAdmin() {
        return groupAdmin;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int compareTo(StudyGroup other) {
        return Integer.compare(studentsCount, other.studentsCount);
    }

    public static long readId(String fieldAsString) throws FailedToParseException {
        try {
            return Long.parseLong(fieldAsString);
        } catch (IllegalArgumentException e) {
            throw new FailedToParseException("Failed to read id: " + e.getMessage());
        }
    }

    public static String readName(String fieldAsString) throws FailedToParseException {
        if (fieldAsString.isEmpty()) {
            throw new FailedToParseException("name could not be empty");
        }
        return fieldAsString;
    }

    public static int readStudentsCount(String fieldAsString) throws FailedToParseException {
        int value;
        try {
            value = Integer.parseInt(fieldAsString);
        } catch (NumberFormatException e) {
            throw new FailedToParseException("Failed to read student count: " + e.getMessage());
        }

        if (value <= 0) {
            throw new FailedToParseException("Students count should be greater than 0");
        }

        return value;
    }

    public static Date readCreationDate(String fieldAsString) throws FailedToParseException {
        try {
            return new Date(Long.parseLong(fieldAsString));
        } catch (NumberFormatException e) {
            throw new FailedToParseException("Failed to read creation date: " + e.getMessage());
        }
    }

    public static FormOfEducation readFormOfEducation(String fieldAsString) throws FailedToParseException {
        FormOfEducation value;
        try {
            value = FormOfEducation.valueOf(fieldAsString);
        } catch (IllegalArgumentException e) {
            throw new FailedToParseException("Failed to read form of education: " + e.getMessage());
        }
        return value;
    }

    public static Semester readSemester(String fieldAsString) throws FailedToParseException {
        Semester value;
        try {
            value = Semester.valueOf(fieldAsString);
        } catch (IllegalArgumentException e) {
            throw new FailedToParseException("Failed to read semester: " + e.getMessage());
        }
        return value;
    }

    public String getCreator() {
        return this.creator;
    }
}