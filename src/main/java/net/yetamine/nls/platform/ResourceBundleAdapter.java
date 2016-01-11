package net.yetamine.nls.platform;

import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

import net.yetamine.nls.ResourceProvider;

/**
 * An adapter of a {@link ResourceBundle} instance.
 */
final class ResourceBundleAdapter implements ResourceProvider {

    /** Adapted resource bundle. */
    private final ResourceBundle bundle;

    /**
     * Creates a new instance.
     *
     * @param resourceBundle
     *            the actual resource bundle. It must not be {@code null}.
     */
    public ResourceBundleAdapter(ResourceBundle resourceBundle) {
        bundle = Objects.requireNonNull(resourceBundle);
    }

    /**
     * @see net.yetamine.nls.ResourceProvider#name()
     */
    public String name() {
        return bundle.getBaseBundleName();
    }

    /**
     * @see net.yetamine.nls.ResourceProvider#object(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public <T> T object(String name) {
        try {
            return (T) bundle.getObject(name);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#string(java.lang.String)
     */
    public String string(String name) {
        return bundle.getString(name);
    }
}
