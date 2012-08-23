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
package org.dynjs.parser.ast;

import me.qmx.jitescript.CodeBlock;

import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.runtime.BlockManager;

public class FunctionExpression extends AbstractExpression {

    private BlockManager blockManager;
    private FunctionDescriptor descriptor;

    public FunctionExpression(BlockManager blockManager, FunctionDescriptor descriptor) {
        super(descriptor.getTree());
        this.blockManager = blockManager;
        this.descriptor = descriptor;
    }

    public FunctionDescriptor getDescriptor() {
        return this.descriptor;
    }

    public CodeBlock getCodeBlock() {
        return CodeBlockUtils.compiledFunction(blockManager, descriptor.getFormalParameters(), descriptor.getBlock());
    }

}
