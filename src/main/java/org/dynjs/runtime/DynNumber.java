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

import static java.lang.Double.NaN;

public class DynNumber extends DynObject {

    public static final DynNumber NAN = new DynNumber(NaN);
    public static final DynNumber POSITIVE_INFINITY = new DynNumber(Double.POSITIVE_INFINITY);
    public static final DynNumber NEGATIVE_INFINITY = new DynNumber(Double.NEGATIVE_INFINITY);

    private final double value;

    public DynNumber(Double value) {
        this.value = value;
    }

    public DynNumber(Integer value) {
        this.value = value;
    }

    public DynNumber add(final DynNumber other) {
        return new DynNumber(getValue() + other.getValue());
    }

    public DynNumber sub(final DynNumber other) {
        return new DynNumber(this.value - other.value);
    }

    public DynNumber mul(final DynNumber other) {
        return new DynNumber(this.value * other.value);
    }

    public DynNumber div(final DynNumber other) {
        if (other.getValue() == 0) {
            return getValue() == 0 ? NAN : (getValue() < 0 ? NEGATIVE_INFINITY : POSITIVE_INFINITY);
        }
        return new DynNumber(getValue() / other.getValue());
    }

    public DynNumber mod(final DynNumber other) {
        if (other.getValue() == 0) {
            return NAN;
        }
        return new DynNumber(getValue() % other.getValue());
    }

    public static DynNumber parseInt(final String string) {
        return parseInt(string, new DynNumber(10.0));
    }

    public static DynNumber parseInt(final String string, final DynNumber radix) {
        String given = string.trim().toLowerCase();

        if (given.equals("")) {
            return NAN;
        }

        char firstChar = given.charAt(0);

        if (((firstChar < '0') || (firstChar > '9')) && ((firstChar != '+') && (firstChar != '-'))) {
            return NAN;
        }

        if (firstChar == '0') {
            if (given.startsWith("0x")) {
                return fromHex(removeRightInvalidCharacter
                        (given.substring(2), 16));
            }
        }

        return new DynNumber(0);
    }

    private static DynNumber fromHex(String value) {
        return new DynNumber(Integer.parseInt(value, 16));
    }

    private static String removeRightInvalidCharacter(String given, int radix) {
        return given;
    }

    public double getValue() {
        return value;
    }

    public boolean isNaN() {
        return Double.isNaN(getValue());
    }

    @Override
    public String toString() {
        return "DynNumber{" +
                "value=" + value +
                '}';
    }
}
