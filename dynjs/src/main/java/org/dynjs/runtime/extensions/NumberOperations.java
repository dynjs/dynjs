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
package org.dynjs.runtime.extensions;

import org.dynjs.runtime.linker.anno.CompanionFor;

@CompanionFor(Double.class)
public class NumberOperations {

    public static Double add(Double a, Double b) {
        return a + b;
    }

    public static Double sub(Double a, Double b) {
        return a - b;
    }

    public static Double mul(Double a, Double b) {
        return a * b;
    }

    public static Double div(Double a, Double b) {
        return a / b;
    }

    public static Double mod(Double a, Double b) {
        return a % b;
    }

    public static Boolean eq(Double a, Double b) {
        if (a.isNaN() || b.isNaN()) {
            return false;
        }
        if (a.equals(b)) {
            return true;
        }
        return false;
    }

    public static Boolean neq(Double a, Double b) {
        return !eq(a, b);
    }

    public static Boolean lt(Double a, Double b) {
        return a < b;
    }

    public static Boolean gt(Double a, Double b) {
        return a > b;
    }

    public static Boolean le(Double a, Double b) {
        return a <= b;
    }

    public static Boolean ge(Double a, Double b) {
        return a >= b;
    }

}
