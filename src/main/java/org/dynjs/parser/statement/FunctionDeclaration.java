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
package org.dynjs.parser.statement;

import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;

public class FunctionDeclaration extends AbstractStatement {

    private final String identifier;
    private final List<String> formalParameters;
    private BlockStatement block;

    public FunctionDeclaration(final Tree tree, final List<String> formalParameters, final BlockStatement block) {
        this( tree, null, formalParameters, block );
    }

    public FunctionDeclaration(final Tree tree, final String identifier, final List<String> formalParameters, final BlockStatement block) {
        super( tree );
        this.identifier = identifier;
        this.formalParameters = formalParameters;
        this.block = block;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public List<String> getFormalParameters() {
        return this.formalParameters;
    }
    
    public BlockStatement getBlock() {
        return this.block;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock();
    }
}
