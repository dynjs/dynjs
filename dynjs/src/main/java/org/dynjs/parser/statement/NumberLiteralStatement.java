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
package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class NumberLiteralStatement implements Statement {

    private final String value;
    private final int radix;

    public NumberLiteralStatement(String value, int radix) {
        this.value = value;
        this.radix = radix;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return newCodeBlock()
                .aload(DynJSCompiler.Arities.CONTEXT)
                .ldc(value)
                .invokevirtual(p(DynThreadContext.class), getFactoryMethod(), sig(Number.class, String.class));
    }

    private String getFactoryMethod() {
        switch (this.radix) {
            case 8:
                return "defineOctalLiteral";
            case 10:
                return "defineDecimalLiteral";
            case 16:
                return "defineHexaDecimalLiteral";
            default:
                throw new IllegalArgumentException("unsupported radix");
        }
    }
}
