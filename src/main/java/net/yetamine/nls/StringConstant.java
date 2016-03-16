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

package net.yetamine.nls;

import java.util.Objects;

/**
 * Represents a constant string provided as a resource with the given name.
 */
@FunctionalInterface
public interface StringConstant extends ResourceObject<String> {

    /**
     * @see net.yetamine.nls.ResourceReference#use(net.yetamine.nls.ResourceProvider)
     */
    default String use(ResourceProvider resources) {
        return resources.string(name());
    }

    /**
     * Creates a new instance with the given name.
     *
     * @param name
     *            the name of the resource. It must not be {@code null}.
     *
     * @return the new instance
     */
    static StringConstant name(String name) {
        Objects.requireNonNull(name);
        return () -> name;
    }
}
