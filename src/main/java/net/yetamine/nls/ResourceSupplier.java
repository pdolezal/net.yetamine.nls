package net.yetamine.nls;

import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A resource supplier interface.
 *
 * <p>
 * This interface is the core interface of the whole library as it encapsulates
 * a source of resources for other parts of the library and for clients as well.
 * The design of this interface assumes that all resources are representable as
 * strings and all related resources are parsed from the strings. Because of
 * that, {@link #string(String)} is supposed to be the common denominator of
 * other resource-supplying methods, except for {@link #object(String)} that
 * supports in a way object resources as well.
 *
 * <p>
 * However, this assumption does not prevent an implementation from parsing the
 * string representation of a resource once and reusing it later. Other options,
 * like separated resource types, are possible as well, but those must be then
 * reflected when using the resource discovery feature.
 */
public interface ResourceSupplier {

    /**
     * Returns the name of this resource source.
     *
     * @return the name of this resource source, or {@code null} if unknown
     */
    String name();

    /**
     * Retrieves a resource object of the given type.
     *
     * <p>
     * This method, unlike other resource-supplying methods, does not throw an
     * exception if the object is missing, but rather returns {@code null}. It
     * is not mentioned for direct use, but it provides rather a base for safe
     * access methods like {@link ResourceObject}.
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
     * @return the resource object, or {@code null} if missing
     */
    <T> T object(String name);

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
     * While this interface does not intentionally support object resources
     * directly, this method provides a way to reach a similar functionality.
     * The major advantage of this approach is that the client code has the full
     * control of the resource processing and does not have to rely on any magic
     * features of the resource system, which could vary with the underlying
     * implementation.
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
    default MessageTemplate message(String name) {
        return args -> MessageFormat.format(string(name), args);
    }

    // Resource context support

    /**
     * Opens a resource context for this supplier, making it the current
     * implicit supplier.
     *
     * <p>
     * This method should be used in the following way:
     *
     * <pre>
     * try (ResourceContext rc = resourceSupplier.context()) {
     *     // Here 'resourceSupplier' is the implicit resource supplier
     * }
     * </pre>
     *
     * @return the context for this supplier
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
