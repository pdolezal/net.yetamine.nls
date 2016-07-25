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

import net.yetamine.nls.StringConstant;
import net.yetamine.nls.discovery.ResourceStockpile;
import net.yetamine.nls.discovery.ResourceString;

/**
 * Testing resources using defaults and minimal definitions, based on an
 * {@code enum}.
 *
 * <p>
 * This resource set uses an enum for titles constants and provides the class as
 * public and visible publicly to the NLS discovery mechanism directly without a
 * special measure.
 */
@SuppressWarnings("javadoc")
@ResourceStockpile
public enum TestingResources1 implements StringConstant {

    @ResourceString("Mr.")
    MR,

    @ResourceString("Ms.")
    MS,

    @ResourceString("Mrs.")
    MRS,

    @ResourceString("Miss.")
    MISS;
}
