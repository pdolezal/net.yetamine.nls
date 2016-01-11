package net.yetamine.nls;

import java.util.MissingResourceException;

/**
 * Represents a reference to a resource with the given name.
 *
 * @param <T>
 *            the type of the resource
 */
public interface ResourceReference<T> {

    /**
     * Returns the name of the resource.
     *
     * <p>
     * This method uses intentionally the same name as {@link Enum#name()}, so
     * that an enum may inherit this interface and let its constants act right
     * away as resource references. This technique is demonstrated by the code
     * snippet below, using a more concrete descendant of this interface:
     *
     * <pre>
     * enum Titles implements MessageTemplate.Reference {
     *     MR, MS, MRS, MISS;
     * }
     *
     * // Then, assuming 'resources' contain such resources, it is possible just
     * // to get the values and format them immediately:
     * Titles.MR.use(resources).with("Smith");
     *
     * // The results could be, e.g., "Mr. Smith" or "Herr Smith" (depending on
     * // the 'resources' implementation and settings like the actual locale).
     * </pre>
     *
     * @return the name of the resource
     */
    String name();

    /**
     * Retrieves the resource from the given resouce provider.
     *
     * @param resources
     *            the resource provider use. It must not be {@code null}.
     *
     * @return the resource
     *
     * @throws MissingResourceException
     *             if the resource could not be retrieved
     */
    T use(ResourceProvider resources);

    /**
     * Retrieves the resource from the current implicit provider.
     *
     * @return the resource
     *
     * @throws IllegalStateException
     *             if no implicit provider is available at this moment
     * @throws MissingResourceException
     *             if the resource could not be retrieved
     *
     * @see ResourceProvider#context()
     */
    T use();
}
