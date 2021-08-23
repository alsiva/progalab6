package lab.domain;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PrintRepresentation {

    private final SimpleDateFormat CREATION_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private final DateTimeFormatter BIRTHDAY_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String toPrint(StudyGroup studyGroup) {
        Person groupAdmin = studyGroup.getGroupAdmin();

        return "{" +
            "id=" + studyGroup.getId() +
            ", name='" + studyGroup.getName() + '\'' +
            ", coordinates=" + toPrint(studyGroup.getCoordinates()) +
            ", creationDate=" + toPrint(studyGroup.getCreationDate()) +
            ", studentsCount=" + studyGroup.getStudentsCount() +
            ", formOfEducation=" + studyGroup.getFormOfEducation() +
            ", semesterEnum=" + studyGroup.getSemesterEnum() +
            (groupAdmin == null ? "" : ", groupAdmin=" + toPrint(groupAdmin)) +
        '}';
    }

    public String toPrint(Coordinates coordinates) {
        return "(x=" + coordinates.getX() + ", y=" + coordinates.getY() + ")";
    }

    public String toPrint(Date date) {
        return CREATION_DATE_FORMAT.format(date);
    }

    public String toPrint(Person person) {
        Location location = person.getLocation();
        String passportID = person.getPassportID();

        return "{" +
            "name=" + person.getName() +
            ", birthday=" + person.getBirthday().format(BIRTHDAY_DATE_FORMAT) +
            ", passportId=" + (passportID == null ? "empty" : passportID) +
            ((location == null) ? "" : (", location=" + toPrint(location))) +
        "}";
    }

    public String toPrint(Location location) {
        return "{" +
            "x=" + location.getX() +
            ", y=" + location.getY() +
            ", name=" + location.getLocationName() +
        "}";
    }

    private final DateTimeFormatter instantFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone( ZoneId.systemDefault() );

    public String toPrint(Instant creationDate) {
        return instantFormatter.format(creationDate);
    }

    public static class Factory {
        public static final PrintRepresentation singleton = new PrintRepresentation();
    }
}
