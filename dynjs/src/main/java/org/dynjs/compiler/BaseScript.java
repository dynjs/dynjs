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
package org.dynjs.compiler;

import org.dynjs.api.Scope;
import org.dynjs.parser.Statement;

public class BaseScript implements Scope {

    private final Statement[] statements;
    private Scope globalScope;

    public BaseScript(Statement... statements) {
        this.statements = statements;
    }

    public void setGlobalScope(Scope globalScope) {
        this.globalScope = globalScope;
    }

    @Override
    public Scope getEnclosingScope() {
        return null;
    }

    @Override
    public Object resolve(String name) {
        return globalScope.resolve(name);
    }

    @Override
    public void define(String property, Object value) {
        globalScope.define(property, value);
    }
}
