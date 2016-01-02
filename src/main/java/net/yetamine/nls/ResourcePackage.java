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
 * {@link #resolve()} that returns a fully resolved instance; note it is the
 * only method that does return a resolved instance, others return always an
 * unresolved (lazy) instances.
 */
public interface ResourcePackage {

    /**
     * Returns the name of this resource package.
     *
     * @return the name of this resource package
     */
    String name();

    /**
     * Returns a resource package with the same name for the specified locale.
     *
     * @param locale
     *            the locale supplier to use. It must not be {@code null} and it
     *            must return a valid locale.
     *
     * @return a resource package for the specified locale
     */
    ResourcePackage locale(Supplier<Locale> locale);

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
     * Returns a resource package with the same name for the specified locale.
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
     * Retrieves a resource of the given name and returns it as a {@link String}
     * which is usually best for string constants or when the raw string content
     * of a template is needed.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the string content of the resource
     */
    String string(String name);

    /**
     * Constructs a template from a resource with the given name and returns the
     * template.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the template
     */
    DecimalTemplate decimal(String name);

    /**
     * Constructs a template from a resource with the given name and returns the
     * template.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the template
     */
    IntegralTemplate integral(String name);

    /**
     * Constructs a template from a resource with the given name and returns the
     * template.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the template
     */
    MessageTemplate message(String name);
}
