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
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public abstract class AbstractUnaryOperationStatement implements Statement {

    private final Statement expression;

    public AbstractUnaryOperationStatement(Statement expression) {
        this.expression = expression;
    }

    protected abstract String operation();

    @Override
    public CodeBlock getCodeBlock() {
        final ResolveIdentifierStatement resolvable = (ResolveIdentifierStatement) expression;
        return newCodeBlock()
                .append(expression.getCodeBlock())
                .append(before())
                .append(processOperation())
                .append(after())
                .aload(DynJSCompiler.Arities.THIS)
                .swap()
                .ldc(resolvable.getName())
                .swap()
                .invokeinterface(DynJSCompiler.Types.Scope, "define", sig(void.class, String.class, Object.class))
                .aload(4);
    }

    protected CodeBlock after() {
        return DynJSCompiler.Helper.EMPTY_CODEBLOCK;
    }

    protected CodeBlock before() {
        return DynJSCompiler.Helper.EMPTY_CODEBLOCK;
    }

    protected CodeBlock store() {
        return newCodeBlock()
                .dup()
                .astore(4);
    }

    private CodeBlock processOperation() {
        return newCodeBlock()
                .append(new NumberLiteralStatement("1", 10).getCodeBlock())
                .invokedynamic(this.operation(), sig(Object.class, Object.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
    }

}
