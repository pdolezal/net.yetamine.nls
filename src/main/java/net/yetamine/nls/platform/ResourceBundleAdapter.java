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
import java.util.Objects;
import java.util.ResourceBundle;

import net.yetamine.nls.MessageTemplate;
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
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("ResourceBundleAdapter[%s]", name());
    }

    /**
     * @see net.yetamine.nls.ResourceProvider#name()
     */
    public String name() {
        return bundle.getBaseBundleName();
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
        return args -> new MessageFormat(string(name), bundle.getLocale()).format(args);
    }

    /**
     * @see net.yetamine.nls.ResourcePackage#string(java.lang.String)
     */
    public String string(String name) {
        return bundle.getString(name);
    }
}
