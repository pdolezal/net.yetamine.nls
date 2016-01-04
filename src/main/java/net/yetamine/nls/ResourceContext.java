package net.yetamine.nls;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A local resource context.
 *
 * <p>
 * This class is designed for easier reusing the same supplier in a local code
 * scope, so that it is not necessary to specify the resource supplier for all
 * {@link ResourceReference#use(ResourceSupplier)} method invocations and the
 * {@link ResourceReference#use()} method may be used instead.
 *
 * <p>
 * The recommended technique is using the try-with-resources construct for that:
 *
 * <pre>
 * try (ResourceContext r = RESOURCES.context()) {
 *     // Not necessary to specify the source in 'use' invocation
 *     System.out.println(HELLO.use().with("Mr. Smith"));
 * }
 * </pre>
 *
 * The contexts use a thread local variable to make a stack of their instances,
 * hence only the top one can be used as the implicit operand for {@code use}.
 * It is strongly recommended to use only the try-with-resources idiom to avoid
 * leaks and unexpected behavior.
 *
 * <p>
 * Another pattern, probably even better in most situation, although not always
 * as flexible when multiple contexts might be useful in parallel, is using the
 * support provided by {@link ResourceSupplier} directly:
 *
 * <pre>
 * RESOURCES.execute(() -&gt; {
 *     System.out.println(HELLO.use().with("Mr. Smith"));
 * });
 * </pre>
 */
public final class ResourceContext implements AutoCloseable, Supplier<ResourceSupplier> {

    /** Head of the resource context chain, the current context actually. */
    private static final ThreadLocal<ResourceContext> CURRENT = new ThreadLocal<>();

    /** Adapted supplier. */
    private final ResourceSupplier supplier;
    /** Previous context in the chain. */
    private ResourceContext prev;
    /** Next context in the chain. */
    private ResourceContext next;

    /**
     * Creates a new instance.
     *
     * @param resourceSupplier
     *            the supplier to adapt. It must not be {@code null}.
     */
    private ResourceContext(ResourceSupplier resourceSupplier) {
        supplier = Objects.requireNonNull(resourceSupplier);
    }

    /**
     * Opens a new context and makes it the current one.
     *
     * @param supplier
     *            the supplier to adapt. It must not be {@code null}.
     *
     * @return the new context
     */
    public static ResourceContext open(ResourceSupplier supplier) {
        final ResourceContext result = new ResourceContext(supplier);

        final ResourceContext current = CURRENT.get();
        if (current != null) { // Hook it into the chain
            current.prev = result;
            result.next = current;
        }

        CURRENT.set(result);
        return result;
    }

    /**
     * Returns the supplier from the current context.
     *
     * @return the supplier from the current context, or an empty container if
     *         no context is available
     */
    public static Optional<ResourceSupplier> acquire() {
        final ResourceContext result = CURRENT.get();
        return (result != null) ? Optional.of(result.get()) : Optional.empty();
    }

    /**
     * Returns the supplier from the current context.
     *
     * @return the supplier from the current context
     *
     * @throws IllegalStateException
     *             if no context is available
     */
    public static ResourceSupplier require() {
        return acquire().orElseThrow(IllegalStateException::new);
    }

    /**
     * Returns the supplier bound to this context.
     *
     * @see java.util.function.Supplier#get()
     */
    public ResourceSupplier get() {
        return supplier;
    }

    /**
     * @see java.lang.AutoCloseable#close()
     */
    public void close() {
        final ResourceContext nextContext = next;
        final ResourceContext prevContext = prev;
        prev = next = null; // Prevent repeated closing

        if (nextContext != null) { // Unhook next
            nextContext.prev = prevContext;
        }

        if (prevContext != null) { // Unhook prev
            prevContext.next = nextContext;
        }

        if (CURRENT.get() == this) { // Unhook this
            CURRENT.set(nextContext);
        }
    }
}
