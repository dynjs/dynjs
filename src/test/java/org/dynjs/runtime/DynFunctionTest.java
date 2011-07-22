/**
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

import static org.dynjs.runtime.Argument.arg;
import static org.fest.assertions.Assertions.assertThat;

public class DynFunctionTest {

    private DynObject object;

    @Before
    public void setup() {
        object = new DynObject();
    }

    @Test
    public void testFunctionCall() {
        DynFunction<DynString> function = new DynFunction<DynString>() {{
            willReturn(new DynString("hello"));
        }};
        DynString result = function.call();
        assertThat(result).isNotNull();
    }

    @Test
    public void testDefaultReturnType() {
        DynFunction function = new DynFunction();
        assertThat(function.call()).isInstanceOf(Undefined.class);
    }

    @Test
    public void testArguments() {
        new DynFunction(arg("arg0", object)) {{
            addArgument("arg0", object);
            addArgument("arg1", object);
        }};
    }
}
