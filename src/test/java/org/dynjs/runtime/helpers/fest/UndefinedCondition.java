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
package org.dynjs.runtime.helpers.fest;

import org.dynjs.runtime.Attribute;
import org.dynjs.runtime.DynAtom;
import org.fest.assertions.Condition;

public class UndefinedCondition<T> extends Condition<Attribute<DynAtom>> {

    @Override
    public boolean matches(Attribute<DynAtom> attribute) {
        if (attribute != null) {
            return attribute.isUndefined();
        }
        return false;
    }

    public static UndefinedCondition undefined() {
        return new UndefinedCondition();
    }
}
