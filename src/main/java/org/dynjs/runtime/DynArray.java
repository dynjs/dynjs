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

import java.util.Arrays;

public class DynArray {

    public static final int DEFAULT_ARRAY_SIZE = 16;
    public static final Object[] UNDEFINED_PREFILLED_ARRAY;

    static {
        Object[] prefilledArray = new Object[DEFAULT_ARRAY_SIZE * 8];
        for (int i = 0; i < prefilledArray.length; i++) {
            prefilledArray[i] = UNDEFINED;
        }
        UNDEFINED_PREFILLED_ARRAY = prefilledArray;
    }

    private Object[] array;

    public DynArray() {
        this(DEFAULT_ARRAY_SIZE);
    }

    public DynArray(int size) {
        this.array = new Object[size];
        fillUndefinedArray(this.array, 0, this.array.length);
    }

    private void fillUndefinedArray(Object[] array, int from, int to) {
        int i;
        for (i = from; i + UNDEFINED_PREFILLED_ARRAY.length < to; i += UNDEFINED_PREFILLED_ARRAY.length) {
            System.arraycopy(UNDEFINED_PREFILLED_ARRAY, 0, array, i, UNDEFINED_PREFILLED_ARRAY.length);
        }
        System.arraycopy(UNDEFINED_PREFILLED_ARRAY, 0, array, i, to - i);
    }

    public void set(int index, Object value) {
        growIfNeeded(index);
        array[index] = value;
    }

    private void growIfNeeded(int index) {
        if (!checkBounds(index)) {
            Object[] reallocated = new Object[index + 1];
            System.arraycopy(this.array, 0, reallocated, 0, this.array.length);
            fillUndefinedArray(reallocated, this.array.length, index);
            this.array = reallocated;
        }
    }

    public Object get(int index) {
        if (checkBounds(index)) {
            return array[index];
        }
        return UNDEFINED;
    }

    private boolean checkBounds(int index) {
        return index < array.length;
    }

    public int length() {
        return array.length;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.array);
    }
}
