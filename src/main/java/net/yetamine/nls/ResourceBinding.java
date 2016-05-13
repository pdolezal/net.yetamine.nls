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

package net.yetamine.nls;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.function.Function;

/**
 * A resource with bound operands, so that it is ready for rendering, although
 * retaining the possibility to switch the resource provider before doing so.
 *
 * @param <T>
 *            the type of the result
 */
public final class ResourceBinding<T> implements Function<Locale, T> {

    /** Resource resolving function. */
    private final Function<ResourceProvider, T> resolver;
    /** Source of the resource to be applied. */
    private final ResourcePackage source;

    /**
     * Creates a new instance.
     *
     * @param resourceSource
     *            the source of the resource. It must not be {@code null}.
     * @param resourceResolver
     *            the resolver of the resource with the respect to the source.
     *            It must not be {@code null}.
     */
    public ResourceBinding(ResourcePackage resourceSource, Function<ResourceProvider, T> resourceResolver) {
        resolver = Objects.requireNonNull(resourceResolver);
        source = Objects.requireNonNull(resourceSource);
    }

    /**
     * The implementation uses {@link #apply()} to return the string. If either
     * {@code null} returns, or {@link MissingResourceException} raises, an
     * empty string is returned rather than {@code null}, which this method
     * should avoid.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        try {
            return Objects.toString(apply(), "");
        } catch (MissingResourceException e) {
            return "";
        }
    }

    /**
     * Returns the resource value with the respect to the given locale.
     *
     * @see java.util.function.Function#apply(java.lang.Object)
     */
    public T apply(Locale locale) {
        return resolver.apply(source.locale(locale));
    }

    /**
     * Returns the resource value from the currently available source.
     *
     * @return the resource value
     */
    public T apply() {
        return resolver.apply(source());
    }

    /**
     * Provides the source of the resource.
     *
     * @return the source of the resource
     */
    public ResourcePackage source() {
        return source;
    }

    /**
     * Provides a new instance using a different locale.
     *
     * @param locale
     *            the locale to switch to. It must not be {@code null}.
     *
     * @return a new instance using a different locale.
     */
    public ResourceBinding<T> locale(Locale locale) {
        return new ResourceBinding<>(source.locale(locale), resolver);
    }
}
