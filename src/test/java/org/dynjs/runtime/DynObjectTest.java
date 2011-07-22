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

import static org.dynjs.runtime.helpers.hamcrest.IsUndefined.undefined;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;


public class DynObjectTest {

    private DynObject object;

    @Before
    public void setUp() throws Exception {
        object = new DynObject();
    }

    @Test
    public void returnsUndefinedForUnknownAttributes() {
        assertThat(object.get("prototype"), is(undefined()));
    }

    @Test
    public void allowsSettingAttributes() {
        final DynObject person = new DynObject();
        object.set("person").to(person);
        assertThat(object.get("person"), is(undefined()));
    }

    @Test
    public void searchesThePrototypeChain() {
        final DynObject car = new DynObject();
        final DynString blue = new DynString("blue");
        car.set("color").to(blue);
        object.set("prototype").to(car);
        assertThat(object.get("color"), is(not(undefined())));
    }
}
