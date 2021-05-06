package domain;


/**
 * parser interface
 * @param <T>
 */
@FunctionalInterface
public interface Parser<T> {
    // todo: move to client
    T parse(String str) throws FailedToParseException;
}
