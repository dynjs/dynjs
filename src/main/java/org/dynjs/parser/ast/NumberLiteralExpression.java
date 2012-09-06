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

public class NumberLiteralExpression extends AbstractExpression {

    private final String text;
    private final int radix;

    public NumberLiteralExpression(final Tree tree, final String text, final int radix) {
        super(tree);
        this.text = text;
        this.radix = radix;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                if (text.indexOf(".") > 0) {
                    ldc(text);
                    invokestatic(p(Double.class), "valueOf", sig(Double.class, String.class));
                    // Double
                } else {
                    final int index = text.toLowerCase().indexOf('e');
                    if (index > 0) {
                        // scientific notation, but without the java-friendlyness. E.g. 1E21 instead of 1.0e21
                        String base = text.substring(0, index);
                        String exponent = text.substring(index);
                        String javafied = base + ".0" + exponent;
                        ldc(javafied);
                        invokestatic(p(Double.class), "valueOf", sig(Double.class, String.class));
                    } else {
                        String realText = text;
                        if (text.startsWith("0x") || text.startsWith("0X")) {
                            realText = text.substring(2);
                        }
                        ldc(realText);
                        bipush(radix);
                        invokestatic(p(Long.class), "valueOf", sig(Long.class, String.class, int.class));
                        // Long
                    }
                }
            }
        };
    }

    public String toString() {
        return this.text;
    }
}
