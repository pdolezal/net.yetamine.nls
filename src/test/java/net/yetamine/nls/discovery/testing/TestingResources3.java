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

import net.yetamine.nls.MessageTemplate;
import net.yetamine.nls.discovery.ResourceStockpile;
import net.yetamine.nls.discovery.ResourceString;

/**
 * Testing resources with the custom format and custom definitions, based on an
 * {@code enum}.
 *
 * <p>
 * This resource set uses an enum for greetings constants, but keeps the class
 * package-private, so the NLS discovery mechanism needs sufficient access
 * permissions, or a privileged method lookup handle must be provided.
 */
@ResourceStockpile(format = "java.lang.String::format")
enum TestingResources3 implements MessageTemplate.Reference {

    @ResourceString("Hello %1$s %2$s.")
    HELLO,

    @ResourceString(name = "GOODBYE", value = "Good bye, %1$s.")
    BYE;
}
