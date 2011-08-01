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

import com.google.common.collect.Maps;
import me.qmx.jitescript.CodeBlock;

import java.util.Map;

public class DynFunction<ReturnType extends DynAtom> {

    private ReturnType result;
    private final Map<String, DynAtom> arguments = Maps.newLinkedHashMap();

    public DynFunction(Argument... arguments) {
        for (Argument argument : arguments) {
            addArgument(argument.getKey(), argument.getValue());
        }
    }

    public DynFunction addArgument(String argName, DynAtom value) {
        arguments.put(argName, value);
        return this;
    }

    public DynFunction willReturn(ReturnType result) {
        this.result = result;
        return this;
    }

    public ReturnType call() {
        if (result != null) {
            return result;
        } else {
            return (ReturnType) Undefined.UNDEFINED;
        }
    }

    protected DynAtom getArgument(Object string) {
        return arguments.get(string);
    }

    public CodeBlock getCodeBlock() {
        return CodeBlock.newCodeBlock();
    }
}
