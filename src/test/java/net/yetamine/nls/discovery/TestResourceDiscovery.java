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

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import net.yetamine.nls.discovery.testing.TestingResources;

/**
 * Tests {@link ResourceDiscovery}.
 */
public final class TestResourceDiscovery {

    // Testing:
    //
    // Reference with enums
    // Reference with different format
    // Reference with no @ResourceString
    // Multiple classes

    /**
     * Tests the typical situation using the defaults if possible.
     */
    @Test
    public void testDefaults() {
        final Map<String, Object> result = new HashMap<>();
        final ResourceDiscovery rd = new ResourceDiscovery(result::put);

        Assert.assertTrue(rd.test(TestingResources.TR1));
        Assert.assertTrue(rd.test(TestingResources.TR2));
        Assert.assertFalse(rd.test(TestingResources.TR3));

        // Tests adding resources of TestingResource1
        final Map<String, Object> tr1 = new HashMap<>();
        tr1.put("MR", "Mr.");
        tr1.put("MS", "Ms.");
        tr1.put("MRS", "Mrs.");
        tr1.put("MISS", "Miss.");

        Assert.assertSame(rd.inspect(TestingResources.TR1), rd);
        Assert.assertEquals(result, tr1);

        // Tests adding resources of TestingResource2
        final Map<String, Object> tr2 = new HashMap<>();
        tr2.put("YEAR", "Year");
        tr2.put("MONTH", "Month");
        tr2.put("WEEK", "Week");
        tr2.put("DAY", "Day");
        tr2.put("MR", "MR.");

        final Map<String, Object> tr = new HashMap<>(tr1);
        tr.putAll(tr2); // Tests overwritting "MR" resource

        rd.accept(TestingResources.TR2); // Test accept() behavior
        Assert.assertEquals(result, tr);

        Assert.expectThrows(IllegalArgumentException.class, () -> rd.accept(TestingResources.TR3));
        Assert.assertEquals(result, tr); // No change can happen
    }

    /**
     * Tests accessing a non-public class with name override and different
     * format.
     */
    @Test
    public void testSpecials() {
        final Map<String, Object> result = new HashMap<>();
        final ResourceDiscovery rd = new ResourceDiscovery(result::put, "java.lang.String::format");

        Assert.assertTrue(rd.test(TestingResources.TR3));

        // Tests adding resources of TestingResource3 with reflection only
        final Map<String, Object> tr3 = new HashMap<>();
        tr3.put("HELLO", "Hello %1$s %2$s.");
        tr3.put("GOODBYE", "Good bye, %1$s.");

        rd.accept(TestingResources.TR3); // This fails with SecurityManager installed, or without access permissions
        Assert.assertEquals(result, tr3);
        result.clear();

        // Tests adding resources of TestingResource3 with a lookup handle
        Assert.assertSame(rd.lookup(TestingResources.getLookup()), rd);
        rd.accept(TestingResources.TR3); // This never fails
        Assert.assertEquals(result, tr3);
    }
}
