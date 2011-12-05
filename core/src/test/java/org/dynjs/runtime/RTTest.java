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

import org.dynjs.api.Function;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class RTTest {

    private DynThreadContext context;
    private DynObject scope;

    @Before
    public void setUp() {
        context = new DynThreadContext();
        scope = new DynObject();
    }

    @Test
    public void testFunctionCall() throws Throwable {
        Function f = new BaseFunction() {
            @Override
            public Object call(DynThreadContext context, Object[] arguments) {
                return "";
            }

        };

        Object result = RT.FUNCTION_CALL.bindTo(f).invoke(context, new Object[]{});
        assertThat(result)
                .isNotNull()
                .isInstanceOf(String.class);

    }

}
