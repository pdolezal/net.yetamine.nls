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
import java.util.function.Supplier;

/**
 * A package of templates and strings in the given locale.
 *
 * <p>
 * The interface design anticipates lazy resolution implementations that do not
 * resolve the resource until the resource is actually required and therefore it
 * must be resolved. While this approach is useful for occasional uses (e.g.,
 * when an exception shall be thrown and the error message is resolved with a
 * callback), it is not very efficient for a sequence of uses. For such cases,
 * the {@link #resolve()} method, which returns a fully resolved instance, can
 * be used to get a fully resolved instance.
 */
public interface ResourcePackage extends ResourceProvider {

    /**
     * Returns a fully resolved resource package.
     *
     * <p>
     * The returned instance is immutable as far as possible and invoking this
     * method on it returns the same instance. It is recommended to resolve an
     * unresolved package if it is used multiple times in the current context,
     * e.g.:
     *
     * <pre>
     * // Assuming 'resources' contain an unresolved instance:
     * final ResourcePackage r = resources.resolve();
     * final String message = Greetings.HELLO.use(r).with(Titles.MR.use(r).with("Smith"));
     * // Without using this method, the output would be usually the same, but
     * // the resolution process could be repeated twice unnecessarily.
     * </pre>
     *
     * @return a fully resolved resource package representation, possibly this
     *         instance if already resolved
     *
     * @throws MissingResourceException
     *             if the package could not be resolved
     */
    ResourcePackage resolve();

    /**
     * Returns an unresolved resource package with the same name, but using the
     * specified locale supplier.
     *
     * @param value
     *            the locale supplier to use. It must not be {@code null} and it
     *            must return a valid locale.
     *
     * @return a resource package for the specified locale
     */
    ResourcePackage locale(Supplier<Locale> value);

    /**
     * Returns a resource package with the same name for the specified locale;
     * if this instance is resolved and the locale is the same, the result is
     * this instance, otherwise an unresolved instance is returned.
     *
     * @param value
     *            the locale to use. It must not be {@code null}.
     *
     * @return a resource package for the specified locale
     */
    ResourcePackage locale(Locale value);

    /**
     * Returns the actual locale.
     *
     * <p>
     * The result of this method depends on the locale supplier which this
     * resource package uses and does not have to be constant necessarily.
     * Resolved instances, however, must return a constant result.
     *
     * @return the actual locale
     */
    Locale locale();

    /**
     * The default implementation uses a resolved form of this instance to make
     * the context.
     *
     * @see net.yetamine.nls.ResourceProvider#context()
     */
    default ResourceContext context() {
        return ResourceContext.open(resolve());
    }

    /**
     * A factory interface for {@link ResourcePackage}
     */
    @FunctionalInterface
    interface Factory {

        /**
         * Returns an unresolved {@link ResourcePackage} instance bound to the
         * given name and locale supplier.
         *
         * @param name
         *            the name of the package. It must not be {@code null}.
         * @param locale
         *            the locale supplier. It must not be {@code null} and it
         *            must not return {@code null}.
         *
         * @return an unresolved instance bound to the specified parameters
         */
        ResourcePackage bind(String name, Supplier<Locale> locale);

        /**
         * Returns an unresolved {@link ResourcePackage} instance bound to the
         * given name and locale.
         *
         * @param name
         *            the name of the package. It must not be {@code null}.
         * @param locale
         *            the locale. It must not be {@code null}.
         *
         * @return an unresolved instance bound to the specified parameters
         */
        default ResourcePackage bind(String name, Locale locale) {
            return bind(name, () -> locale);
        }

        /**
         * Returns an unresolved {@link ResourcePackage} instance bound to the
         * given name and using {@link Locale#getDefault()} as the locale
         * supplier.
         *
         * @param name
         *            the name of the package. It must not be {@code null}.
         *
         * @return an unresolved instance bound to the specified parameters
         */
        default ResourcePackage bind(String name) {
            return bind(name, Locale::getDefault);
        }

        /**
         * Returns a resolved {@link ResourcePackage} instance bound to the
         * given name and locale.
         *
         * @param name
         *            the name of the package. It must not be {@code null}.
         * @param locale
         *            the locale. It must not be {@code null}.
         *
         * @return a resolved instance bound to the specified parameters
         */
        default ResourcePackage resolve(String name, Locale locale) {
            return bind(name, locale).resolve();
        }
    }
}
