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

import net.yetamine.nls.MessageTemplate;
import net.yetamine.nls.StringConstant;

/**
 * Testing resources for the platform implementation tests.
 */
final class TestingResources {

    public static final StringConstant MR = StringConstant.name("MR");
    public static final StringConstant MS = StringConstant.name("MS");
    public static final StringConstant MRS = StringConstant.name("MRS");
    public static final StringConstant MISS = StringConstant.name("MISS");

    public static final MessageTemplate.Reference TEXT = MessageTemplate.Reference.to("text");

    private TestingResources() {
        throw new AssertionError();
    }
}
