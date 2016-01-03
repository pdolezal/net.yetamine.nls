package net.yetamine.nls.bridge;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import net.yetamine.nls.ResourcePackage;

/**
 * A bridge for a {@link ResourceBundle} instance.
 */
final class ResourceBundleBridge implements ResourcePackage {

    /** Adapted resource bundle. */
    private final ResourceBundle bundle;
    /** Loader of the resource bundles. */
    private final ResourceBundleLoader loader;

    /**
     * Creates a new instance.
     *
     * @param bundleLoader
     *            the loader of the bundle. It must not be {@code null}.
     * @param resourceBundle
     *            the actual resource bundle. It must not be {@code null}.
     */
    public ResourceBundleBridge(ResourceBundleLoader bundleLoader, ResourceBundle resourceBundle) {
        bundle = Objects.requireNonNull(resourceBundle);
        loader = Objects.requireNonNull(bundleLoader);
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#name()
     */
    public String name() {
        return bundle.getBaseBundleName();
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#resolve()
     */
    public ResourcePackage resolve() {
        return this;
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#locale(java.util.function.Supplier)
     */
    public ResourcePackage locale(Supplier<Locale> locale) {
        return new ResourceBundleResolver(loader, name(), locale);
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#locale(java.util.Locale)
     */
    public ResourcePackage locale(Locale locale) {
        final Locale current = locale();
        return locale.equals(current) ? this : new ResourceBundleResolver(loader, name(), () -> current);
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#locale()
     */
    public Locale locale() {
        return bundle.getLocale();
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#string(java.lang.String)
     */
    public String string(String name) {
        return bundle.getString(name);
    }
}
