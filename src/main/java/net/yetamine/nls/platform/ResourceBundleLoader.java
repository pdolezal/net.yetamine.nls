package net.yetamine.nls.platform;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * A loader for loading {@link ResourceBundle} instances on demand.
 *
 * <p>
 * This interface exists for two major reasons:
 *
 * <ol>
 * <li>It allows hooking and customizing the loading process.</li>
 * <li>It allows to deal with class loaders and permissions in the way that
 * works always. A class may supply its class loader with no explicit permission
 * which would be otherwise required for a class in such a common library.</li>
 * </ol>
 */
@FunctionalInterface
public interface ResourceBundleLoader {

    /**
     * Returns a {@link ResourceBundle} instance matching to the given name and
     * locale.
     *
     * @param name
     *            the name of the resource bundle. It must not be {@code null}.
     * @param locale
     *            the desired locale. It must not be {@code null}.
     *
     * @return a suitable instance of {@link ResourceBundle}
     *
     * @throws MissingResourceException
     *             if the bundle could not be loaded
     */
    ResourceBundle load(String name, Locale locale);

    /**
     * Creates a new instance that uses the specified class loader for loading
     * the bundles.
     *
     * @param loader
     *            the class loader to use. It must not be {@code null}.
     * @param control
     *            the loading control. It must not be {@code null}.
     *
     * @return the new instance
     */
    static ResourceBundleLoader using(ClassLoader loader, ResourceBundle.Control control) {
        Objects.requireNonNull(control);
        Objects.requireNonNull(loader);

        return (name, locale) -> ResourceBundle.getBundle(name, locale, loader, control);
    }

    /**
     * Creates a new instance that uses the specified class loader for loading
     * the bundles and the control providing no fallback and recognizing only
     * properties.
     *
     * @param loader
     *            the class loader to use. It must not be {@code null}.
     *
     * @return the new instance
     */
    static ResourceBundleLoader using(ClassLoader loader) {
        return using(loader, ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }
}
