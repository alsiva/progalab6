package lab.domain;

import java.io.Serializable;

/**
 * class that contains x,y coordinates
 */
public class Coordinates implements Serializable {
    public Coordinates(float x, int y) {
        this.x = x;
        this.y = y;
    }

    private final float x; //Значение поля должно быть больше -318
    private final int y; //Максимальное значение поля: 870

    /**
     * @return x coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    public static float readX(String fieldAsString) throws FailedToParseException {
        float value;
        try {
            value = Float.parseFloat(fieldAsString);
        } catch (NumberFormatException e) {
            throw new FailedToParseException("Failed to read x: " + e.getMessage());
        }

        if (value <= -318) {
            throw new FailedToParseException("x should be greater than 318");
        }

        return value;
    }

    public static int readY(String fieldAsString) throws FailedToParseException {
        int value;
        try {
            value = Integer.parseInt(fieldAsString);
        } catch (NumberFormatException e) {
            throw new FailedToParseException("Failed to read y: " + e.getMessage());
        }

        if (value > 870) {
            throw new FailedToParseException("y should be less than 870");
        }

        return value;
    }
}