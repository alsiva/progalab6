package lab.domain;

/**
 * is thrown when failed to parse field from string
 */
public class FailedToParseException extends Exception {
    public FailedToParseException(String message) {
        super(message);
    }
}
