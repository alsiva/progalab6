import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import domain.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * class responsible for reading/writing csv file
 */
public class FileStorage {
    private final String filename;
    private String[] headerLine = new String[] {"id","name","coordinate.x","coordinate.y","creationDate","studentsCount","formOfEducation","semesterEnum","groupAdmin.name","groupAdmin.birthday","groupAdmin.passportId","groupAdmin.location.x","groupAdmin.location.y","groupAdmin.location.name"};
    private static final DateTimeFormatter BIRTHDAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

        } catch (IOException e) {
            System.err.println("Unable to save file");
        }
    }
}
