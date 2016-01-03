package net.yetamine.nls.platform;

import java.util.Objects;
import java.util.ResourceBundle;

import net.yetamine.nls.ResourceSupplier;

/**
 * An adapter of a {@link ResourceBundle} instance.
 */
final class ResourceBundleAdapter implements ResourceSupplier {

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
     * @see net.yetamine.nls.ResourcePackage#string(java.lang.String)
     */
    public String string(String name) {
        return bundle.getString(name);
    }
}
