package net.yetamine.nls.platform;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import net.yetamine.nls.ResourcePackage;

/**
 * A proxy for a {@link ResourceBundle} instance that loads the instance on
 * demand.
 */
final class ResourceBundleResolver implements ResourcePackage {

    /** Loader of the resource bundles. */
    private final ResourceBundleLoader loader;
    /** Supplier of the locale for the loading time. */
    private final Supplier<Locale> locale;
    /** Name of the bundle to load. */
    private final String name;

    /**
     * Creates a new instance.
     *
     * @param bundleLoader
     *            the loader of the bundle. It must not be {@code null}.
     * @param bundleName
     *            the name of the bundle. It must not be {@code null}.
     * @param localeSupplier
     *            the supplier of the locale. It must not be {@code null}.
     */
    public ResourceBundleResolver(ResourceBundleLoader bundleLoader, String bundleName, Supplier<Locale> localeSupplier) {
        loader = Objects.requireNonNull(bundleLoader);
        locale = Objects.requireNonNull(localeSupplier);
        name = Objects.requireNonNull(bundleName);
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#name()
     */
    public String name() {
        return name;
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#resolve()
     */
    public ResourcePackage resolve() {
        return new ResourceBundleBridge(loader, loader.load(name(), locale()));
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#locale(java.util.function.Supplier)
     */
    public ResourcePackage locale(Supplier<Locale> value) {
        return new ResourceBundleResolver(loader, name, value);
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#locale(java.util.Locale)
     */
    public ResourcePackage locale(Locale value) {
        return locale(() -> value);
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#locale()
     */
    public Locale locale() {
        return locale.get();
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#string(java.lang.String)
     */
    public String string(String name) {
        return loader.load(name(), locale()).getString(name);
    }
}
