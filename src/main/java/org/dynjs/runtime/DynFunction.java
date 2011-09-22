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

import me.qmx.jitescript.CodeBlock;

import java.util.Arrays;

public abstract class DynFunction extends DynObject {

    private final CodeBlock codeBlock;
    private final String[] arguments;

    public DynFunction(String... arguments) {
        this(CodeBlock.newCodeBlock(), arguments);
    }

    public DynFunction(CodeBlock codeBlock, String... arguments) {
        this.codeBlock = codeBlock;
        this.arguments = arguments;
        initBuiltins();
    }

    private void initBuiltins() {
        setProperty("construct", Functions.CONSTRUCTOR);
    }

    public CodeBlock getCodeBlock() {
        return this.codeBlock;
    }

    protected int getArgumentsOffset() {
        // context + scope + one-based index
        return 3;
    }

    protected int getArgumentOffset(String key) {
        // list is zero based
        return Arrays.asList(this.arguments).indexOf(key);
    }

    public String[] getArguments() {
        return this.arguments;
    }
}
