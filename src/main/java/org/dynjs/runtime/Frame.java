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

import org.dynjs.api.Function;
import org.dynjs.api.Resolver;

public class Frame implements Resolver {
    private final Function function;
    private final Object[] params;

    public Frame(Function function, Object... params) {
        this.function = function;
        this.params = params;
    }

    public Object[] getParams() {
        return params;
    }

    @Override
    public Object resolve(String name) {
        final String[] arguments = function.getArguments();
        for (int i = 0; i < arguments.length; i++) {
            String argument = arguments[i];
            if (argument.equals(name)) {
                if (getParams().length > i) {
                    return getParams()[i];
                }
            }
        }
        return null;
    }
}
