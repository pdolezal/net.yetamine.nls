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

/**
 * A simple façade for National Language Support (NLS) providing a fluent API.
 *
 * <h1>Overview</h1>
 *
 * The purpose of this package is to define and provide a façade for decreasing
 * the dependency of clients on the actual resource system implementation and to
 * provide a fluent API. While using the standard resource system provided via
 * {@link java.util.ResourceBundle} as the implementation remains possible and
 * it is actually the natural choice, so it is supported out of the box, it is
 * not the only choice.
 *
 * <p>
 * One notable difference from possible underlying implementations is that this
 * façade does not support non-string resources, or at least not directly. This
 * design decision reduces the amount of problems with the implementations and
 * makes the API more consistent and focused. When needing more freedom, using
 * string-mapping functionality allows to reach similar results as the direct,
 * but magic support of them from the resource system.
 */
package net.yetamine.nls;
