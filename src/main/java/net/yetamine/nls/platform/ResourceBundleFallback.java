package net.yetamine.nls.platform;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Supplier;

/**
 * A {@link java.util.ResourceBundle.Control} instance providing a fallback
 * using a {@link Map}-based {@link ResourceBundle} instance.
 */
public final class ResourceBundleFallback extends ResourceBundle.Control {

    /** Fallback bundle instance. */
    private final Supplier<? extends ResourceBundle> bundle;

    /**
     * Creates a new instance.
     *
     * @param fallbackSupplier
     *            the supplier of the fallback bundle to use. It must not be
     *            {@code null} and must not return {@code null} either.
     */
    public ResourceBundleFallback(Supplier<? extends ResourceBundle> fallbackSupplier) {
        bundle = Objects.requireNonNull(fallbackSupplier);
    }

    /**
     * Creates a new instance.
     *
     * @param fallback
     *            the fallback bundle to use. It must not be {@code null}.
     */
    public ResourceBundleFallback(ResourceBundle fallback) {
        Objects.requireNonNull(fallback);
        bundle = () -> fallback;
    }

    /**
     * @see java.util.ResourceBundle.Control#newBundle(java.lang.String,
     *      java.util.Locale, java.lang.String, java.lang.ClassLoader, boolean)
     */
    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
        return (locale == Locale.ROOT) ? bundle.get() : super.newBundle(baseName, locale, format, loader, reload);
    }

    /**
     * @see java.util.ResourceBundle.Control#getFormats(java.lang.String)
     */
    @Override
    public List<String> getFormats(String baseName) {
        return ResourceBundle.Control.FORMAT_PROPERTIES;
    }

    /**
     * @see java.util.ResourceBundle.Control#getFallbackLocale(java.lang.String,
     *      java.util.Locale)
     */
    @Override
    public Locale getFallbackLocale(String baseName, Locale locale) {
        Objects.requireNonNull(baseName);
        return locale.equals(Locale.ROOT) ? null : Locale.ROOT;
    }
}
