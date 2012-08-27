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
import static me.qmx.jitescript.util.CodegenUtils.*;

import org.antlr.runtime.tree.Tree;

public class BooleanLiteralExpression extends AbstractExpression {

    private final boolean value;

    public BooleanLiteralExpression(final Tree tree, final boolean value) {
        super(tree);
        this.value = value;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                if (value) {
                    getstatic(p(Boolean.class), "TRUE", ci(Boolean.class));
                } else {
                    getstatic(p(Boolean.class), "FALSE", ci(Boolean.class));
                }
            }
        };
    }
    
    public String toString() {
        return "" + this.value;
    }
}
