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
 * Testing resources using explicitly defined defaults and custom names, based
 * on a regular public class. This is an alternative to enums, which allows an
 * arbitrary constant mix in a single class.
 *
 * <p>
 * This resource set uses an enum for titles constants and provides the class as
 * public and visible publicly to the NLS discovery mechanism directly without a
 * special measure.
 */
@SuppressWarnings("javadoc")
@ResourceStockpile(format = "java.util.ResourceBundle")
public final class TestingResources2 {

    // Constant names are intentionally equal to the names of TestingResource1,
    // so that the string of the constructor is tested for applying.

    @ResourceString("Year")
    public static final StringConstant MR = StringConstant.name("YEAR");

    @ResourceString("Month")
    public static final StringConstant MS = StringConstant.name("MONTH");

    @ResourceString("Week")
    public static final StringConstant MRS = StringConstant.name("WEEK");

    @ResourceString("Day")
    public static final StringConstant MISS = StringConstant.name("DAY");

    @ResourceString("MR.")
    public static final StringConstant TITLE = StringConstant.name("MR"); // Intentionally overwrites TestingResource1.MR

    // Intentionally omitted annotation: this field should not be found
    public static final StringConstant UNKNOWN = StringConstant.name("UNKNOWN");

    private TestingResources2() {
        throw new AssertionError();
    }
}
