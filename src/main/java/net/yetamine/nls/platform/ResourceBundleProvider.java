package net.yetamine.nls.platform;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import net.yetamine.nls.ResourceDiscovery;
import net.yetamine.nls.ResourcePackage;
import net.yetamine.nls.ResourceSupplier;

/**
 * The provider of the implementation based on {@link ResourceBundle} support.
 */
public final class ResourceBundleProvider {

    /**
     * Creates an adapter of a {@link ResourceBundle}.
     *
     * @param bundle
     *            the bundle to adapt. It must not be {@code null}.
     *
     * @return the adapter
     */
    public static ResourceSupplier adapt(ResourceBundle bundle) {
        return new ResourceBundleAdapter(bundle);
    }

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

    /**
     * Creates a new instance that searches the specified list of classes, using
     * the first given class's loader for subsequent loading operations, to find
     * all resources and make a fallback bundle of them.
     *
     * <p>
     * Note that the implementation uses {@code assert} to check the duplication
     * of found entries, so that a duplication should be detected in a testing
     * environment, but should not cause fatal problems in production (unless a
     * misformatted message is a fatal problem).
     *
     * @param lookup
     *            the lookup object to use for reading the fields with the
     *            resource definitions. It may be {@code null} if reflective
     *            access is no problem, otherwise it should be the instance that
     *            has the access to the fields to be inspected.
     * @param clazz
     *            the class whose class loader shall be used and whose name
     *            should be used for the resource bundle name. It must not be
     *            {@code null}.
     * @param classes
     *            the additional classes to be inspected. It must not be
     *            {@code null} and may not contain any {@code null} values.
     *
     * @return the new instance
     *
     * @see ResourceDiscovery
     */
    public static ResourcePackage discover(MethodHandles.Lookup lookup, Class<?> clazz, Class<?>... classes) {
        final Map<String, String> map = new HashMap<>(); // The map with the actual resources
        final ResourceDiscovery discovery = new ResourceDiscovery((k, v) -> {
            final String previous = map.put(k, v);
            assert (previous == null) : String.format("Detected duplicated key '%s'.", k);
        }).lookup(lookup);

        if (discovery.test(clazz)) { // The umbrella class may be omitted from inspection
            discovery.add(clazz);
        }

        Stream.of(classes).forEach(discovery); // Inspect all other given classes, these must pass the test
        final ResourceBundle.Control bundleControl = new ResourceBundleFallback(new MapResourceBundle(map));
        final ResourceBundleLoader loader = ResourceBundleLoader.using(clazz.getClassLoader(), bundleControl);
        return factory(loader).bind(clazz.getName());
    }

    private ResourceBundleProvider() {
        throw new AssertionError();
    }
}
