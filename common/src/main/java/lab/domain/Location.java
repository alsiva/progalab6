package lab.domain;

import java.io.Serializable;

/**
 * class that defines location
 */
public class Location implements Serializable {
    public Location(Integer id, int x, int y, String locationName) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.locationName = locationName;
    }

    private Integer id; // Может быть null, если создано с клиента
    private final int x; // not null
    private final int y; // Поле не может быть null
    private final String locationName; // Поле не может быть null

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    /**
     * returns coordinate x
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * returns location y
     * @return y
     */
    public Integer getY() {
        return y;
    }

    /**
     * returns location name
     * @return location name
     */
    public String getLocationName() {
        return locationName;
    }

    public static Integer readX(String fieldAsString) throws FailedToParseException {
        if (fieldAsString.isEmpty()) {
            return null;
        }

        int value;
        try {
            value = Integer.parseInt(fieldAsString);
        } catch (IllegalArgumentException e) {
            throw new FailedToParseException("Failed to read location x: " + e.getMessage());
        }
        return value;
    }

    public static int readY(String fieldAsString) throws FailedToParseException {
        int value;
        try {
            value = Integer.parseInt(fieldAsString);
        } catch (IllegalArgumentException e) {
            throw new FailedToParseException("Failed to read location y: " + e.getMessage());
        }
        return value;
    }
}