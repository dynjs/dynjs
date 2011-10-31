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

import org.fest.assertions.DoubleAssert;
import org.junit.Test;

import static org.dynjs.runtime.DynNumber.NAN;
import static org.dynjs.runtime.DynNumber.parseInt;
import static org.fest.assertions.Assertions.assertThat;

public class ParseIntTest {

    // notes to myself while developing:
    // we have some cases here:
    // 1. spaces will be trimmed
    // 2. an empty string returns NaN
    // 3. a valid number starts with +, - or [0-9]
    // 4. a number starting with 0x with default radix is hexa
    // 5. a number starting with 0x with not default radix is NaN
    // 6. a number starting with 0 with default radix is octal
    // 7. anything else after valid numbers will be removed
    // 8.

    @Test
    public void spacesWillBeTrimmed() {
        assertThat(parseInt("  ")).as("blank spaces").isEqualTo(NAN);
        assertThatValueOf(parseInt("  0 ")).as("integer number").isEqualTo(0);
        assertThatValueOf(parseInt("  0.0 ")).as("float number").isEqualTo(0);
    }

    @Test
    public void emptyStringReturnsNaN() {
        assertThat(parseInt("")).isEqualTo(NAN);
    }

    @Test
    public void validNumbersStartWithPlusMinusOrDigits() {
        assertThat(parseInt("a")).isEqualTo(NAN);
        assertThat(parseInt("x")).isEqualTo(NAN);
        assertThat(parseInt("FE")).isEqualTo(NAN);
        assertThat(parseInt("*3")).isEqualTo(NAN);
        assertThat(parseInt(".1")).isEqualTo(NAN);

        assertThat(parseInt("0.1")).isNotEqualTo(NAN);
        assertThat(parseInt("9999999")).isNotEqualTo(NAN);
        assertThat(parseInt("+2")).isNotEqualTo(NAN);
        assertThat(parseInt("+ 2")).isNotEqualTo(NAN);
        assertThat(parseInt("-3")).isNotEqualTo(NAN);
        assertThat(parseInt("- 3")).isNotEqualTo(NAN);
    }

    @Test
    public void numbersStartingWith0xIsHexadecimal() {
        assertThatValueOf(parseInt("0xff")).isEqualTo(0xff);
    }

    private DoubleAssert assertThatValueOf(DynNumber number) {
        return assertThat(number.getValue());
    }
}