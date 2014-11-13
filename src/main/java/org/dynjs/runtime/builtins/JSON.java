/**
 *  Copyright 2013 Douglas Campos, and individual contributors
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
package org.dynjs.runtime.builtins;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.builtins.types.json.Parse;
import org.dynjs.runtime.builtins.types.json.Stringify;

public class JSON extends DynObject {

    public JSON(GlobalContext globalContext) {
        super(globalContext);
        setClassName("JSON");
        defineNonEnumerableProperty(globalContext, "parse", new Parse(globalContext));
        defineNonEnumerableProperty(globalContext, "stringify", new Stringify(globalContext));
    }

}
