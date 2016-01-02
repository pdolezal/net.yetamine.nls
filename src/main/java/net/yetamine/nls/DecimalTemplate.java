package net.yetamine.nls;

import java.util.Objects;
import java.util.function.DoubleFunction;

/**
 * A formatter for numeric quantities of a decimal nature.
 *
 * <p>
 * This formatter is useful for converting a decimal number into a string, but
 * the result does not have to be the pure string representation of the value:
 * the resulting string can, e.g., contain the unit of the value in the actual
 * locale and with the respect to the actual language.
 */
@FunctionalInterface
public interface DecimalTemplate extends DoubleFunction<String> {

    /**
     * Represents a reference to a resource with the given name that shall be
     * resolved to a {@link DecimalTemplate}.
     */
    @FunctionalInterface
    interface Reference extends ResourceReference<DecimalTemplate> {

        /**
         * @see net.yetamine.nls.ResourceReference#from(net.yetamine.nls.ResourcePackage)
         */
        default DecimalTemplate from(ResourcePackage resources) {
            return resources.decimal(name());
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
    String with(double value);

    /**
     * The default implementation is an alias to {@link #with(double)}.
     *
     * @see java.util.function.DoubleFunction#apply(double)
     */
    default String apply(double value) {
        return with(value);
    }
}
