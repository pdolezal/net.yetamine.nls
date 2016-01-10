package net.yetamine.nls;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a constant string provided as a resource with the given name.
 */
@FunctionalInterface
public interface ConstantString extends ResourceReference<String> {

    /**
     * @see net.yetamine.nls.ResourceReference#use(net.yetamine.nls.ResourceSupplier)
     */
    default String use(ResourceSupplier resources) {
        return resources.string(name());
    }

    /**
     * @see net.yetamine.nls.ResourceReference#use()
     */
    default String use() {
        return use(ResourceContext.require());
    }

    /**
     * Binds this resource to a source.
     *
     * @param source
     *            the source to bind to. It must not be {@code null}.
     *
     * @return the resource binding
     */
    default ResourceBinding<String> bind(ResourcePackage source) {
        return new ResourceBinding<>(source, s -> use(s));
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
    default Supplier<String> using(ResourceSupplier resources) {
        Objects.requireNonNull(resources);
        return () -> use(resources);
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
