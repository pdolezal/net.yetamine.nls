package net.yetamine.nls;

import java.util.function.Supplier;

/**
 * A representation of a formattable message template.
 */
public interface MessageTemplate {

    /**
     * Applies the given arguments on this template.
     *
     * @param args
     *            the arguments to use for formatting
     *
     * @return the result of formatting this template
     */
    String apply(Object... args);

    /**
     * Returns a {@link Supplier} that executes {@link #apply(Object[])} on
     * demand.
     *
     * @param args
     *            the arguments to bind
     *
     * @return the supplier
     */
    default Supplier<String> bind(Object... args) {
        return () -> apply(args);
    }
}
