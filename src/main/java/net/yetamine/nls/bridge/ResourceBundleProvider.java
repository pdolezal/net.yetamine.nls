package net.yetamine.nls.bridge;

import java.util.Locale;
import java.util.ResourceBundle;

import net.yetamine.nls.ResourcePackage;

/**
 * The provider of the implementation based on {@link ResourceBundle} support.
 */
public final class ResourceBundleProvider {

    /**
     * Creates a factory using the specified {@link ResourceBundleLoader} to
     * resolve resource bundles.
     *
     * @param loader
     *            the loader to use. It must not be {@code null}.
     *
     * @return the factory
     */
    public static ResourcePackage.Factory factory(ResourceBundleLoader loader) {
        return (name, locale) -> new ResourceBundleResolver(loader, name, locale);
    }

    /**
     * Creates a factory using the specified {@link ClassLoader} to load the
     * resource bundles.
     *
     * @param loader
     *            the class loader to use. It must not be {@code null}.
     *
     * @return the factory
     */
    public static ResourcePackage.Factory factory(ClassLoader loader) {
        return factory(ResourceBundleLoader.using(loader));
    }

    /**
     * Creates a factory using the {@link ClassLoader} of the given
     * {@link Class} to load the resource bundles.
     *
     * @param clazz
     *            the class whose class loader shall be used. It must not be
     *            {@code null}.
     *
     * @return the factory
     */
    public static ResourcePackage.Factory factory(Class<?> clazz) {
        return factory(clazz.getClassLoader());
    }

    /**
     * Returns an unresolved {@link ResourcePackage} instance bound to the name
     * of the given class (hence using resouces with the same name), using its
     * {@link ClassLoader} and using {@link Locale#getDefault()} as the locale
     * supplier.
     *
     * @param clazz
     *            the class to use. It must not be {@code null}.
     *
     * @return an unresolved instance bound to the specified parameters
     */
    public static ResourcePackage bundle(Class<?> clazz) {
        return factory(clazz.getClassLoader()).bind(clazz.getName());
    }

    private ResourceBundleProvider() {
        throw new AssertionError();
    }
}
