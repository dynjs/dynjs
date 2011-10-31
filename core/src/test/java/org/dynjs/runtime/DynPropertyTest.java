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
package org.dynjs.runtime;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DynPropertyTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void allowsAttributeChaining() {
        DynProperty property = new DynProperty("x")
                .setAttribute("value", DynThreadContext.UNDEFINED)
                .setAttribute("writable", true)
                .setAttribute("enumerable", true)
                .setAttribute("configurable", true);
        assertThat(property.getAttribute("value")).isEqualTo(DynThreadContext.UNDEFINED);
        assertThat(property.getAttribute("writable")).isEqualTo(true);
        assertThat(property.getAttribute("enumerable")).isEqualTo(true);
        assertThat(property.getAttribute("configurable")).isEqualTo(true);
    }

}
