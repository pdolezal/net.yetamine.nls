package net.yetamine.nls;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a resource definition for automated resource bundle generation.
 *
 * <p>
 * The annotation should be applied on a {@code public static final} field (that
 * is a constant) and the field type should be an {@link ResourceReference} type
 * that defines the resource and supplies the name of the resource. However, if
 * the type is different, it is still possible to supply the actual name using
 * the {@link #name()} field.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceDefinition {

    /**
     * Returns the default content of the resource.
     *
     * <p>
     * The format of the content is implied by the context, e.g., it may be
     * indicated by {@link ResourceDefinition} annotation on the enclosing
     * class.
     *
     * @return the default content of the resource
     */
    String value();

    /**
     * Returns the name of the resource.
     *
     * @return the name of the resource, or an empty string if the name shall
     *         override the name implied by the resource constant itself
     */
    String name() default "";
}
