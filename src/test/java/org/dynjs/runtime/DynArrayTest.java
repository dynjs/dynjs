/**
 *  Copyright 2012 Douglas Campos, and individual contributors
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
package org.dynjs.runtime;

import org.junit.Before;
import org.junit.Test;

import static org.dynjs.runtime.DynThreadContext.UNDEFINED;
import static org.fest.assertions.Assertions.assertThat;

public class DynArrayTest {

    private DynArray array;

    @Before
    public void setUp() throws Exception {
        array = new DynArray();
    }

    @Test
    public void undefinedByDefault() {
        assertThat(array.get(11)).isEqualTo(UNDEFINED);
        assertThat(array.get(550)).isEqualTo(UNDEFINED);
    }

    @Test
    public void storesAndRetrievesValues() {
        Object o1 = new Object();
        array.set(10, o1);
        assertThat(array.get(10)).isEqualTo(o1);
    }

    @Test
    public void growsAsNeeded() {
        final Object o1 = new Object();
        array.set(17, o1);
        assertThat(array.get(17)).isEqualTo(o1);
        assertThat(array.length()).isEqualTo(18);
    }
}
