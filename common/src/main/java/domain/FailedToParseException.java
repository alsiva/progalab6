package domain;

/**
 * is thrown when failed to parse csv file
 */
public class FailedToParseException extends Exception {
    // todo: move to client
    public FailedToParseException(String message) {
        super(message);
    }
}
