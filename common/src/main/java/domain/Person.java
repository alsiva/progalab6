package domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * class that defines person
 */
public class Person implements Serializable {
    public Person(String name, LocalDate birthday, String passportID, Location location){
        this.adminName = name;
        this.birthday = birthday;
        this.passportID = passportID;
        this.location = location;
    }

    private final String adminName; //Поле не может быть null, Строка не может быть пустой
    private final LocalDate birthday; //Поле не может быть null
    private final String passportID; //Длина строки должна быть не меньше 7, Поле может быть null
    private final Location location; //Поле может быть null
    private static final DateTimeFormatter BIRTHDAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * @return person name
     */
    public String getName() {
        return adminName;
    }

    /**
     * @return person birthday
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * @return person passport id
     */
    public String getPassportID() {
        return passportID;
    }

    /**
     * @return person location
     */
    public Location getLocation() {
        return location;
    }

    public static LocalDate readAdminBirthday(String fieldAsString) throws FailedToParseException {
        LocalDate value;
        try {
            value = LocalDate.parse(fieldAsString, BIRTHDAY_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new FailedToParseException("Failed to read admin birthday: " + e.getMessage());
        }
        return value;
    }

    public static String readPassportID(String fieldAsString) throws FailedToParseException {
        String value;
        try {
            value = fieldAsString;
        } catch (IllegalArgumentException e) {
            throw new FailedToParseException("Failed to read passport id: " + e.getMessage());
        }

        if (value.length() < 7) {
            throw new FailedToParseException("Passport id length should be greater than 7");
        }

        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return adminName.equals(person.adminName) && birthday.equals(person.birthday) && Objects.equals(passportID, person.passportID) && Objects.equals(location.getLocationName(), person.location.getLocationName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminName, birthday, passportID, location);
    }
}