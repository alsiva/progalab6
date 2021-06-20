package lab;

import lab.domain.FailedToParseException;

/**
 * parser interface
 * @param <T>
 */
@FunctionalInterface
public interface Parser<T> {
    T parse(String str) throws FailedToParseException;
}
