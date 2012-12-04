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

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.JavascriptTree;
import org.dynjs.parser.Statement;

public class FunctionDescriptor {

    private Tree tree;
    private final String identifier;
    private final String[] formalParameters;
    private BlockStatement block;
    private boolean strict;

    public FunctionDescriptor(final Tree tree, final String identifier, final String[] formalParameters, final BlockStatement block) {
        this.tree = tree;
        this.identifier = identifier;
        this.formalParameters = formalParameters;
        this.block = block;
        this.strict = ((JavascriptTree)tree).isStrict();
    }

    public Tree getTree() {
        return this.tree;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String[] getFormalParameters() {
        return this.formalParameters;
    }

    public BlockStatement getBlock() {
        return this.block;
    }
    
    public boolean isStrict() {
        return this.strict;
    }

    public String toString() {
        return "function " + this.identifier + "(...){...}";
    }

}
