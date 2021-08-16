package lab;

import lab.domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.*;

public class DatabaseManager {
    private final Configuration configuration;

    public DatabaseManager(Configuration configuration) {
        this.configuration = configuration;
    }

    private Connection connect() throws SQLException {
        Properties props = new Properties();
        props.put("user", configuration.username);
        props.put("password", configuration.password);

        String url = "jdbc:postgresql://" + configuration.host + ":" + configuration.port + "/" + configuration.database;
        return DriverManager.getConnection(url, props);
    }

    public void createNewUser(String username, String hashedPassword) throws SQLException {
        try (Connection conn = connect(); PreparedStatement statement = conn.prepareStatement(
            "INSERT INTO USERS(USERNAME, PASSWORD_HASHED) values(?, ?)"
        )) {
            statement.setString(1, username);
            statement.setString(2, hashedPassword);

            statement.executeUpdate();
        }
    }

    public boolean checkCredentials(String username, String hashedPassword) throws SQLException {
        try (Connection conn = connect(); PreparedStatement statement = conn.prepareStatement(
                "select * from USERS where USERNAME = ? and PASSWORD_HASHED = ?;"
        )) {
            statement.setString(1, username);
            statement.setString(2, hashedPassword);

            ResultSet rs = statement.executeQuery();
            return rs.next();
        }
    }

    public Set<StudyGroup> getStudyGroups() throws SQLException {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            Map<Integer, Location> locationById = new HashMap<>();
            try (ResultSet rs = stmt.executeQuery("select * from location")) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int x = rs.getInt("x");
                    int y = rs.getInt("y");
                    String locationName = rs.getString("name");

                    locationById.put(id, new Location(id, x, y, locationName));
                }
            }

            Map<Integer, Person> peopleById = new HashMap<>();
            try (ResultSet rs = stmt.executeQuery("select * from person")) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    LocalDate birthday = rs.getObject("birthday", LocalDate.class);
                    String passport = rs.getString("passport");

                    Integer locationId = rs.getObject("location_id", Integer.class);
                    Location location = locationById.get(locationId);

                    peopleById.put(id, new Person(id, name, birthday, passport, location));
                }
            }


            Set<StudyGroup> groups = new HashSet<>();

            try (ResultSet rs = stmt.executeQuery("select * from study_group")) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    String name = rs.getString("name");

                    float x = rs.getFloat("coordinate_x");
                    int y = rs.getInt("coordinate_y");
                    Coordinates coordinates = new Coordinates(x, y);

                    Date creationDate = rs.getObject("creation_date", Date.class);
                    int studentCount = rs.getInt("student_count");
                    FormOfEducation formOfEducation = FormOfEducation.valueOf(rs.getString("form_of_education"));
                    Semester semester = Semester.valueOf(rs.getString("semester"));

                    Integer groupAdminId = rs.getObject("group_admin_id", Integer.class);
                    Person groupAdmin = peopleById.get(groupAdminId);
                    String creator = rs.getString("creator");

                    groups.add(new StudyGroup(id, name, coordinates, creationDate, studentCount, formOfEducation, semester, groupAdmin, creator));
                }

                return groups;
            }
        }
    }

    public long addGroup(StudyGroup studyGroup, String creator) throws SQLException {
        try (Connection conn = connect()) {
            return saveStudyGroup(conn, studyGroup, creator);
        }
    }

    private long saveStudyGroup(Connection conn, StudyGroup studyGroup, String creator) throws SQLException {
        Long studyGroupId = studyGroup.getId();
        if (studyGroupId != null) {
            // it's already in db
            return studyGroupId;
        }

        Integer groupAdminId = savePerson(conn, studyGroup.getGroupAdmin());

        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO STUDY_GROUP" +
                        " (NAME, COORDINATE_X, COORDINATE_Y, CREATION_DATE, STUDENT_COUNT, FORM_OF_EDUCATION, SEMESTER, GROUP_ADMIN_ID, CREATOR)" +
                        " values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, studyGroup.getName());
            statement.setFloat(2, studyGroup.getCoordinates().getX());
            statement.setInt(3, studyGroup.getCoordinates().getY());
            statement.setDate(4, new java.sql.Date(studyGroup.getCreationDate().getTime()));
            statement.setInt(5, studyGroup.getStudentsCount());
            statement.setObject(6, studyGroup.getFormOfEducation(), Types.OTHER);
            statement.setObject(7, studyGroup.getSemesterEnum(), Types.OTHER);
            statement.setObject(8, groupAdminId);
            statement.setString(9, creator);

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    studyGroup.setId(id);
                    studyGroup.setCreator(creator);
                    return id;
                }

                throw new SQLException("Failed to save study group to db");
            }
        }
    }


    private Integer savePerson(Connection conn, Person person) throws SQLException {
        if (person == null) {
            return null;
        }

        Integer groupAdminId = person.getId();

        if (groupAdminId != null) {
            // it is already in database
            return groupAdminId;
        }

        // it was not persisted to database yet
        Integer locationId = saveLocation(conn, person.getLocation());

        try (PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO PERSON (NAME, BIRTHDAY, PASSPORT, LOCATION_ID) values (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, person.getName());
            statement.setObject(2, person.getBirthday());
            statement.setString(3, person.getPassportID());
            statement.setObject(4, locationId);

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    person.setId(id);
                    return id;
                }

                throw new SQLException("Failed to insert person to db");
            }
        }
    }

    private Integer saveLocation(Connection conn, Location location) throws SQLException {
        if (location == null) {
            return null;
        }

        Integer locationId = location.getId();
        if (locationId != null) {
            // it is already in database
            return locationId;
        }

        try (PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO LOCATION (name, x, y) values (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, location.getLocationName());
            statement.setInt(2, location.getX());
            statement.setInt(3, location.getY());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    location.setId(id);
                    return id;
                }
            }

            throw new SQLException("Failed to insert location");
        }
    }

    public boolean updateGroup(StudyGroup studyGroup) throws SQLException {
        Long id = studyGroup.getId();
        if (id == null) {
            System.err.println("Group is not saved to db yet");
            return false;
        }

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("UPDATE STUDY_GROUP SET" +
                     " NAME = ?, COORDINATE_X = ?, COORDINATE_Y = ?, CREATION_DATE = ?, STUDENT_COUNT = ?, FORM_OF_EDUCATION = ?, SEMESTER = ?, GROUP_ADMIN_ID = ?" +
                     " WHERE ID = ?")
        ) {
            statement.setString(1, studyGroup.getName());
            statement.setFloat(2, studyGroup.getCoordinates().getX());
            statement.setInt(3, studyGroup.getCoordinates().getY());
            statement.setDate(4, new java.sql.Date(studyGroup.getCreationDate().getTime()));
            statement.setInt(5, studyGroup.getStudentsCount());
            statement.setObject(6, studyGroup.getFormOfEducation(), Types.OTHER);
            statement.setObject(7, studyGroup.getSemesterEnum(), Types.OTHER);

            Person groupAdmin = studyGroup.getGroupAdmin();
            Integer groupAdminId = groupAdmin != null
                    ? groupAdmin.getId()
                    : null;
            statement.setObject(8, groupAdminId);

            statement.setLong(9, id);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean removeStudyGroup(long id) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("DELETE FROM STUDY_GROUP WHERE ID = ?")
        ) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public int clearStudyGroups(String username) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("DELETE FROM STUDY_GROUP WHERE CREATOR = ?")
        ) {
            statement.setString(1, username);
            return statement.executeUpdate();
        }
    }

    public boolean removeGroups(Set<Long> groupsToRemove) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("DELETE FROM STUDY_GROUP WHERE ID = ANY (?)")
        ) {
            statement.setArray(1, conn.createArrayOf("bigint", groupsToRemove.toArray()));
            return statement.executeUpdate() > 0;
        }
    }
}
