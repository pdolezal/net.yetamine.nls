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

package net.yetamine.nls.discovery;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.yetamine.nls.ResourceReference;

/**
 * Marks a resource definition for resource discovery.
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
public @interface ResourceString {

    /**
     * Returns the default content of the resource.
     *
     * <p>
     * The format of the content is implied by the definition context, e.g., it
     * may be indicated by {@link ResourceStockpile} annotation on the enclosing
     * class.
     *
     * @return the default content of the resource
     */
    String value();

    /**
     * Returns the name of the resource.
     *
     * <p>
     * This attribute should be used when for some reason the automatic name
     * resolution can't work, e.g., the annotated field holds no instance of
     * {@link ResourceReference}, but rather an object to be used for retrieving
     * the resource directly, or when a tool extracting the data directly from a
     * class file shall be used for assembling the resource package etc.
     *
     * @return the name of the resource, or an empty string if the name shall
     *         not override the name implied by the resource constant itself
     */
    String name() default "";
}
