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

import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A resource providing interface.
 *
 * <p>
 * This interface is the core interface of the whole library as it encapsulates
 * a source of resources for other parts of the library and for clients as well.
 * The design of this interface assumes that all resources are representable as
 * strings and all related resources are parsed from the strings. Because of
 * that, {@link #string(String)} is supposed to be the common denominator of
 * other resource-providing methods, except for {@link #object(String)} that
 * supports in a way object resources as well.
 *
 * <p>
 * However, this assumption does not prevent an implementation from parsing the
 * string representation of a resource once and reusing it later. Other options,
 * like separated resource types, are possible as well, but those must be then
 * reflected when using the resource discovery feature.
 */
public interface ResourceProvider {

    /**
     * Returns the name of this resource source.
     *
     * @return the name of this resource source, or {@code null} if unknown
     */
    String name();

    /**
     * Retrieves a resource object.
     *
     * <p>
     * Note that the method or underlying implementation may not be able to
     * avoid unchecked casts or other type verifications, hence it is not a
     * completely type-safe option. Therefore {@link ClassCastException} may
     * occur as the result of a type check failure at a later point.
     *
     * @param <T>
     *            the type of the resource object
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the resource object
     *
     * @throws MissingResourceException
     *             if the resource could not be retrieved
     */
    <T> T object(String name);

    /**
     * Retrieves a resource object of the given type.
     *
     * <p>
     * Unlike {@link #object(String)}, this method does perform a type check
     * before returning the result, throwing {@link MissingResourceException}
     * rather than {@link ClassCastException} if the desired type does not match
     * the type of the actual resource.
     *
     * @param <T>
     *            the type of the resource object
     * @param name
     *            the name of the resource. It must not be {@code null}.
     * @param type
     *            the desired type of the resource. It must not be {@code null}.
     *
     * @return the resource object
     *
     * @throws MissingResourceException
     *             if the resource could not be retrieved, or its type is wrong
     */
    default <T> T object(String name, Class<T> type) {
        final Object result = object(name);
        try { // Try casting to the desired type
            return type.cast(result);
        } catch (ClassCastException e) {
            final String f = "Unable to cast resource '%s' to type '%s'.";
            final MissingResourceException t = new MissingResourceException(String.format(f, name, type), name(), name);
            t.initCause(e);
            throw t;
        }
    }

    /**
     * Retrieves a resource of the given name and returns it as a {@link String}
     * which is usually best for string constants or when the raw string content
     * of a template is needed.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the string content of the resource
     *
     * @throws MissingResourceException
     *             if the resource could not be found
     */
    String string(String name);

    /**
     * Parses a resource with the given constructor function in a value of
     * different type.
     *
     * <p>
     * Providing objects as resources is not very usual and it may be tricky or
     * have some limitations. In some cases, a conversion from the textual form
     * into the object form can be a more convenient way and provides the full
     * control of the object retrieval to the client. This is what this method
     * offers.
     *
     * @param <T>
     *            the type of the result
     * @param name
     *            the name of the resource. It must not be {@code null}.
     * @param constructor
     *            the constructing function that parses the resource string and
     *            returns the result. It must not be {@code null}.
     *
     * @return the result of the constructor function
     *
     * @throws MissingResourceException
     *             if the resource could not be retrieved
     */
    default <T> T value(String name, Function<? super String, ? extends T> constructor) {
        return constructor.apply(string(name));
    }

    /**
     * Indicates whether this resource source provides any resource of the given
     * name.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return {@code true} iff a resoure of the given name does exist
     */
    boolean provides(String name);

    /**
     * Attempts to retrieve the resource with the given name.
     *
     * <p>
     * Unlike other resource-retrieving methods, this method rather returns an
     * empty {@link Optional} if the resource could not be found, leaving the
     * resolution of the failure to the caller fully. The result allows other
     * comfortable operations like mapping the resource to a different type etc.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the resource, or an empty {@link Optional} if the resource is
     *         missing
     */
    default Optional<?> lookup(String name) {
        if (provides(name)) {
            try {
                return Optional.ofNullable(object(name));
            } catch (MissingResourceException e) {
                // Ignore the error, go for empty; this is a safety block to
                // avoid problems with not-so-good implementations
            }
        }

        return Optional.empty();
    }

    // Template support

    /**
     * Constructs a template from a resource with the given name and returns the
     * template.
     *
     * <p>
     * The default implementation uses {@link #value(String, Function)} to parse
     * the string into a {@link ChoiceFormat} which then performs the actual
     * formatting of the template.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the template
     *
     * @throws MissingResourceException
     *             if the resource could not be found
     */
    default DecimalTemplate decimal(String name) {
        return value -> new ChoiceFormat(string(name)).format(value);
    }

    /**
     * Constructs a template from a resource with the given name and returns the
     * template.
     *
     * <p>
     * The default implementation uses {@link #value(String, Function)} to parse
     * the string into a {@link ChoiceFormat} which then performs the actual
     * formatting of the template.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the template
     *
     * @throws MissingResourceException
     *             if the resource could not be found
     */
    default IntegerTemplate integer(String name) {
        return value -> new ChoiceFormat(string(name)).format(value);
    }

    /**
     * Constructs a template from a resource with the given name and returns the
     * template.
     *
     * <p>
     * The default implementation uses {@link #string(String)} to retrive the
     * pattern for {@link MessageFormat} which then performs the actual
     * formatting of the template.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the template
     *
     * @throws MissingResourceException
     *             if the resource could not be found
     */
    MessageTemplate message(String name);

    // Resource context support

    /**
     * Opens a resource context for this provider, making it the current
     * implicit provider.
     *
     * <p>
     * This method should be used in the following way:
     *
     * <pre>
     * try (ResourceContext rc = resourceProvider.context()) {
     *     // Here 'resourceProvider' is the implicit resource provider
     * }
     * </pre>
     *
     * @return the context for this provider
     */
    default ResourceContext context() {
        return ResourceContext.open(this);
    }

    /**
     * Returns a result of the specified supplier that is invoked within the
     * {@link #context()} of this instance.
     *
     * @param <T>
     *            the type of the result
     * @param supplier
     *            the supplier to execute. It must not be {@code null}.
     *
     * @return the result of the supplier
     */
    default <T> T supply(Supplier<? extends T> supplier) {
        try (ResourceContext context = context()) {
            return supplier.get();
        }
    }

    /**
     * Executes the given action within the {@link #context()} of this instance.
     *
     * @param action
     *            the action to execute. It must not be {@code null}.
     */
    default void execute(Runnable action) {
        try (ResourceContext context = context()) {
            action.run();
        }
    }
}
