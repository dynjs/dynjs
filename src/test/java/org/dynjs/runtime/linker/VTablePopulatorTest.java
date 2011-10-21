/**
 *  Copyright 2011 Douglas Campos
 *  Copyright 2011 dynjs contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.runtime.linker;

import org.dynjs.runtime.linker.anno.CompanionFor;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.util.Map;

public class VTablePopulatorTest {

    @Test
    public void testVtableFrom() throws Exception {
        Map<String, MethodHandle> vtable = VTablePopulator.vtableFrom(VTableTest.class);
        Assertions.assertThat(vtable.keySet().size())
                .isEqualTo(1);
    }

    @CompanionFor
    public static class VTableTest {

        public void add(Double a, Double b) {

        }
    }
}
