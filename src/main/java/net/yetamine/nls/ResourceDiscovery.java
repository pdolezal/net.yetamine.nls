package net.yetamine.nls;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A support for building resource packages from annotated classes providing the
 * resource definitions.
 *
 * <p>
 * Following code snippet demonstrates the typical use of this class:
 *
 * <pre>
 * final Map&gt;String, String&lt; map = new HashMap&gt;&lt;(); // The map with the retrieved resources
 * final ResourceDiscovery discovery = new ResourceDiscovery(map::put).lookup(MethodHandles.lookup());
 * discovery.add(clazz); // Assuming: discovery.test(clazz) == true
 * // Now 'map' contains the discovered resources for 'clazz'
 * </pre>
 */
public final class ResourceDiscovery implements Predicate<Class<?>>, Consumer<Class<?>> {

    /** Acceptor of the result. */
    private final BiConsumer<? super String, ? super String> result;
    /** Condition for the formats that are supported by this instance. */
    private final Predicate<? super String> format;
    /** Lookup object for retrieving the fields. */
    private MethodHandles.Lookup lookup = null;

    /**
     * Creates a new instance.
     *
     * @param accumulator
     *            the accumulator of added fields. It must not be {@code null}.
     * @param supportedFormats
     *            the condition on supported formats. It must not be
     *            {@code null}.
     */
    public ResourceDiscovery(BiConsumer<? super String, ? super String> accumulator, Predicate<? super String> supportedFormats) {
        format = Objects.requireNonNull(supportedFormats);
        result = Objects.requireNonNull(accumulator);
    }

    /**
     * Creates a new instance.
     *
     * @param accumulator
     *            the accumulator of added fields. It must not be {@code null}.
     * @param supportedFormat
     *            the supported format. It must not be {@code null}.
     */
    public ResourceDiscovery(BiConsumer<? super String, ? super String> accumulator, String supportedFormat) {
        this(accumulator, Objects.requireNonNull(supportedFormat)::equals);
    }

    /**
     * Creates a new instance with the default format.
     *
     * @param accumulator
     *            the accumulator of added fields. It must not be {@code null}.
     */
    public ResourceDiscovery(BiConsumer<? super String, ? super String> accumulator) {
        this(accumulator, "java.util.ResourceBundle"::equals);
    }

    /**
     * Sets the lookup object for reflective inspection of the subsequently
     * added classes.
     *
     * @param value
     *            the value to set. If {@code null} (the initial value), then
     *            instead of the lookup object, classical reflection is used and
     *            then either the fields must be accessible, or this class must
     *            have sufficient privilege to reflectively read the fields (if
     *            the security manager is enabled).
     *
     * @return this instance
     */
    public ResourceDiscovery lookup(MethodHandles.Lookup value) {
        lookup = value;
        return this;
    }

    /**
     * Tests if the given class may be passed to {@link #add(Class)}.
     *
     * @param clazz
     *            the class to test. It must not be {@code null}.
     *
     * @return {@code true} if the class is properly annotated and supports the
     *         expected format(s)
     *
     * @see java.util.function.Predicate#test(java.lang.Object)
     */
    public boolean test(Class<?> clazz) {
        final ResourceStockpile stockpile = clazz.getAnnotation(ResourceStockpile.class);
        return ((stockpile != null) && format.test(stockpile.format()));
    }

    /**
     * This method is an alias for {@link #add(Class)}.
     *
     * @see java.util.function.Consumer#accept(java.lang.Object)
     */
    public void accept(Class<?> clazz) {
        add(clazz);
    }

    /**
     * Inspects the given class, discovering all available fields there.
     *
     * @param clazz
     *            the class to inspect. It must not be {@code null} and it must
     *            pass {@link #test(Class)}.
     *
     * @return this instance
     */
    public ResourceDiscovery add(Class<?> clazz) {
        if (!test(clazz)) {
            throw new IllegalArgumentException(clazz.toString());
        }

        // Cache the access control context for later invocations of adding fields
        final AccessControlContext acc = (lookup != null) ? AccessController.getContext() : null;

        Stream.of(clazz.getFields()).filter(field -> {
            final int modifiers = Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL;
            return (field.getModifiers() & modifiers) == modifiers;
        }).forEach(field -> add(field, acc));

        return this;
    }

    /**
     * Adds the resource defined by the given field
     *
     * @param field
     *            the field to inspect. It must not be {@code null} and it must
     *            be accessible with the current lookup, properly annotated and
     *            formatted.
     * @param acc
     *            the {@link AccessControlContext} for adding the field if not
     *            using the lookup object. It must not be {@code null} if no
     *            lookup is specified.
     */
    private void add(Field field, AccessControlContext acc) {
        final ResourceString definition = field.getAnnotation(ResourceString.class);
        if (definition == null) { // Definition missing, not a resource
            return;
        }

        final String override = definition.name();
        if (!override.isEmpty()) { // No need to read the field then
            result.accept(override, definition.value());
            return;
        }

        try { // Read the name from the field content
            final Object o = field.isAccessible() ? field.get(null) : readField(field, acc);

            if (o instanceof ResourceReference<?>) {
                result.accept(((ResourceReference<?>) o).name(), definition.value());
                return;
            }
        } catch (IllegalAccessException e) {
            throw new SecurityException(e);
        }

        throw new IllegalArgumentException(field.toString());
    }

    /**
     * Reads a static field.
     *
     * @param field
     *            the field to read. It must not be {@code null}.
     * @param acc
     *            the {@link AccessControlContext} for adding the field if not
     *            using the lookup object. It must not be {@code null} if no
     *            lookup is specified.
     *
     * @return the field content
     *
     * @throws IllegalAccessException
     *             if the access is disallowed
     * @throws SecurityException
     *             if the access is disallowed
     */
    private Object readField(Field field, AccessControlContext acc) throws IllegalAccessException {
        try {
            if (lookup != null) { // Use the lookup object if available
                return lookup.unreflectGetter(field).invoke();
            }

            assert (acc != null); // Warning: using this method is state-dependent
            final PrivilegedExceptionAction<?> read = () -> {
                field.setAccessible(true);
                try {
                    return field.get(null);
                } finally {
                    field.setAccessible(false);
                }
            };

            return AccessController.doPrivileged((PrivilegedExceptionAction<?>) read, acc);
        } catch (IllegalAccessException | RuntimeException | Error e) {
            throw e; // Let these propagate directly
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
