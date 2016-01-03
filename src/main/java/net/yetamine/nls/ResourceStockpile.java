package net.yetamine.nls;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a type providing resource references using {@link ResourceDefinition}.
 *
 * <p>
 * The annotated type should have no static initialization blocks with
 * side-effects or external dependencies that might provide loading its class in
 * isolation. The type should declare inidividual resources as constants (i.e.,
 * {@code public static final} fields) of {@link ResourceReference} type, being
 * annotated with {@link ResourceDefinition}.
 *
 * <p>
 * The described arrangement allows automated construction of the default
 * resources directly from the code, which is suitable for both never-failing
 * resource provisioning and/or for generating the localization templates for
 * subsequent translation.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceStockpile {

    /**
     * Indicates the format of the data which the annotated type can supply.
     *
     * <p>
     * The name should be a name of class or package where the format is defined
     * or implemented. The default refers to {@link java.util.ResourceBundle}.
     *
     * @return the format of the data
     */
    String format() default "java.util.ResourceBundle";
}
