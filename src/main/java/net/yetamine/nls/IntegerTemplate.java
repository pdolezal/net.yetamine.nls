package net.yetamine.nls;

import java.util.Objects;
import java.util.function.LongFunction;

/**
 * A formatter for numeric quantities of an integer nature.
 *
 * <p>
 * This formatter is useful for converting an integer number into a string, but
 * the result does not have to be the pure string representation of the value:
 * the resulting string can, e.g., contain the unit of the value in the actual
 * locale and with the respect to the actual language. This example shows the
 * idea:
 *
 * <pre>
 * template.with(1); // Results in "1 byte"
 * template.with(2); // Results in "2 bytes"
 * </pre>
 */
@FunctionalInterface
public interface IntegerTemplate extends LongFunction<String> {

    /**
     * Represents a reference to a resource with the given name that shall be
     * resolved to a {@link IntegerTemplate}.
     */
    @FunctionalInterface
    interface Reference extends ResourceReference<IntegerTemplate> {

        /**
         * @see net.yetamine.nls.ResourceReference#from(net.yetamine.nls.ResourceSupplier)
         */
        default IntegerTemplate from(ResourceSupplier resources) {
            return resources.integer(name());
        }

        /**
         * Creates a new instance with the given name.
         *
         * @param name
         *            the name of the resource. It must not be {@code null}.
         *
         * @return the new instance
         */
        static Reference to(String name) {
            Objects.requireNonNull(name);
            return () -> name;
        }
    }

    /**
     * Returns the value formatted as the text.
     *
     * @param value
     *            the value to format
     *
     * @return the formatted text
     */
    String with(long value);

    /**
     * The default implementation is an alias to {@link #with(long)}.
     *
     * @see java.util.function.LongFunction#apply(long)
     */
    default String apply(long value) {
        return with(value);
    }
}
