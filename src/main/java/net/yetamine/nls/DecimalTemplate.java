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

import java.util.Objects;
import java.util.function.DoubleFunction;

/**
 * A formatter for numeric quantities of a decimal nature.
 *
 * <p>
 * This formatter is useful for converting a decimal number into a string, but
 * the result does not have to be the pure string representation of the value:
 * the resulting string can, e.g., contain the unit of the value in the actual
 * locale and with the respect to the actual language.
 */
@FunctionalInterface
public interface DecimalTemplate extends DoubleFunction<String> {

    /**
     * Represents a reference to a resource with the given name that shall be
     * resolved to a {@link DecimalTemplate}.
     */
    @FunctionalInterface
    interface Reference extends ResourceObject<DecimalTemplate> {

        /**
         * @see net.yetamine.nls.ResourceReference#use(net.yetamine.nls.ResourceProvider)
         */
        default DecimalTemplate use(ResourceProvider resources) {
            return resources.decimal(name());
        }

        /**
         * Binds this resource to a source.
         *
         * @param source
         *            the source to bind to. It must not be {@code null}.
         * @param value
         *            the value to bind
         *
         * @return the resource binding
         */
        default ResourceBinding<String> bind(ResourcePackage source, double value) {
            return new ResourceBinding<>(source, s -> use(s).with(value));
        }

        /**
         * Creates a new instance with the given name.
         *
         * @param name
         *            the name of the resource. It must not be {@code null}.
         *
         * @return the new instance
         */
        static Reference to(String name) {
            Objects.requireNonNull(name);
            return () -> name;
        }
    }

    /**
     * Returns the value formatted as the text.
     *
     * @param value
     *            the value to format
     *
     * @return the formatted text
     */
    String with(double value);

    /**
     * The default implementation is an alias to {@link #with(double)}.
     *
     * @see java.util.function.DoubleFunction#apply(double)
     */
    default String apply(double value) {
        return with(value);
    }
}
