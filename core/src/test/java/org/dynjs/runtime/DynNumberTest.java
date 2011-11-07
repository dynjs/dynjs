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

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DynNumberTest {

    private static final int FOO_VALUE = 12;
    private static final int BAR_VALUE = 3;

    private static final DynNumber FOO = new DynNumber(FOO_VALUE);
    private static final DynNumber BAR = new DynNumber(BAR_VALUE);

    @Test
    public void testAddition() throws Exception {
        assertThat(FOO.add(BAR).getValue()).isEqualTo(FOO_VALUE + BAR_VALUE);
        assertThat(BAR.add(FOO).getValue()).isEqualTo(FOO_VALUE + BAR_VALUE);
    }

    @Test
    public void testSubtraction() throws Exception {
        assertThat(FOO.sub(BAR).getValue()).isEqualTo(FOO_VALUE - BAR_VALUE);
        assertThat(BAR.sub(FOO).getValue()).isEqualTo(BAR_VALUE - FOO_VALUE);
    }

    @Test
    public void testMultiplication() throws Exception {
        assertThat(FOO.mul(BAR).getValue()).isEqualTo(FOO_VALUE * BAR_VALUE);
        assertThat(BAR.mul(FOO).getValue()).isEqualTo(BAR_VALUE * FOO_VALUE);
    }

    @Test
    public void testParseIntWithDefaultRadix() throws Exception {
        assertThat(FOO.mul(BAR).getValue()).isEqualTo(FOO_VALUE * BAR_VALUE);
    }

    @Test
    public void testGetValue() throws Exception {
        assertThat(FOO.getValue()).isEqualTo(FOO_VALUE);
        assertThat(BAR.getValue()).isEqualTo(BAR_VALUE);
    }
}
