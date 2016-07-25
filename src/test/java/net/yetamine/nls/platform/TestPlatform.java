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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import org.testng.Assert;
import org.testng.annotations.Test;

import net.yetamine.nls.ResourceContext;
import net.yetamine.nls.ResourceObject;
import net.yetamine.nls.ResourcePackage;
import net.yetamine.nls.ResourceProvider;

/**
 * Tests {@link net.yetamine.nls.platform} implementation.
 */
public final class TestPlatform {

    /**
     * Tests messages functionality.
     */
    @Test
    public void testMessages() {
        final ResourcePackage resources = ResourceBundleProvider.bundle(TestingResources.class);

        final ResourcePackage en = resources.locale(Locale.ENGLISH);
        Assert.assertEquals(TestingResources.MR.use(en), "Mr.");
        Assert.assertEquals(TestingResources.MR.using(en).get(), "Mr.");
        Assert.assertEquals(TestingResources.MR.bind(en).apply(), "Mr.");
        Assert.assertFalse(en.lookup("unknown").isPresent());
        Assert.assertFalse(en.provides("unknown"));

        final ResourcePackage de = resources.locale(() -> Locale.GERMAN);
        Assert.assertEquals(TestingResources.MR.use(de), "Herr");
        Assert.assertEquals(TestingResources.MR.using(de).get(), "Herr");
        Assert.assertEquals(TestingResources.MR.bind(de).apply(), "Herr");
        Assert.assertFalse(de.lookup("unknown").isPresent());
        Assert.assertFalse(de.provides("unknown"));

        final ResourcePackage cs = resources.locale(new Locale("cs")).resolve();
        Assert.assertEquals(TestingResources.MR.use(cs), "pane");
        Assert.assertEquals(TestingResources.MR.using(cs).get(), "pane");
        Assert.assertEquals(TestingResources.MR.bind(cs).apply(), "pane");
        Assert.assertFalse(cs.lookup("unknown").isPresent());
        Assert.assertFalse(cs.provides("unknown"));

        final ResourcePackage fr = resources.locale(() -> Locale.FRENCH).resolve();

        { // Use an integral argument
            final String message = "Hello Mr. Smith, your subscription ends today.";
            Assert.assertEquals(TestingResources.TEXT.use(fr).with(TestingResources.MR.use(fr), "Smith", 0), message);
        }

        { // Use a decimal argument instead
            final String m = "Hello Mr. Smith, your subscription ends in 10.1 days.";
            Assert.assertEquals(TestingResources.TEXT.use(fr).with(TestingResources.MR.use(fr), "Smith", 10.1), m);
        }

        { // Use a decimal argument instead - test the comma for German formatting
            final String m = "Halo, Herr Smith, Dein Abonnement l\u00e4uft in 10,1 Tage ab.";
            Assert.assertEquals(TestingResources.TEXT.use(de).with(TestingResources.MR.use(de), "Smith", 10.1), m);
        }
    }

    /**
     * Tests numbers functionality.
     */
    @Test
    public void testNumbers() {
        final ResourcePackage resources = ResourceBundleProvider.bundle(TestingResources.class);

        final ResourcePackage de = resources.locale(Locale.GERMAN);
        Assert.assertEquals(TestingDiscovery.INT.use(de).apply(0), "Null");
        Assert.assertEquals(TestingDiscovery.INT.use(de).with(0), "Null");

        final ResourcePackage cs = resources.locale(new Locale("cs")).resolve();
        Assert.assertEquals(TestingDiscovery.DEC.use(cs).apply(0), "nula");
        Assert.assertEquals(TestingDiscovery.DEC.use(cs).with(1), "jedna");
        Assert.assertEquals(TestingDiscovery.DEC.use(cs).with(1.5), "moc");
        Assert.assertEquals(TestingDiscovery.DEC.use(cs).apply(0.5), "nula");

        Assert.expectThrows(MissingResourceException.class, () -> {
            TestingDiscovery.INT.use(resources.locale(Locale.FRENCH)).apply(0);
        });
    }

    /**
     * Tests try-with-resources support for context-aware uses.
     */
    @Test
    public void testContext() {
        final ResourcePackage resources = ResourceBundleProvider.bundle(TestingResources.class);

        try (ResourceContext cs = resources.locale(new Locale("cs")).context()) {
            Assert.assertEquals(TestingDiscovery.DEC.use().apply(0), "nula");
            Assert.assertEquals(TestingDiscovery.DEC.use().with(1), "jedna");
            Assert.assertEquals(TestingResources.MR.use(), "pane");
        }
    }

    /**
     * Tests discovery support.
     */
    @Test
    public void testDiscovery() {
        final ResourcePackage some = ResourceBundleProvider.discover(null, TestingResources.class);
        Assert.assertFalse(some.locale(Locale.ENGLISH).provides("number"));

        final ResourcePackage all = ResourceBundleProvider.discover(null, TestingResources.class,
                TestingDiscovery.class);
        Assert.assertTrue(all.locale(Locale.GERMAN).provides("number"));

        // Uses the hard-wired values fallback when no root bundle is available
        final ResourcePackage num = ResourceBundleProvider.discover(null, TestingDiscovery.class);
        Assert.assertEquals(TestingDiscovery.INT.use(num.locale(Locale.ENGLISH)).apply(1), "one");
    }

    /**
     * Tests object-value support.
     */
    @Test
    public void testObjects() {
        final Map<String, Object> map = new HashMap<>();
        map.put("test1", Integer.valueOf(1));
        map.put("test2", Collections.unmodifiableList(Arrays.asList("test")));
        map.put("test3", null);
        final ResourceProvider bundle = ResourceBundleProvider.adapt(new MapResourceBundle(map));

        final ResourceObject<Integer> o1 = ResourceObject.declare("test1");
        final ResourceObject<Collection<String>> o2 = ResourceObject.constant("test2", Collections.singleton("oops"));
        final ResourceObject<Integer> o3 = ResourceObject.supplier("test3", o -> Integer.valueOf(2));
        final ResourceObject<Integer> o4 = ResourceObject.declare("test4");

        try (ResourceContext rc = bundle.context()) {
            Assert.assertEquals(o1.use(), Integer.valueOf(1));
            Assert.assertEquals(o2.use(), Arrays.asList("test"));
            Assert.assertEquals(o3.use(), Integer.valueOf(2));
            Assert.expectThrows(MissingResourceException.class, o4::use);
        }
    }
}
