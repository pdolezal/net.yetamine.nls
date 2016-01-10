package net.yetamine.nls;

import java.util.MissingResourceException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a generic resource provided as a resource with the given name.
 *
 * @param <T>
 *            the type of the resource
 */
public interface ResourceObject<T> extends ResourceReference<T> {

    /**
     * @see net.yetamine.nls.ResourceReference#use(net.yetamine.nls.ResourceSupplier)
     */
    T use(ResourceSupplier resources);

    /**
     * @see net.yetamine.nls.ResourceReference#use()
     */
    default T use() {
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
    default ResourceBinding<T> bind(ResourcePackage source) {
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
    default Supplier<T> using(ResourceSupplier resources) {
        Objects.requireNonNull(resources);
        return () -> use(resources);
    }

    /**
     * Declares a resource object which must be supplied by a bundle.
     *
     * <p>
     * The result shall never resolve to {@code null}, because the provided
     * fallback throws rather a {@link MissingResourceException}.
     *
     * @param <T>
     *            the type of the object
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return a resource object
     */
    static <T> ResourceObject<T> declare(String name) {
        return new DefaultResourceObject<>(name, r -> {
            throw new MissingResourceException(ResourceObject.class.getTypeName(), r.name(), name);
        });
    }

    /**
     * Defines a resource object with a constant as a fallback.
     *
     * <p>
     * The fallback constant must be an immutable instance, or {@code null}
     * (which would then resolve to {@code null} for an actually missing
     * resource).
     *
     * @param <T>
     *            the type of the object
     * @param name
     *            the name of the resource. It must not be {@code null}.
     * @param value
     *            the fallback value
     *
     * @return a resource object
     */
    static <T> ResourceObject<T> constant(String name, T value) {
        return new DefaultResourceObject<>(name, r -> value);
    }

    /**
     * Defines a resource object with a supplier providing the fallback.
     *
     * <p>
     * The fallback supplier may return {@code null} (which would then resolve
     * to {@code null} for an actually missing resource).
     *
     * @param <T>
     *            the type of the object
     * @param name
     *            the name of the resource. It must not be {@code null}.
     * @param supplier
     *            the fallback supplier, which gets the {@link ResourceSupplier}
     *            as the argument, so that it knows the actual failing source.
     *            It must not be {@code null}.
     *
     * @return a resource object
     */
    static <T> ResourceObject<T> supplier(String name, Function<? super ResourceSupplier, ? extends T> supplier) {
        return new DefaultResourceObject<>(name, supplier);
    }
}

/**
 * The default implementation of the {@link ResourceObject} interface.
 *
 * @param <T>
 *            the type of the resource
 */
final class DefaultResourceObject<T> implements ResourceObject<T> {

    /** Name of the resource. */
    private final String name;
    /** Type of the resource. */
    private final Function<? super ResourceSupplier, ? extends T> fallback;

    /**
     * Creates a new instance.
     *
     * @param resourceName
     *            the name of the resource. It must not be {@code null}.
     * @param fallbackSupplier
     *            the fallback supplier. It must not be {@code null}.
     */
    public DefaultResourceObject(String resourceName, Function<? super ResourceSupplier, ? extends T> fallbackSupplier) {
        name = Objects.requireNonNull(resourceName);
        fallback = Objects.requireNonNull(fallbackSupplier);
    }

    /**
     * @see net.yetamine.nls.ResourceReference#name()
     */
    public String name() {
        return name;
    }

    /**
     * @see net.yetamine.nls.ResourceObject#use(net.yetamine.nls.ResourceSupplier)
     */
    public T use(ResourceSupplier resources) {
        final T result = resources.object(name());
        return (result != null) ? result : fallback.apply(resources);
    }
}
