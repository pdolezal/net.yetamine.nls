package net.yetamine.nls;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * A factory interface for retrieving {@link ResourcePackage} instances.
 */
public interface ResourceFactory {

    /**
     * Returns an unresolved {@link ResourcePackage} instance bound to the
     * given name and locale supplier.
     *
     * @param name
     *            the name of the package. It must not be {@code null}.
     * @param locale
     *            the locale supplier. It must not be {@code null} and it
     *            must not return {@code null}.
     *
     * @return an unresolved instance bound to the specified parameters
     */
    ResourcePackage bind(String name, Supplier<Locale> locale);

    /**
     * Returns an unresolved {@link ResourcePackage} instance bound to the
     * given name and locale.
     *
     * @param name
     *            the name of the package. It must not be {@code null}.
     * @param locale
     *            the locale. It must not be {@code null}.
     *
     * @return an unresolved instance bound to the specified parameters
     */
    default ResourcePackage bind(String name, Locale locale) {
        return bind(name, () -> locale);
    }

    /**
     * Returns an unresolved {@link ResourcePackage} instance bound to the
     * given name and using {@link Locale#getDefault()} as the locale
     * supplier.
     *
     * @param name
     *            the name of the package. It must not be {@code null}.
     *
     * @return an unresolved instance bound to the specified parameters
     */
    default ResourcePackage bind(String name) {
        return bind(name, Locale::getDefault);
    }

    /**
     * Returns a resolved {@link ResourcePackage} instance bound to the
     * given name and locale.
     *
     * @param name
     *            the name of the package. It must not be {@code null}.
     * @param locale
     *            the locale. It must not be {@code null}.
     *
     * @return a resolved instance bound to the specified parameters
     */
    default ResourcePackage resolve(String name, Locale locale) {
        return bind(name, locale).resolve();
    }
}