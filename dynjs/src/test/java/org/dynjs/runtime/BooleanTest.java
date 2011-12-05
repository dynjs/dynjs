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

import static org.dynjs.runtime.DynObject.toBoolean;
import static org.dynjs.runtime.DynThreadContext.UNDEFINED;
import static org.dynjs.runtime.DynThreadContext.NULL;
import static org.fest.assertions.Assertions.assertThat;

public class BooleanTest {
    // http://es5.github.com/#x9.2
    // undefined    => false
    // null         => false
    // Boolean      => no conversion
    // Number       => 0, NaN = false
    // String       => "" = false
    // Object       => true

    @Test
    public void undefinedBecomesFalse() {
        assertThat(toBoolean(UNDEFINED)).isFalse();
    }

    @Test
    public void nullBecomesFalse() {
        assertThat(toBoolean(NULL)).isFalse();
    }

    @Test
    public void booleanIsNotConverted() {
        assertThat(toBoolean(true)).as("true").isTrue();

        assertThat(toBoolean(false)).as("false").isFalse();
    }

    @Test
    public void nanBecomesFalse() {
        assertThat(toBoolean(DynNumber.NAN)).isFalse();
    }

    @Test
    public void zeroBecomesFalse() {
        assertThat(toBoolean(new DynNumber(0))).isFalse();
    }

    @Test
    public void anotherValidNumberBecomesTrue() {
        assertThat(toBoolean(new DynNumber(42))).isTrue();
    }

    @Test
    public void emptyStringBecomesFalse() {
        assertThat(toBoolean("")).isFalse();
    }

    @Test
    public void nonEmptyStringBecomesTrue() {
        assertThat(toBoolean("zzz...")).isTrue();
    }

    @Test
    public void objectBecomesTrue() {
        assertThat(toBoolean(new DynObject())).isTrue();
    }
}