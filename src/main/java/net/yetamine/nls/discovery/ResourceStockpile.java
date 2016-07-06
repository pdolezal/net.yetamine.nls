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
 * Marks a type providing resource references using {@link ResourceString}.
 *
 * <p>
 * The annotated type should have no static initialization blocks with
 * side-effects or external dependencies that might provide loading its class in
 * isolation. The type should declare individual resources as constants (i.e.,
 * {@code public static final} fields) of {@link ResourceReference} type, being
 * annotated with {@link ResourceString}.
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
