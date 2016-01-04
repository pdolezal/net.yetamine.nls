package net.yetamine.nls;

import java.text.ChoiceFormat;
import java.text.MessageFormat;

/**
 * A source of templates and strings.
 */
public interface ResourceSupplier {

    /**
     * Retrieves a resource of the given name and returns it as a {@link String}
     * which is usually best for string constants or when the raw string content
     * of a template is needed.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the string content of the resource
     */
    String string(String name);

    /**
     * Constructs a template from a resource with the given name and returns the
     * template.
     *
     * <p>
     * The default implementation is based on {@link ChoiceFormat} while using
     * {@link #string(String)} to load the formatting pattern.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the template
     */
    default DecimalTemplate decimal(String name) {
        return value -> new ChoiceFormat(string(name)).format(value);
    }

    /**
     * Constructs a template from a resource with the given name and returns the
     * template.
     *
     * <p>
     * The default implementation is based on {@link ChoiceFormat} while using
     * {@link #string(String)} to load the formatting pattern.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the template
     */
    default IntegerTemplate integer(String name) {
        return value -> new ChoiceFormat(string(name)).format(value);
    }

    /**
     * Constructs a template from a resource with the given name and returns the
     * template.
     *
     * <p>
     * The default implementation is based on {@link MessageFormat} while using
     * {@link #string(String)} to load the formatting pattern.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the template
     */
    default MessageTemplate message(String name) {
        return args -> MessageFormat.format(string(name), args);
    }

    /**
     * Establishes a local context for this supplier.
     *
     * @return the established context
     */
    default ResourceContext context() {
        return ResourceContext.open(this);
    }
}
