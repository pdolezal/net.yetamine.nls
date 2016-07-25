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

package net.yetamine.nls.discovery.testing;

import java.lang.invoke.MethodHandles;

/**
 * A bridge to access resource classes for the testing purposes.
 */
@SuppressWarnings("javadoc")
public final class TestingResources {

    // Testing resources classes

    public static final Class<?> TR1 = TestingResources1.class;
    public static final Class<?> TR2 = TestingResources2.class;
    public static final Class<?> TR3 = TestingResources3.class;

    /**
     * Provides a private lookup object with the privileges of this class.
     *
     * @return a private lookup object
     */
    public static MethodHandles.Lookup getLookup() {
        return MethodHandles.lookup();
    }

    private TestingResources() {
        throw new AssertionError();
    }
}
