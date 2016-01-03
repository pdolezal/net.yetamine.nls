package net.yetamine.nls;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.function.Supplier;

/**
 * A package of templates and strings in the given locale.
 *
 * <p>
 * The interface design anticipates lazy resolution implementations that do not
 * resolve the resource until it is actually needed and must be provided. While
 * this approach is useful for occasional uses (for instance, when an exception
 * shall be thrown and the error message is resolved with a callback), it is not
 * very efficient for a sequence of resolutions. This scenario is supported with
 * {@link #resolve()} that returns a fully resolved instance.
 */
public interface ResourcePackage extends ResourceSupplier {

    /**
     * Returns the name of this resource package.
     *
     * @return the name of this resource package
     */
    String name();

    /**
     * Returns a fully resolved resource package.
     *
     * <p>
     * The returned instance is immutable as far as possible and invoking this
     * method on it returns the same instance. It is recommended to resolve an
     * unresolved package if it is used multiple times in the current context,
     * e.g.:
     *
     * <pre>
     * // Assuming 'resources' contain an unresolved instance:
     * final ResourcePackage r = resources.resolve();
     * final String message = Greetings.HELLO.from(r).with(Titles.MR.from(r).with("Smith"));
     * // Without using this method, the output would be usually the same, but
     * // the resolution process could be repeated twice unncessarily.
     * </pre>
     *
     * @return a fully resolved resource package representation, possibly this
     *         instance if already resolved
     *
     * @throws MissingResourceException
     *             if the package could not be resolved
     */
    ResourcePackage resolve();

    /**
     * Returns an unresolved resource package with the same name, but using the
     * specified locale supplier.
     *
     * @param locale
     *            the locale supplier to use. It must not be {@code null} and it
     *            must return a valid locale.
     *
     * @return a resource package for the specified locale
     */
    ResourcePackage locale(Supplier<Locale> locale);

    /**
     * Returns a resource package with the same name for the specified locale;
     * if this instance is resolved and the locale is the same, the result is
     * this instance, otherwise an unresolved instance is returned.
     *
     * @param locale
     *            the locale to use. It must not be {@code null}.
     *
     * @return a resource package for the specified locale
     */
    ResourcePackage locale(Locale locale);

    /**
     * Returns the actual locale.
     *
     * <p>
     * The result of this method depends on the locale supplier which this
     * resource package uses and does not have to be constant necessarily.
     * Resolved instances, however, must return a constant result.
     *
     * @return the actual locale
     */
    Locale locale();

    /**
     * A factory interface for {@link ResourcePackage}.
     */
    @FunctionalInterface
    interface Factory {

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
}
