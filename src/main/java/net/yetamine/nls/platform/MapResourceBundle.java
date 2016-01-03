package net.yetamine.nls.platform;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * A {@link ResourceBundle} implementation using a {@link Map} to supply the
 * properties.
 */
public final class MapResourceBundle extends ResourceBundle {

    /** Resources in the bundle. */
    private final Map<String, ?> resources;

    /**
     * Creates a new instance.
     *
     * <p>
     * Note that the provided {@link Map} instance is adapted and used directly,
     * no copy is performed. This enables the caller to modify the bundle if the
     * map may be modified concurrently. Otherwise the caller is responsible for
     * abandoning the reference and not to modify it after passing it to this
     * constructor.
     *
     * @param content
     *            the content of the bundle. It must not be {@code null}.
     */
    public MapResourceBundle(Map<String, ?> content) {
        resources = Objects.requireNonNull(content);
    }

    /**
     * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
     */
    @Override
    protected Object handleGetObject(String key) {
        return resources.get(Objects.requireNonNull(key));
    }

    /**
     * @see java.util.ResourceBundle#getKeys()
     */
    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(resources.keySet());
    }
}
