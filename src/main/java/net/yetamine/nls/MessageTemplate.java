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
         * @see net.yetamine.nls.ResourceReference#from(net.yetamine.nls.ResourcePackage)
         */
        default MessageTemplate from(ResourcePackage resources) {
            return resources.message(name());
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
