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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import net.yetamine.nls.MessageTemplate;
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
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("ResourceBundleBridge[%s]", name());
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
    public ResourcePackage locale(Supplier<Locale> value) {
        return new ResourceBundleResolver(loader, name(), value);
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#locale(java.util.Locale)
     */
    public ResourcePackage locale(Locale value) {
        return value.equals(locale()) ? this : new ResourceBundleResolver(loader, name(), () -> value);
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#locale()
     */
    public Locale locale() {
        return bundle.getLocale();
    }

    /**
     * @see net.yetamine.nls.ResourceProvider#provides(java.lang.String)
     */
    public boolean provides(String name) {
        return bundle.containsKey(name);
    }

    /**
     * @see net.yetamine.nls.ResourceProvider#object(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public <T> T object(String name) {
        return (T) bundle.getObject(name);
    }

    /**
     * @see net.yetamine.nls.ResourceProvider#message(java.lang.String)
     */
    public MessageTemplate message(String name) {
        return args -> new MessageFormat(string(name), locale()).format(args);
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#string(java.lang.String)
     */
    public String string(String name) {
        return bundle.getString(name);
    }
}
