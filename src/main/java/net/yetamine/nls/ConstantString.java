package net.yetamine.nls;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a constant string provided as a resource with the given name.
 */
@FunctionalInterface
public interface ConstantString extends ResourceReference<String> {

    /**
     * @see net.yetamine.nls.ResourceReference#from(net.yetamine.nls.ResourceSupplier)
     */
    default String from(ResourceSupplier resources) {
        return resources.string(name());
    }

    /**
     * Returns a {@link Supplier} which supplies the result on demand rather
     * than immediate loading of the value.
     *
     * @param resources
     *            the resource package to use. It must not be {@code null}.
     *
     * @return a {@link Supplier} which supplies the result on demand
     */
    default Supplier<String> bind(ResourceSupplier resources) {
        Objects.requireNonNull(resources);
        return () -> from(resources);
    }

    /**
     * Creates a new instance with the given name.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the new instance
     */
    static ConstantString name(String name) {
        Objects.requireNonNull(name);
        return () -> name;
    }
}
