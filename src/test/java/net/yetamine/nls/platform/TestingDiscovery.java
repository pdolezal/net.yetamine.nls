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

package net.yetamine.nls.platform;

import net.yetamine.nls.DecimalTemplate;
import net.yetamine.nls.IntegerTemplate;
import net.yetamine.nls.discovery.ResourceStockpile;
import net.yetamine.nls.discovery.ResourceString;

/**
 * Testing resources for the platform implementation tests. Supplied as a
 * separate class to test composition of multiple classes for discovery
 * inspection. This class contains just numeric templates.
 */
@ResourceStockpile
final class TestingDiscovery {

    @ResourceString("0#zero|1#one|1<many")
    public static final IntegerTemplate.Reference INT = IntegerTemplate.Reference.to("number");

    // Can't use @ResourceString("0#zero|1#one|1<many") to avoid duplicate key definition
    public static final DecimalTemplate.Reference DEC = DecimalTemplate.Reference.to("number");

    private TestingDiscovery() {
        throw new AssertionError();
    }
}
