package net.yetamine.nls;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.function.Function;

/**
 * A resource with bound operands, so that it is ready for rendering, although
 * retaining the possibility to switch the resource supplier before doing so.
 */
public final class ResourceBinding implements Function<Locale, String> {

    /** Resource resolving function. */
    private final Function<ResourceSupplier, String> resolver;
    /** Source of the resource to be applied. */
    private final ResourcePackage source;

    /**
     * Creates a new instance.
     *
     * @param resourceSource
     *            the source of the resource. It must not be {@code null}.
     * @param resourceResolver
     *            the resolver of the resource with the respect to the source.
     *            It must not be {@code null}.
     */
    public ResourceBinding(ResourcePackage resourceSource, Function<ResourceSupplier, String> resourceResolver) {
        source = Objects.requireNonNull(resourceSource);
        resolver = Objects.requireNonNull(resourceResolver);
    }

    /**
     * The implementation uses {@link #apply()} to return the string and may
     * return {@code null} if it fails with {@link MissingResourceException}.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        try {
            return apply();
        } catch (MissingResourceException e) {
            return null;
        }
    }

    /**
     * Returns the resource value with the respect to the given locale.
     *
     * @see java.util.function.Function#apply(java.lang.Object)
     */
    public String apply(Locale locale) {
        return resolver.apply(source.locale(locale));
    }

    /**
     * Returns the resource value from the currently available source.
     *
     * @return the resource value
     */
    public String apply() {
        return resolver.apply(source());
    }

    /**
     * Provides the source of the resource.
     *
     * @return the source of the resource
     */
    public ResourcePackage source() {
        return source;
    }

    /**
     * Provides a new instance using a different locale.
     *
     * @param locale
     *            the locale to switch to. It must not be {@code null}.
     *
     * @return a new instance using a different locale.
     */
    public ResourceBinding locale(Locale locale) {
        return new ResourceBinding(source.locale(locale), resolver);
    }
}
