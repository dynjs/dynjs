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
package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;

import java.io.PrintStream;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.*;

public class PrintStatement implements Statement {

    private final CodeBlock codeBlock;

    public PrintStatement(final Statement expression) {
        this.codeBlock = newCodeBlock(expression.getCodeBlock())
                .getstatic(p(System.class), "out", ci(PrintStream.class))
                .swap()
                .invokevirtual(p(PrintStream.class), "println", sig(void.class, Object.class))
                .voidreturn();
    }

    @Override
    public CodeBlock getCodeBlock() {
        return this.codeBlock;
    }
}
