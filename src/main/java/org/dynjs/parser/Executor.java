/**
 *  Copyright 2011 Douglas Campos <qmx@qmx.me>
 *  Copyright 2011 Alexandre Porcelli <alexandre.porcelli@gmail.com>
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
package org.dynjs.parser;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;
import org.antlr.runtime.tree.CommonTree;
import org.dynjs.parser.statement.BlockStatement;
import org.dynjs.parser.statement.LdcStatement;
import org.dynjs.parser.statement.PrintStatement;
import org.objectweb.asm.Opcodes;

import java.util.List;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class Executor implements Opcodes {

    public byte[] program(final List<Statement> blockContent) {
        final String className = "WTF";
        JiteClass bootstrapClass = new JiteClass(className) {{
            defineMethod("main", ACC_PUBLIC | ACC_SUPER | ACC_STATIC, sig(void.class, String[].class), new CodeBlock() {{
                for (Statement statement : blockContent) {
                    append(statement.getCodeBlock());
                }
            }});
        }};
        return bootstrapClass.toBytes();
    }

    public Statement printStatement(final Statement expression) {
        return new PrintStatement(expression);
    }

    public Statement createLDC(final CommonTree stringLiteral) {
        return new LdcStatement(stringLiteral.getText());
    }

    public Statement block(final List<Statement> blockContent) {
        return new BlockStatement(blockContent);
    }
}
