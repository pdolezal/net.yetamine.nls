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
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("MapResourceBundle[%s]", getBaseBundleName());
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
