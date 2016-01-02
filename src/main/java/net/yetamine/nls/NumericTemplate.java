package net.yetamine.nls;

import java.util.function.Supplier;

/**
 * A template for formatting a numeric quantity to a string.
 */
public interface NumericTemplate {

    /**
     * Returns the formatted text.
     *
     * @param value
     *            the value to format
     *
     * @return the formatted text
     */
    String apply(long value);

    /**
     * Returns the formatted text.
     *
     * @param value
     *            the value to format
     *
     * @return the formatted text
     */
    String apply(double value);

    /**
     * Returns a {@link Supplier} that executes {@link #apply(long)} on demand.
     *
     * @param value
     *            the value to bind
     *
     * @return the supplier
     */
    default Supplier<String> bind(long value) {
        return () -> apply(value);
    }

    /**
     * Returns a {@link Supplier} that executes {@link #apply(double)} on demand.
     *
     * @param value
     *            the value to bind
     *
     * @return the supplier
     */
    default Supplier<String> bind(double value) {
        return () -> apply(value);
    }
}
