/*
 * Copyright 2016 Yetamine
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.yetamine.nls.platform;

import java.util.Locale;
import java.util.MissingResourceException;
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
     * @see net.yetamine.nls.ResourceProvider#provides(java.lang.String)
     */
    public boolean provides(String identifier) {
        return loader.load(name(), locale()).containsKey(identifier);
    }

    /**
     * @see net.yetamine.nls.ResourceProvider#object(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public <T> T object(String identifier) {
        try {
            return (T) loader.load(name(), locale()).getObject(identifier);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#string(java.lang.String)
     */
    public String string(String identifier) {
        return loader.load(name(), locale()).getString(identifier);
    }
}
