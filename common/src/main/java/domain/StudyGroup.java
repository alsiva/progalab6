package domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Class that defines study group
 */
public class StudyGroup implements Comparable<StudyGroup>, Serializable {
    private final long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    private final java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final int studentsCount; //Значение поля должно быть больше 0, Поле не может быть null
    private final FormOfEducation formOfEducation; //Поле не может быть null
    private final Semester semesterEnum; //Поле не может быть null
    private final Person groupAdmin; //Поле может быть null

    public StudyGroup(
            long id,
            String name,
            Coordinates coordinates,
            Date creationDate,
            Integer studentsCount,
            FormOfEducation formOfEducation,
            Semester semesterEnum,
            Person groupAdmin
    ) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.studentsCount = studentsCount;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.groupAdmin = groupAdmin;
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
        return this.groupAdmin;
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

}




