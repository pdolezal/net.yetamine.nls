package net.yetamine.nls;

import java.util.Objects;

/**
 * Represents a constant string provided as a resource with the given name.
 */
@FunctionalInterface
public interface ConstantString extends ResourceObject<String> {

    /**
     * @see net.yetamine.nls.ResourceReference#use(net.yetamine.nls.ResourceProvider)
     */
    default String use(ResourceProvider resources) {
        return resources.string(name());
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
