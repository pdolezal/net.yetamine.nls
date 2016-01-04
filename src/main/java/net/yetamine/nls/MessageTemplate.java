package net.yetamine.nls;

import java.util.Objects;

/**
 * A formatter of a message template.
 */
@FunctionalInterface
public interface MessageTemplate {

    /**
     * Represents a reference to a resource with the given name that shall be
     * resolved to a {@link MessageTemplate}.
     */
    @FunctionalInterface
    interface Reference extends ResourceReference<MessageTemplate> {

        /**
         * @see net.yetamine.nls.ResourceReference#use(net.yetamine.nls.ResourceSupplier)
         */
        default MessageTemplate use(ResourceSupplier resources) {
            return resources.message(name());
        }

        /**
         * @see net.yetamine.nls.ResourceReference#use()
         */
        default MessageTemplate use() {
            return use(ResourceContext.require());
        }

        /**
         * Binds this resource to a source.
         *
         * @param source
         *            the source to bind to. It must not be {@code null}.
         * @param args
         *            the arguments to bind. It must not be {@code null}.
         *
         * @return the resource binding
         */
        default ResourceBinding bind(ResourcePackage source, Object... args) {
            return new ResourceBinding(source, s -> use(s).with(args));
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
     * Applies the given arguments on this template and returns the formatted
     * text.
     *
     * @param args
     *            the arguments to use for formatting. It must not be
     *            {@code null}.
     *
     * @return the formatted text
     */
    String with(Object... args);
}
